package my.samples.core

import akka.actor.{ ActorRef, ActorSystem }
import akka.stream.Materializer
import my.samples.GlobalOutputChannel

trait AppBindings {

  def actorSystem: ActorSystem
  def materializer: Materializer

  def supervisorActor: ActorRef
  def globalChannel: GlobalOutputChannel
}
object AppBindings {

  def apply(system: ActorSystem, actorMaterializer: Materializer) = new AppBindings {

    override val actorSystem = system
    override val materializer = actorMaterializer
    override val globalChannel: GlobalOutputChannel =
      GlobalOutputChannel.apply

    override val supervisorActor: ActorRef =
      system.actorOf(SupervisorActor.props(globalChannel))
  }
}