package aggregator.thriftserver

import aggregator.logging.v1.LoggingEvent
import akka.Done
import akka.actor.typed.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.typesafe.config.Config
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

import scala.concurrent.Future

class KafkaProducer(implicit val system: ActorSystem[Nothing]) {

  import akka.actor.typed.scaladsl.adapter._

  implicit val materializer: ActorMaterializer = ActorMaterializer()(system.toUntyped)

  final val config: Config = system.settings.config.getConfig("akka.kafka.producer")
  final val producerSettings: ProducerSettings[String, Array[Byte]] =
    ProducerSettings(config, new StringSerializer, new ByteArraySerializer)
      .withBootstrapServers(bootstrapServers = "localhost:29092")

  def pushEvent(event: LoggingEvent): Future[Done] =
    Source(Set(event))
      .map(LoggingEventSerde.serialize)
      .map(value => new ProducerRecord[String, Array[Byte]]("logs-v1", value))
      .runWith(Producer.plainSink(producerSettings))

}

