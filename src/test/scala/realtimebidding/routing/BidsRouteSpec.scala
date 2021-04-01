package realtimebidding.routing

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentTypes, MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.Ignore
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import realtimebidding.clients.CampaignClient
import realtimebidding.models._
import realtimebidding.utils.JsonSupport

import java.util.UUID

@Ignore
class BidsRouteSpec extends AnyWordSpec
  with Matchers
  with ScalaFutures
  with ScalatestRouteTest
  with BidsRoute
  with JsonSupport {

  "be able to request bid (POST /bids)" in {
    val site = Site("0006a522ce0f4bbbbaa6b3c38cafaa0f", "fake.tld")
    val user = Some(User("USARIO1", Some(Geo(Some("LT")))))
    val device = Some(Device("440579f4b408831516ebd02f6e1c31b4", Some(Geo(Some("LT")))))
    val imp = Some(List(Impression("1", Some(50), Some(300), Some(300), Some(100), Some(300), Some(250), Some(3.12123))))

    val newId = UUID.randomUUID().toString
    val bidRequest = BidRequest(newId, imp, site, user, device)

    val bidRequestEntity = Marshal(bidRequest).to[MessageEntity].futureValue

    // using the RequestBuilding DSL:
    val request = Post("/bids").withEntity(bidRequestEntity)

    request ~> bidsRoute ~> check {
      status should ===(StatusCodes.OK)

      contentType should ===(ContentTypes.`application/json`)

      entityAs[String] should ===("""{
                                    |    "price": 5.0,
                                    |    "bidRequestId": "SGu1Jpq1IO",
                                    |    "banner": {
                                    |        "id": 1,
                                    |        "width": 300,
                                    |        "height": 250,
                                    |        "src": "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg"
                                    |    },
                                    |    "id": "3733c5b8-3716-40f1-bd8c-8f00d7c3b60d",
                                    |    "adId": 1
                                    |}""".stripMargin)
    }
  }

  lazy val testKit = ActorTestKit()
  implicit def typedSystem = testKit.system
  override def createActorSystem(): akka.actor.ActorSystem =
    testKit.system.classicSystem
  override def campaignClientActor: ActorRef = actorSys.actorOf(Props[CampaignClient], "campaign-client")

  override def actorSys: ActorSystem = testKit.system.classicSystem
}
