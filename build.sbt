organization := "com.example"
name := "apipractice"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.2"

val Http4sVersion = "0.15.11a"

libraryDependencies ++= Seq(
  "org.http4s"     %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"     %% "http4s-circe"        % Http4sVersion,
  "org.http4s"     %% "http4s-dsl"          % Http4sVersion,
  "ch.qos.logback" %  "logback-classic"     % "1.2.1",
  // Optional for auto-derivation of JSON codecs
  "io.circe" %% "circe-generic" % "0.6.1",
  "io.circe" %% "circe-literal" % "0.6.1",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oF")
