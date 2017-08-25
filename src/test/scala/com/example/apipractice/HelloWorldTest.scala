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
    val response = HelloWorld.service.run(
      org.http4s.Request(Method.POST, uri("/hello/bobo")).withBody(
        """{"greeting": "hola"}""").run).run.body.runLast.run
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
