package aggregator.kafkaconsumer

import aggregator.logging.v1.LoggingEvent
import com.twitter.scrooge.BinaryThriftStructSerializer

// TODO: move to common project
object LoggingEventSerde {

  val serializer: BinaryThriftStructSerializer[LoggingEvent] = BinaryThriftStructSerializer(LoggingEvent)

  def serialize(loggingEvent: LoggingEvent): Array[Byte] = serializer.toBytes(loggingEvent)
  def deserialize(bytes: Array[Byte]): LoggingEvent = serializer.fromBytes(bytes)

}
