# Thrift-Kafka-Cassandra Log Aggregator
## Overview
Consisting of 3 applications to eventually aggregate a logging event   
from a client/server using Thrift through Kafka to Cassandra.

### Thrift Client
Randomly generates a logging event every 0.5 seconds  
Sends it to the Thrift Server  

Uses Twitter Scrooge for auto generated Twitter Finagle Client
### Thrift Server
Has endpoint for logging events
Pushes event received to Kafka using Akka-Stream-Kafka to topic `logs-v1`

Uses Twitter Scrooge for auto generated Twitter Finagle Server

### Kafka Consumer
Consumes logging events from topic `logs-v1`
Pushes event to Cassandra using Alpakka-Cassandra

(see also Cassandra schema cassandra/cassandra-init.cql)

### Logging Event
Definition using Apache Thrift (see src/main/thrift/loggingEvent.thrift)

1. required i16 v
2. required i64 time
3. required string message
4. optional string host   
5. optional Level log_level,
6. optional MessageType message_type,

## Build


## Run


### Readings
- [https://twitter.github.io/scrooge/](Twitter Scrooge - Thrift Code Generator in Scala)
- [https://github.com/bitnami/bitnami-docker-kafka](Bitnami - Docker - Kafka)
- [https://github.com/bitnami/bitnami-docker-cassandra](Bitnami - Docker - Cassandra)
- [https://www.youtube.com/watch?v=mgVH3NMGMUg](Reactive Summit - Reactive Kafka with Akka Streams)
- [https://medium.com/code-zen/cassandra-schemas-for-beginners-like-me-9714cee9236a](Cassandra Schemas for Beginners - Medium)
- [https://medium.com/rahasak/end-to-end-streaming-from-kafka-to-cassandra-447d0e6ba25a](End to end streaming from Kafka to Cassandra - Medium)
- [https://books.google.gr/books?id=JaJdAgAAQBAJ&pg=PA20&lpg=PA20&dq=how+to+store+log+events+in+cassandra&source=bl&ots=KFAhotPjZ6&sig=ACfU3U3cGgxH2cowMH4bsyOgRvQ7suHJAg&hl=el&sa=X&ved=2ahUKEwjdtaSLteznAhWJUBUIHXv0AOEQ6AEwBnoECAgQAQ#v=onepage&q=how%20to%20store%20log%20events%20in%20cassandra&f=false](Practical Cassandra: A Developer's Approach - Book) 
- [https://pbassiner.github.io/blog/defining_multi-project_builds_with_sbt](Multi-Project Builds with SBT)