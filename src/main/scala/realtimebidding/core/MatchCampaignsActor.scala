package realtimebidding.core

import akka.actor.{Actor, ActorRef}
import realtimebidding.clients.CampaignClient.{Campaigns, GetCampaigns}
import realtimebidding.models._

import java.util.UUID

class MatchCampaignsActor(campaignClientActor: ActorRef) extends Actor {

  var campaignSeq = Option.empty[Seq[Campaign]]
  var bidOption = Option.empty[BidRequest]

  override def receive: Receive = {
    case MatchCampaigns(bid) =>
      bidOption = Some(bid)
      campaignClientActor ! GetCampaigns()
      context.become(waitingResponses)
  }

  def waitingResponses: Receive = {
    case Campaigns(campaigns) =>
      campaignSeq = Some(campaigns)
      replyIfReady
  }

  private def replyIfReady: Unit =
    if(campaignSeq.nonEmpty) {

      val campaigns = campaignSeq.head
      val bid = bidOption.head

      val matchedCampaignBanners = scala.collection.mutable.Map[Int, List[Banner]]()
      var matchedBanners: List[Banner] = List.empty
      val newId = UUID.randomUUID().toString

      def bidDeviceCountry: Option[String] = if (bid.device.isDefined && bid.device.get.geo.isDefined) bid.device.get.geo.get.country else None
      def bidUserCountry: Option[String] = if (bid.user.isDefined && bid.user.get.geo.isDefined) bid.user.get.geo.get.country else None

      def validateCountry(cmp: Campaign): Boolean =
        (bidDeviceCountry.isDefined && cmp.country.equals(bidDeviceCountry.get)) || (bidUserCountry.isDefined && cmp.country.equals(bidUserCountry.get))

      def validateImpressions(campaignId: Int, banners: List[Banner], bidPrice: Double): Boolean = {
        val impressions = bid.imp.getOrElse(List.empty);

        def validateWandH(imp: Impression, bannerW: Int, bannerH: Int): Boolean =
          if((imp.bidFloor.isDefined && imp.bidFloor.head > bidPrice) ||
            (imp.w.isDefined && imp.w.head != bannerW) || (imp.h.isDefined && imp.h.head != bannerH) ||
            (imp.hmax.isDefined && imp.hmax.head < bannerH) || (imp.hmin.isDefined && imp.hmin.head > bannerH) ||
            (imp.wmax.isDefined && imp.wmax.head < bannerW) || (imp.wmin.isDefined && imp.wmin.head > bannerW)) false else true

        matchedBanners = {
          for {
            imp: Impression <- impressions
            banner: Banner <- banners
            if validateWandH(imp, banner.width, banner.height)
          } yield banner
        }
        matchedCampaignBanners.put(campaignId, matchedBanners)

        if (matchedBanners.size.equals(impressions.size)) true else false
      }

      val filteredCampaigns = campaigns.filter(cmp => {
        cmp.targeting.targetedSiteIds.contains(bid.site.id) && validateCountry(cmp) && validateImpressions(cmp.id, cmp.banners, cmp.bid)
      })

      def closest(c1: Campaign, c2: Campaign): Campaign = {
        def isEqualDeviceCountry(cmp: Campaign): Boolean = bidDeviceCountry.head.equals(cmp.country)
        def isEqualUserCountry(cmp: Campaign): Boolean = bidUserCountry.head.equals(cmp.country)

        def randomCmp: Campaign = {
          val r = new scala.util.Random
          if(r.nextInt(2) % 2 == 0) c1 else c2
        }

        if (((bidDeviceCountry.isDefined && isEqualDeviceCountry(c1) && isEqualDeviceCountry(c2))
                && (bidUserCountry.isDefined && isEqualUserCountry(c1) && isEqualUserCountry(c2)))
            || (bidDeviceCountry.isEmpty && bidUserCountry.isDefined && isEqualUserCountry(c1) && isEqualUserCountry(c2))
            || (bidUserCountry.isEmpty && bidDeviceCountry.isDefined && isEqualDeviceCountry(c1) && isEqualDeviceCountry(c2))
        ) randomCmp
        else if (bidDeviceCountry.isDefined && isEqualDeviceCountry(c1)) c1
        else if (bidDeviceCountry.isDefined && isEqualDeviceCountry(c2)) c2
        else if (bidUserCountry.isDefined && isEqualUserCountry(c1)) c1
        else if (bidUserCountry.isDefined && isEqualUserCountry(c2)) c2
        else randomCmp
      }

      val winner = filteredCampaigns.reduceLeft(closest)
      val winnerBanner = if(matchedCampaignBanners.contains(winner.id) && matchedCampaignBanners.get(winner.id).head.nonEmpty) Some(matchedCampaignBanners.get(winner.id).head.head) else None

      val bidResponse = BidResponse(
        id            = newId,
        bidRequestId  = bid.id,
        price         = winner.bid,
        adId          = Some(winner.id),
        banner        = winnerBanner
      )

      context.parent ! bidResponse

    } else {
      context.parent ! NotExist("No campaigns!")
    }
}
