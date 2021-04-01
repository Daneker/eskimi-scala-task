package realtimebidding

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import org.slf4j.{Logger, LoggerFactory}
import realtimebidding.clients.CampaignClient
import realtimebidding.routing.BidsRoute

import scala.concurrent.ExecutionContext

class RestEndpoint(serverPort: Int)
                  (implicit actorSystem: ActorSystem, mat: Materializer, ec: ExecutionContext)
  extends BidsRoute {

  val log: Logger = LoggerFactory.getLogger("Boot")

  override def actorSys: ActorSystem = implicitly

  override val campaignClientActor: ActorRef = actorSys.actorOf(Props[CampaignClient], "campaign-client")

  val route: Route = bidsRoute

  def start(): Unit = {
    Http().bind("0.0.0.0", serverPort).runForeach(_.handleWith(Route.handlerFlow(route)))
    log.info(s"Server started on port $serverPort")
  }

}
