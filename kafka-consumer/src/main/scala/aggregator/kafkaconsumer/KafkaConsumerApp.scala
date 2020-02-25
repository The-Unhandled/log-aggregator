package aggregator.kafkaconsumer

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object KafkaConsumerApp extends App {

  implicit val actorSystem: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty, "kafka-consumer")

  val consumer = new KafkaConsumer()

}
