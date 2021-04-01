package realtimebidding.routing

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.{Route, RouteResult}
import realtimebidding.models.RestMessage
import realtimebidding.utils.JsonSupport

import scala.concurrent.Promise

trait BaseRoute extends PerRequestCreator with JsonSupport {
  def actorSys: ActorSystem

  def handleRequest(targetProps: Props, request: RestMessage): Route = ctx => {
    val p = Promise[RouteResult]
    perRequest(ctx, targetProps, request, p)(actorSys)
    p.future
  }
}
