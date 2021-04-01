package realtimebidding

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object Boot {

  implicit val system = ActorSystem("actor-per-request")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  def main(args: Array[String]): Unit = startup()

  def startup(): Unit = {
    new RestEndpoint(38080).start()
    setShutDownHook()
  }

  def setShutDownHook(): Unit = {
    sys.addShutdownHook(() => {
      val future = system.terminate()
      Await.result(future, 120.seconds)
    })
  }

}
