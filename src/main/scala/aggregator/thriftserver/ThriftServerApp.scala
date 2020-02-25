package aggregator.thriftserver

import aggregator.logging.v1.{LoggingEvent, ThriftService}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.twitter.finagle.Thrift
import com.twitter.util.Future

object ThriftServerApp extends App {

  implicit val actorSystem: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty, "thrift-server")

  class ServerImpl(producer: KafkaProducer) extends ThriftService.MethodPerEndpoint {

    override def logEvent(event: LoggingEvent): Future[Unit] = {
      actorSystem.log.info(s"Received event $event")
      Future(producer.pushEvent(event))
    }

  }

  val server = Thrift.server.serveIface(":8080", new ServerImpl(new KafkaProducer()))

}
