package com.example.apipractice

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.literal._

import cats.syntax.show._

import org.http4s._
import org.http4s.circe._
import org.http4s.server._
import org.http4s.dsl._

import scalaz.{\/, -\/, \/-}

object HelloWorld {

  case class Greeting(greeting: String)
  case class ExtendedGreeting(greeting: Greeting, language: String)

  implicit val ExtendedGreetingEncoder: Encoder[ExtendedGreeting] =
    Encoder.instance { hello: ExtendedGreeting =>
      json"""
      {"language": ${hello.language},
       "greeting": {"greeting": ${hello.greeting.greeting }}}"""
    }

  implicit val ExtendedGreetingDecoder: Decoder[ExtendedGreeting] =
    Decoder.instance { hello: HCursor =>
      for {
        language <- hello.downField("language").as[String]
        greeting <- hello.downField("greeting").as[Greeting]
      } yield ExtendedGreeting(greeting, language)
    }

  val service = HttpService {
    case GET -> Root / "hello" / name =>
      Ok(Json.obj("message" -> Json.fromString(s"Hello, ${name}")))

    case request @ POST -> Root / "hello" / name =>
      for {
        // Decode a User request
	greeting <- request.as(jsonOf[Greeting])
        resp <- Ok(Json.obj("message" -> Json.fromString(s"${greeting.greeting}, ${name}")))
      } yield resp

    case request @ PUT -> Root / "hello" / name => {
      request.as(jsonOf[ExtendedGreeting]).attempt.flatMap {
        case -\/(f) => f.getCause match {
          case (d: DecodingFailure) => BadRequest(d.show)
          case _ => BadRequest(f.toString)
        }
        case \/-(greeting) =>  Ok(Json.obj("message" -> Json.fromString(s"${greeting.greeting.greeting}, ${name}")))
      }
    }
  }
}
