package com.example.apipractice

import org.scalatest._
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import org.http4s._
import org.http4s.circe._
import org.http4s.server._
import org.http4s.dsl._

class HelloWorldSpec extends FlatSpec with Matchers {

  "POST -> /hello/bobo" should "say hello with the greeting specified in the body json" in {
    // org.http4s.Request.withBody returns a task that can be run to generate the request
    val request = org.http4s.Request(Method.POST, uri("/hello/bobo")).withBody(
      """{"greeting": "hola"}""").run

    // run runs the service against the request
    // Note the return type
    val responseTask: scalaz.concurrent.Task[org.http4s.Response] = HelloWorld.service.run(request)

    // the first run gets the org.http4s.Request, which has a body;
    // the body is an org.http4s.EntityBody which is really a scalaz.stream.Process
    // accordingly, to get the value from it, runLast, which itself returns a Task
    // the task needs to be run to obtain an Option of the body as a bytevector
    val response: Option[scodec.bits.ByteVector] = responseTask.run.body.runLast.run

    // Obviously, only use get in test code. decodeUtf8 decodes to an either of a String
    response.get.decodeUtf8.right.get shouldEqual """{"message":"hola, bobo"}"""
  }


  "PUT -> /hello/bobo" should "say hello with the greeting specified in the body json" in {
    val response = HelloWorld.service.run(
      org.http4s.Request(Method.PUT, uri("/hello/bobo")).withBody(
        """{"greeting": {"greeting": "hola"}, "language": "spanish"} """).run).run.body.runLast.run
    response.get.decodeUtf8.right.get shouldEqual """{"message":"hola, bobo"}"""
  }

  "PUT -> /hello/bobo" should "handle error gracefully" in {
    val response = HelloWorld.service.run(
      org.http4s.Request(Method.PUT, uri("/hello/bobo")).withBody(
        """{"ham": "jamon", "greeting": {"hambone": "fail", "Xgreeting": "hola"}, "language": "spanish"} """).run).run.body.runLast.run
    response.get.decodeUtf8.right.get shouldEqual """DecodingFailure at .greeting.greeting: Attempt to decode value on failed cursor"""
  }
}
