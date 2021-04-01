package realtimebidding.clients

import akka.actor.Actor
import realtimebidding.clients.CampaignClient.{Campaigns, GetCampaigns}
import realtimebidding.models.{Banner, Campaign, Targeting}


class CampaignClient extends Actor {
  def receive: Receive = {
    case GetCampaigns()      => {
      sender() ! Campaigns(activeCampaigns)
    }
  }



  val activeCampaigns = Seq(
    Campaign(
      id = 1,
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = Seq("0006a522ce0f4bbbbaa6b3c38cafaa0f")
      ),
      banners = List(
        Banner(
          id = 1,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        ),
        Banner(
          id = 1,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 450,
          height = 350
        )
      ),
      bid = 5d
    ),
    Campaign(
      id = 2,
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = Seq("0006a522ce0f4bbbbaa6b3c38cafaa0f")
      ),
      banners = List(
        Banner(
          id = 1,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 100,
          height = 70
        )
      ),
      bid = 3.5
    ),
    Campaign(
      id = 3,
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = Seq("0006a522ce0f4bbbbaa6b3c38cafaa0f")
      ),
      banners = List.empty,
      bid = 2d
    ),
    Campaign(
      id = 4,
      country = "KZ",
      targeting = Targeting(
        targetedSiteIds = Seq("0006a522ce0f4bbbbaa6b3c38cafaa0f")
      ),
      banners = List(
        Banner(
          id = 1,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 5d
    ),
    Campaign(
      id = 5,
      country = "KZ",
      targeting = Targeting(
        targetedSiteIds = Seq("0006a522ce0f4bbbbaa6b3c38cafaa0f")
      ),
      banners = List(
        Banner(
          id = 1,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 10d
    ),
    Campaign(
      id = 6,
      country = "RU",
      targeting = Targeting(
        targetedSiteIds = Seq("0006a522ce0f4bbbbaa6b3c38cafaa0f")
      ),
      banners = List(
        Banner(
          id = 1,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 5d
    )
  )

}

object CampaignClient {
  case class GetCampaigns()
  case class Campaigns(campaigns: Seq[Campaign])
}
