package my.samples.services

import com.typesafe.scalalogging.LazyLogging
import monix.execution.atomic.AtomicBoolean

final class ZombieConnectorService extends LazyLogging {

  private[this] val connectionStatus = AtomicBoolean(false)

  def connect() =
    logger.info(s"ZombieConnectorService connection status = ${connectionStatus.get}")
  connectionStatus.compareAndSet(expect = false, update = true)

  def disconnect() =
    logger.info(s"ZombieConnectorService connection status = ${connectionStatus.get}")
  connectionStatus.compareAndSet(expect = true, update = false)
}
object ZombieConnectorService {
  def apply = new ZombieConnectorService
}