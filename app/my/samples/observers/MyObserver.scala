package my.samples.observers

import akka.actor.ActorRef
import monix.execution.Ack.Continue
import monix.execution.{ Ack, Scheduler }
import monix.reactive.observers.Subscriber
import my.samples.models.MyMessages
import my.samples.models.MyMessages.Tick
import org.slf4j.LoggerFactory

import scala.concurrent.Future

class MyObserver(actorRef: ActorRef, sourceName: String)(implicit s: Scheduler) extends Subscriber[Long] {

  private[this] def logger = LoggerFactory.getLogger(this.getClass)

  override implicit def scheduler: Scheduler = s

  override def onError(ex: Throwable): Unit =
    logger.error(s"error happened when processing the stream: error message << ${ex.getMessage} >>")

  override def onComplete(): Unit =
    logger.info("stream completed")

  override def onNext(elem: Long): Future[Ack] = {
    logger.info(s"message received from source $sourceName --> $elem")
    actorRef ! Tick(sourceName, elem)
    Continue
  }
}
object MyObserver {
  def apply(actorRef: ActorRef, sourceName: String)(implicit s: Scheduler) = {
    new MyObserver(actorRef, sourceName)(s)
  }
}