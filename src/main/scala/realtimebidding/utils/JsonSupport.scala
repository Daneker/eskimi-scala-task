package realtimebidding.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import realtimebidding.models.{Banner, BidRequest, BidResponse, Device, Error, Geo, Impression, NotExist, Site, User}
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val errorFormat = jsonFormat1(Error)
  implicit val notExistFormat = jsonFormat1(NotExist)

  implicit val geoFormat = jsonFormat1(Geo)
  implicit val deviceFormat = jsonFormat2(Device)
  implicit val userFormat = jsonFormat2(User)
  implicit val siteFormat = jsonFormat2(Site)
  implicit val impressionFormat = jsonFormat8(Impression)
  implicit val bidRequestFormat = jsonFormat5(BidRequest)
  implicit val bannerFormat = jsonFormat4(Banner)
  implicit val bidResponseFormat = jsonFormat5(BidResponse)

}
