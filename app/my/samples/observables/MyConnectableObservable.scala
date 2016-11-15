package my.samples.observables

import com.typesafe.scalalogging.LazyLogging
import monix.execution.{ Cancelable, Scheduler }
import monix.execution.cancelables.{ BooleanCancelable, SingleAssignmentCancelable }
import monix.reactive.Observable
import monix.reactive.observables.ConnectableObservable
import monix.reactive.observers.Subscriber
import my.samples.services.ZombieConnectorService

import scala.concurrent.duration._

class MyConnectableObservable(service: ZombieConnectorService)(implicit s: Scheduler) extends ConnectableObservable[Long] with LazyLogging {

  private[this] val connection = SingleAssignmentCancelable()
  private val serviceName = service.getClass.getName

  override def connect(): Cancelable = {
    logger.info(s"connecting to the service $serviceName")

    // 1. we connect to the service first
    service.connect()

    // 2. we register a callback that says what to do when we disconnect
    connection := BooleanCancelable { () =>
      service.disconnect()
    }

    connection
  }

  def close() = {
    logger.info(s"shutting down connection to service $serviceName")
    connection.cancel()
  }

  override def unsafeSubscribeFn(subscriber: Subscriber[Long]): Cancelable =
    Observable.interval(1.second).subscribe(subscriber)
}
object MyConnectableObservable {
  def apply(service: ZombieConnectorService)(implicit s: Scheduler) =
    new MyConnectableObservable(service)
}