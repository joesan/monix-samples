package my.samples.controllers

import play.api.mvc._
import my.samples.core.AppBindings
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer

// how to close this publish channel in the AppBindings?
final class MyApplicationController(bindings: AppBindings) extends Controller {

  implicit val messageFlowTransformer =
    MessageFlowTransformer.jsonMessageFlowTransformer[JsValue, JsValue]

  implicit val actorSystem = bindings.actorSystem
  implicit val materializer = bindings.materializer

  def home = Action { implicit request =>
    Ok("The API is ready")
  }

  def observable = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef(out => MyWebSocketActor.props(bindings.globalChannel, out))
  }

  def connectableObservable = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef(out => MyWebSocketActor.props(bindings.globalChannel, out))
  }
}