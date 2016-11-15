package my.samples

import monix.execution.Cancelable
import monix.reactive.Observable
import monix.reactive.OverflowStrategy.Unbounded
import monix.reactive.observers.Subscriber
import monix.reactive.subjects.ConcurrentSubject
import my.samples.models.MyMessages

class GlobalOutputChannel extends Observable[MyMessages] {
  implicit val s = monix.execution.Scheduler.Implicits.global
  val publishChannel = ConcurrentSubject.publish[MyMessages](Unbounded)

  override def unsafeSubscribeFn(subscriber: Subscriber[MyMessages]): Cancelable =
    publishChannel.subscribe(subscriber)
}
object GlobalOutputChannel {

  def apply = new GlobalOutputChannel
}