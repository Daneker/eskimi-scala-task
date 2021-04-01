package realtimebidding.models

trait RestMessage
case class MatchCampaigns(bidRequest: BidRequest) extends RestMessage

object SuccessfulOperation
final case class Error(msg: String)
final case class NotExist(msg: String)
