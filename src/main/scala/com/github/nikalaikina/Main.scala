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

  val system = Ids.Tag[SystemSymbol]("X1-ZN63")

  def program[F[_]: Monad](game: GameClient[F]): F[Unit] = {
    for {
      agent <- game.agent
      res <- game.waypoint(system, agent.headquarters)
      contracts <- game.contracts
      _ = println(":" * 100)
      _ = println(contracts)
//      _ <- game.acceptContract(contracts.head.id)

      shipyards <- game.waypoints(system, Set("SHIPYARD"))
//      waypoint = shipyards.head

      shipyard <- game.shipyard(system, shipyards.head.symbol)
      _ = println(":" * 100)
      _ = println(shipyard)
      //      shipyard <- game.buyShip(shipyard.symbol, "SHIP_LIGHT_HAULER")
//      ships <- game.myShips

      _ = println("^" * 100)
//      _ = println(ships)
      asteroid <- game.waypoints(system, "ENGINEERED_ASTEROID")
//      _ <- game.orbit("VERA_TEST_2-1")
//      _ <- game.navigate("VERA_TEST_2-1", asteroid.head.symbol)
//      _ <- game.dock("VERA_TEST_2-1")
      _ <- game.refuel("VERA_TEST_2-1")
            _ <- game.orbit("VERA_TEST_2-1")
            _ <- game.extract("VERA_TEST_2-1")
//      _ = println(asteroid)
      //      _ = println("*" * 100)
      //      _ = println(shipyard.shipTypes)
      //      res <- game.buyShip(waypoint.symbol, shipyard.shipTypes.head.`type`)

    } yield ()
  }

}
