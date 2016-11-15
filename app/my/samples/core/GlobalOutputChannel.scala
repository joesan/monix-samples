package my.samples.core

import monix.execution.Cancelable
import monix.reactive.OverflowStrategy.Unbounded
import monix.reactive.observers.Subscriber
import monix.reactive.subjects.ConcurrentSubject
import my.samples.models.MyMessages

class GlobalOutputChannel {
  implicit val s = monix.execution.Scheduler.Implicits.global
  val publishChannel: ConcurrentSubject[MyMessages, MyMessages] = ConcurrentSubject.publish[MyMessages](Unbounded)

  def unsafeSubscribeFn(subscriber: Subscriber[MyMessages]): Cancelable =
    publishChannel.subscribe(subscriber)
}
object GlobalOutputChannel {

  def apply = new GlobalOutputChannel
}