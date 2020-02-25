package aggregator.kafkaconsumer

import java.sql.Timestamp

import aggregator.logging.v1.LoggingEvent
import akka.Done
import akka.actor.typed.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.alpakka.cassandra.scaladsl.CassandraSink
import akka.stream.scaladsl.Sink
import com.datastax.driver.core.{BoundStatement, Cluster, PreparedStatement, Session}
import com.typesafe.config.Config
import aggregator.thriftserver.LoggingEventSerde
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class KafkaConsumer(implicit val system: ActorSystem[Nothing]) {

  import akka.actor.typed.scaladsl.adapter._

  implicit val materializer: ActorMaterializer = ActorMaterializer()(system.toUntyped)

  // Kafka Configuration
  val config: Config = system.settings.config.getConfig("akka.kafka.consumer")

  val kafkaConsumerSettings: ConsumerSettings[String, Array[Byte]] =
    ConsumerSettings(config, new StringDeserializer, new ByteArrayDeserializer)
      .withBootstrapServers(bootstrapServers = "localhost:29092")
      .withGroupId("kafka-consumer-1")

  // Cassandra Configuration
  implicit val session: Session = Cluster.builder
    .addContactPoint("localhost")
    .withPort(9042)
    .withCredentials("cassandra", "cassandra")
    .build
    .connect()

  // Insert Statement for Logs v1
  val statement: PreparedStatement = session.prepare(s"INSERT INTO aggregator.logs_v1(host, time, log_level, message_type, message) VALUES (?, ?, ?, ?, ?)")
  val statementBinder: (LoggingEvent, PreparedStatement) => BoundStatement = (loggingEvent: LoggingEvent, statement: PreparedStatement) =>
    statement.bind(
      loggingEvent.host,
      new Timestamp(loggingEvent.time),
      loggingEvent.logLevel.toString,
      loggingEvent.messageType.toString,
      loggingEvent.message)

  val cassandraSink: Sink[LoggingEvent, Future[Done]] = CassandraSink(parallelism = 2, statement, statementBinder)

  // Kafka To Cassandra
  val control: Future[Done] = Consumer
    .committableSource(kafkaConsumerSettings, Subscriptions.topics("logs-v1"))
    .map { r => LoggingEventSerde.deserialize(r.record.value()) }
    .runWith(cassandraSink)

  implicit val ec: ExecutionContextExecutor = system.executionContext

  control onComplete {
    case Success(_) =>
      println("Successfully terminated consumer")
      system.terminate()
    case Failure(err) =>
      println(err.getMessage)
      system.terminate()
  }

}
