name := "log-aggregator"

lazy val baseSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.12",
)

lazy val akkaVersion = "2.5.21"
lazy val twitterVersion = "20.1.0"

lazy val logAggregator = project
  .in(file("."))
  .settings(baseSettings)
  .aggregate(
    thriftClient,
    thriftServer,
    kafkaConsumer
  )

lazy val thriftClient = project.in(file("thrift-client"))
  .settings(
    baseSettings,
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging, SbtNativePackager)

lazy val thriftServer = project.in(file("thrift-server"))
  .settings(
    baseSettings,
    dockerExposedPorts ++= Seq(9000),
    libraryDependencies ++= commonDependencies ++ Seq(
      "com.twitter" %% "scrooge-serializer" % twitterVersion,
      "com.typesafe.akka" %% "akka-stream-kafka" % "2.0.2",
    )
  )
  .enablePlugins(JavaAppPackaging, SbtNativePackager)

lazy val kafkaConsumer = project.in(file("kafka-consumer"))
  .settings(
    baseSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      "com.twitter" %% "scrooge-serializer" % twitterVersion,
      "com.typesafe.akka" %% "akka-stream-kafka" % "2.0.2",
      "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "1.1.2",
    )
  )
  .enablePlugins(JavaAppPackaging, SbtNativePackager)

lazy val commonDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "org.apache.thrift" % "libthrift" % "0.10.0",
  "com.twitter" % "scrooge-core_2.11" % twitterVersion,
  "com.twitter" %% "scrooge-core" % twitterVersion,
  "com.twitter" %% "finagle-thrift" % twitterVersion,
  "com.twitter" %% "finagle-thrift" % twitterVersion,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)
