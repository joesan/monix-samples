package my.samples.observables

import monix.execution.Cancelable
import monix.reactive.Observable
import monix.reactive.observers.Subscriber
import scala.concurrent.duration._

class MyObservable extends Observable[Long] {
  override def unsafeSubscribeFn(subscriber: Subscriber[Long]): Cancelable = {
    Observable.interval(1.second).subscribe(subscriber)
  }
}
object MyObservable {
  def apply = new MyObservable
}