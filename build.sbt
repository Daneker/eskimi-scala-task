scalaVersion := "2.12.1"

name := "real-time-bidding"
version := "1.0"

val AkkaVersion = "2.6.13"
val AkkaHttpVersion = "10.0.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka"           %% "akka-actor"             % AkkaVersion,
  "com.typesafe.akka"           %% "akka-stream"            % AkkaVersion,
  "com.typesafe.akka"           %% "akka-http"              % AkkaHttpVersion,
  "com.typesafe.akka"           %% "akka-http-spray-json"   % AkkaHttpVersion,
  "ch.qos.logback"              %  "logback-classic"        % "1.2.3",
  "com.typesafe.scala-logging"  %% "scala-logging"          % "3.7.2",

  "com.typesafe.akka" %% "akka-testkit"             % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit"      % AkkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit"        % AkkaHttpVersion,
  "org.scalactic"     %% "scalactic"                % "3.2.5",
  "org.scalatest"     %% "scalatest"                % "3.2.5" % Test

)

exportJars := true
mainClass in Compile := Option("realtimebidding.Boot")

enablePlugins(JavaAppPackaging)
