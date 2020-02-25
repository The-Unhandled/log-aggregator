namespace scala aggregator.logging.v1
enum Level {
  FATAL = 1,
  ERROR = 2,
  WARN = 3,
  INFO = 4,
  DEBUG = 5,
  TRACE = 6
}

enum MessageType {
    BUY = 1,
    SELL = 2
}

struct LoggingEvent {
    1: required i16 v,
    2: required i64 time,
    3: required string message,
    4: optional string host,
    5: optional Level log_level,
    6: optional MessageType message_type,
    #7: optional string newField
}

service ThriftService {

    void logEvent(1: LoggingEvent event)

}

