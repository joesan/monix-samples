package my.samples.models

sealed trait MyMessages

object MyMessages {
  case object Init extends MyMessages
  case object Destroy extends MyMessages
  case class Tick(source: String, long: Long) extends MyMessages
}