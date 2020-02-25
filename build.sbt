name := "thrift-client"

version := "0.1"

scalaVersion := "2.11.12"

lazy val akkaVersion = "2.5.21"

lazy val twitterVersion = "20.1.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % "2.0.2",
  "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "1.1.2",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "org.apache.thrift" % "libthrift" % "0.10.0",
  "com.twitter" % "scrooge-core_2.11" % twitterVersion,
  "com.twitter" %% "scrooge-core" % twitterVersion,
  "com.twitter" %% "scrooge-serializer" % twitterVersion,
  "com.twitter" %% "finagle-thrift" % twitterVersion,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)
