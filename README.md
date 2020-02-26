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

(see also Cassandra schema docker/cassandra/cassandra-init.cql)

### Logging Event
Definition using Apache Thrift (see thrift-client/src/main/thrift/loggingEvent.thrift)

1. required i16 v
2. required i64 time
3. required string message
4. optional string host   
5. optional Level log_level,
6. optional MessageType message_type,

## Build
Built using [sbt](https://www.scala-lang.org/documentation/getting-started-sbt-track/getting-started-with-scala-and-sbt-on-the-command-line.html)
Build and publish the docker images.
> sbt docker:publishLocal

## Run
### Install Docker-Compose
[https://docs.docker.com/compose/install/](Docker-Compose)
1. Run this command to download the current stable release of Docker Compose:
> sudo curl -L "https://github.com/docker/compose/releases/download/1.25.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
2. Apply executable permissions to the binary:
> sudo chmod +x /usr/local/bin/docker-compose
 
### Start Applications
#### Start Cassandra, Kafka
This will also include Zookeeper for Kafka.
> cd docker
> sudo docker-compose up -d cassandra kafka

Wait a moment for images to get up and running.
You can check by running
> sudo docker-compose ps

#### Start Thrift-Client Thrift-Server Kafka-Consumer apps
> sudo docker-compose up thrift-client thrift-server kafka-consumer

### References
- [Twitter Scrooge - Thrift Code Generator in Scala](https://twitter.github.io/scrooge/)
- [Bitnami - Docker - Kafka](https://github.com/bitnami/bitnami-docker-kafka)
- [Bitnami - Docker - Cassandra](https://github.com/bitnami/bitnami-docker-cassandra)
- [Reactive Summit - Reactive Kafka with Akka Streams](https://www.youtube.com/watch?v=mgVH3NMGMUg)
- [Cassandra Schemas for Beginners - Medium](https://medium.com/code-zen/cassandra-schemas-for-beginners-like-me-9714cee9236a)
- [End to end streaming from Kafka to Cassandra - Medium](https://medium.com/rahasak/end-to-end-streaming-from-kafka-to-cassandra-447d0e6ba25a)
- [Practical Cassandra: A Developer's Approach - Book](https://books.google.gr/books?id=JaJdAgAAQBAJ&pg=PA20&lpg=PA20&dq=how+to+store+log+events+in+cassandra&source=bl&ots=KFAhotPjZ6&sig=ACfU3U3cGgxH2cowMH4bsyOgRvQ7suHJAg&hl=el&sa=X&ved=2ahUKEwjdtaSLteznAhWJUBUIHXv0AOEQ6AEwBnoECAgQAQ#v=onepage&q=how%20to%20store%20log%20events%20in%20cassandra&f=false) 
- [Multi-Project Builds with SBT](https://pbassiner.github.io/blog/defining_multi-project_builds_with_sbt)