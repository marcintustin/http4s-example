# HTTP4S example project
This project demonstrates a minimal HTTP4S project (generated from the g8 template; such business logic as exists is jammed into the service layer).

The purpose of this project is to demonstrating testing HTTP4S projects, handling errors, and custom JSON decoding

This project uses http4s 0.15

# Structure
Server.scala is purely stock from g8 template.

src/main/scala/com/example/apipractice/HelloWorld.scala contains the interesting error handling and JSON decoding logic. In a real application, I would strongly recommend separating out the business logic.

src/test/scala/com/example/apipractice/HelloWorldTest.scala demonstrates invoking the service with a hand-built request, and extracting the response body. The example uses scalatest, but does not rely on any special scalatest features. 
