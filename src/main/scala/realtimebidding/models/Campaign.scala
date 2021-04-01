package realtimebidding.models

case class Campaign(id: Int, country: String, targeting: Targeting, banners: List[Banner], bid: Double)
case class Targeting(targetedSiteIds: Seq[String])
case class Banner(id: Int, width: Int, height: Int, src: String)
