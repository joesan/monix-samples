package my.samples

import my.samples.models.MyMessages.{ Destroy, Init, Tick }
import play.api.libs.json._

package object models {

  implicit object MyMessagesWrites extends Writes[MyMessages] {

    def writes(myMessages: MyMessages): JsValue = myMessages match {
      case Init =>
        Json.toJson("INIT")
      case Destroy =>
        Json.toJson("DESTROY")
      case tick: Tick =>
        Json.obj(
          "source " -> tick.source,
          "long" -> tick.long
        )
    }
  }
}g