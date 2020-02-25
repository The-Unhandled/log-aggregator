package aggregator.thriftclient

import java.net.InetAddress
import java.time.Instant

import aggregator.logging.v1.{Level, LoggingEvent, MessageType}

import scala.util.Random

object LogEventFactory {

  val host: String = InetAddress.getLocalHost.getHostName
  val eventVersion: Short = 1

  val coins = Seq("BTC", "ETH", "XRP", "BCH", "LTC", "EOS", "XTZ", "ADA", "XLM", "LINK")

  def randomEvent(): LoggingEvent = {
    val time = Instant.now().toEpochMilli

    val logLevel = Random.nextInt(100) match {
      case 0 => Level.Fatal
      case n if n < 10 => Level.Error
      case n if n < 20 => Level.Warn
      case n if n < 70 => Level.Info
      case n if n < 90 => Level.Debug
      case _ => Level.Trace
    }

    val messageType = Random.nextInt(2) match {
      case 0 => MessageType.Buy
      case 1 => MessageType.Sell
    }

    val coin = coins(Random.nextInt(coins.size))
    val price = Random.nextInt(1000) + 1

    LoggingEvent(v = eventVersion, time = time, message = s"$coin for $$$price", host = Some(host), logLevel = Some(logLevel), messageType = Some(messageType))
  }

}
