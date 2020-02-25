package aggregator.thriftclient

import aggregator.logging.v1.ThriftService
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.twitter.finagle.Thrift

import scala.concurrent.duration._

object ThriftClientApp extends App {

  import akka.actor.typed.scaladsl.adapter._

  implicit val system: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty, "thrift-client")
  implicit val materializer: ActorMaterializer = ActorMaterializer()(system.toUntyped)

  val loggingEndpoint: ThriftService.MethodPerEndpoint = Thrift.client.build[ThriftService.MethodPerEndpoint]("localhost:8080")

  Source.fromIterator(() => Iterator.continually(LogEventFactory.randomEvent()))
    .delay(0.5.seconds)
    .wireTap(event => system.log.info(s"Created event $event"))
    .runForeach(loggingEndpoint.logEvent)

}
