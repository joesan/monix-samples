package my.samples.core

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.{ LazyLogging, StrictLogging }
import controllers.Assets
import play.api.{ Application, BuiltInComponentsFromContext }
import play.api.libs.ws.ahc.AhcWSComponents
import play.api._
import my.samples.controllers.MyApplicationController
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import router.Routes
import scala.concurrent.Future

// compile time DI for loading the play application
final class MyApplicationLoader extends ApplicationLoader with LazyLogging {

  override def load(context: Context): Application = {
    val cfg = ConfigFactory.defaultApplication()
    new MyApp(context).application
  }
}
class MyApp(context: Context)
    extends BuiltInComponentsFromContext(context) with AhcWSComponents with StrictLogging {

  implicit val s = monix.execution.Scheduler.Implicits.global

  def stop(bindings: AppBindings) = {
    logger.info("stopping application")
    bindings.globalChannel.publishChannel.onComplete()
  }

  def start = {
    logger.info("starting application")
    AppBindings(actorSystem, materializer)
  }

  // 1. create the dependencies that will be injected
  lazy val appBindings = start

  // 2. inject the dependencies into the controllers
  lazy val applicationController = new MyApplicationController(appBindings)
  lazy val assets = new Assets(httpErrorHandler)
  override def router: Router = new Routes(
    httpErrorHandler, applicationController, assets
  )

  // 3. add the shutdown hook to properly dispose all connections
  applicationLifecycle.addStopHook { () => Future(stop(appBindings)) }
}