package com.github.nikalaikina

import cats.effect.{ExitCode, IO, IOApp}
import io.circe.Json
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.headers.Authorization
import org.http4s.implicits.uri
import org.http4s._
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{Logger, LoggerFactory}

object Main extends IOApp {

  private implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  val Token = Option(System.getenv().get("TOKEN"))
    .getOrElse(throw new Exception("No TOKEN"))

  override def run(
      args: List[String]
  ): IO[ExitCode] = {
    BlazeClientBuilder[IO].resource
      .map(new HttpClient[IO](_, Token))
      .map(new GameClient[IO](_))
      .use { client => client.printHello }
      .map(println(_))
      .as(ExitCode.Success)
  }

}
