package realtimebidding.routing

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import realtimebidding.core.MatchCampaignsActor
import realtimebidding.models.{BidRequest, MatchCampaigns}
import realtimebidding.utils.JsonSupport

trait BidsRoute extends BaseRoute with JsonSupport {

  def campaignClientActor: ActorRef

  val bidsRoute: Route =
    post {
      path("bids") {
        entity(as[BidRequest]) { bidDTO =>
          handleRequestWithProps(
            bidDTO
          )
        }
      }
    }

  def handleRequestWithProps(bidRequest: BidRequest): Route = {
    handleRequest(Props(classOf[MatchCampaignsActor], campaignClientActor), MatchCampaigns(bidRequest))
  }
}
