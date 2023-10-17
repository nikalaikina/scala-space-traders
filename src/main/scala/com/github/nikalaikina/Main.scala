package com.github.nikalaikina

import cats.Monad
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*
import io.circe.Json
import org.http4s.*
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.headers.Authorization
import org.http4s.implicits.uri
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{Logger, LoggerFactory}

object Main extends IOApp {
  import Ids._

  private implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  val Token = Option(System.getenv().get("TOKEN"))
    .getOrElse(throw new Exception("No TOKEN"))

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeClientBuilder[IO].resource
      .map(new HttpClient[IO](_, Token))
      .map(new GameClient[IO](_))
      .use(program)
      .as(ExitCode.Success)
  }

  val system = Ids.Tag[SystemSymbol]("X1-CS80")

  def program[F[_]: Monad](game: GameClient[F]): F[Unit] = {
    for {
      contracts <- game.contracts
      res <- game.waypoints(system)
      _ = println(res)
      shipyards = res.filter(_.traits.exists(_.symbol == "SHIPYARD"))
      waypoint = shipyards.head
      _ = println(":" * 100)
      _ = println(shipyards)

      shipyard <- game.shipyard(system, waypoint.symbol)
      _ = println("*" * 100)
      _ = println(shipyard.shipTypes)
      res <- game.buyShip(waypoint.symbol, shipyard.shipTypes.head.`type`)

//      _ <- game.acceptContract(contracts.head.id)
    } yield ()
  }

}
