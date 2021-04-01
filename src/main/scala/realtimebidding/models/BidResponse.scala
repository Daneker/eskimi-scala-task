package realtimebidding.models

case class BidResponse(id: String,
                       bidRequestId: String,
                       price: Double,
                       adId: Option[Int],
                       banner: Option[Banner])
