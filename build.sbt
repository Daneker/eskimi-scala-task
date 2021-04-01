scalaVersion := "2.12.1"

name := "real-time-bidding"
version := "1.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka"           %% "akka-actor"             % "2.5.3",
  "com.typesafe.akka"           %% "akka-stream"            % "2.5.3",
  "com.typesafe.akka"           %% "akka-http"              % "10.0.9",
  "com.typesafe.akka"           %% "akka-http-spray-json"   % "10.0.9",
  "ch.qos.logback"              %  "logback-classic"        % "1.2.3",
  "com.typesafe.scala-logging"  %% "scala-logging"          % "3.7.2"

)

exportJars := true
mainClass in Compile := Option("realtimebidding.Boot")

enablePlugins(JavaAppPackaging)
