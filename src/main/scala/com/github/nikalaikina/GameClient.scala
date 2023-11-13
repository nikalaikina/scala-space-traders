package com.github.nikalaikina

import cats.effect.kernel.Async
import cats.effect.{Concurrent, IO}
import cats.implicits.*
import com.github.nikalaikina.Contract.Payment
import com.github.nikalaikina.GameClient.Resp
import com.github.nikalaikina.Ids.*
import com.github.nikalaikina.Waypoint.*
import io.circe.syntax.*
import io.circe.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.client.Client
import org.http4s.headers.Authorization
import org.http4s.implicits.uri
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class GameClient[F[_]: Concurrent](client: HttpClient[F]) {
  import io.circe.generic.auto.*

  def agent: F[Agent] = {
    val url =
      uri"https://api.spacetraders.io/v2/my/agent"
    client
      .run[Resp[Agent]](Request(uri = url))
      .map(_.data)
  }

  def waypoint(
      systemSymbol: Tag[SystemSymbol],
      waypoint: String
  ): F[Waypoint] = {
    val url =
      uri"https://api.spacetraders.io/v2/systems" / systemSymbol / "waypoints" / waypoint
    client
      .run[Resp[Waypoint]](Request(uri = url))
      .map(_.data)
  }

  def waypoints(systemSymbol: Tag[SystemSymbol], traits: Set[String]): F[List[Waypoint]] = {
    val url =
      uri"https://api.spacetraders.io/v2/systems" / systemSymbol / "waypoints" ++? ("traits" -> traits.toSeq)
    client
      .run[Resp[List[Waypoint]]](Request(uri = url))
      .map(_.data)
  }


  def waypoints(systemSymbol: Tag[SystemSymbol], `type`: String): F[List[Waypoint]] = {
    val url =
      uri"https://api.spacetraders.io/v2/systems" / systemSymbol / "waypoints" +? ("type" -> `type`)
    client
      .run[Resp[List[Waypoint]]](Request(uri = url))
      .map(_.data)
  }

  def contracts: F[List[Contract]] = {
    val url = uri"https://api.spacetraders.io/v2/my/contracts"
    client
      .run[Resp[List[Contract]]](Request(uri = url))
      .map(_.data)
  }

  def shipyard(
      systemSymbol: Tag[SystemSymbol],
      waypointSymbol: Tag[WaypointSymbol]
  ): F[Shipyard] = {
    val url =
      uri"https://api.spacetraders.io/v2/systems" / systemSymbol / "waypoints" / waypointSymbol / "shipyard"
    client
      .run[Resp[Shipyard]](Request(uri = url))
      .map(_.data)
  }

  def buyShip(
      waypointSymbol: Tag[WaypointSymbol],
      shipType: String
  ): F[BoughtShip] = {
    val url = uri"https://api.spacetraders.io/v2/my/ships"

    client
      .run[Resp[BoughtShip]](
        Request(method = Method.POST, uri = url)
          .withEntity(BuyShip(shipType, waypointSymbol))
      )
      .map(_.data)
  }

  def myShips: F[List[Ship]] = {
    val url = uri"https://api.spacetraders.io/v2/my/ships"

    client
      .run[Resp[List[Ship]]](
        Request(method = Method.GET, uri = url)
      )
      .map(_.data)
  }

  def orbit(
      ship: String,
  ): F[NavigationResult] = {
    val url = uri"https://api.spacetraders.io/v2/my/ships" / ship / "orbit"

    client
      .run[Resp[NavigationResult]](
        Request(method = Method.POST, uri = url)
      )
      .map(_.data)
  }

  def extract(
      ship: String,
  ): F[NavigationResult] = {
    val url = uri"https://api.spacetraders.io/v2/my/ships" / ship / "extract"

    client
      .run[Resp[NavigationResult]](
        Request(method = Method.POST, uri = url)
      )
      .map(_.data)
  }

  def dock(
      ship: String,
  ): F[NavigationResult] = {
    val url = uri"https://api.spacetraders.io/v2/my/ships" / ship / "dock"

    client
      .run[Resp[NavigationResult]](
        Request(method = Method.POST, uri = url)
      )
      .map(_.data)
  }

  def refuel(
      ship: String,
  ): F[BoughtFuel] = {
    val url = uri"https://api.spacetraders.io/v2/my/ships" / ship / "refuel"

    client
      .run[Resp[BoughtFuel]](
        Request(method = Method.POST, uri = url)
      )
      .map(_.data)
  }

  def navigate(
      ship: String,
      waypointSymbol: Tag[WaypointSymbol]
  ): F[NavigationResult] = {
    val url = uri"https://api.spacetraders.io/v2/my/ships" / ship / "navigate"

    client
      .run[Resp[NavigationResult]](
        Request(method = Method.POST, uri = url)
          .withEntity(WaypointPayload(waypointSymbol))
      ).map(_.data)
  }

  def acceptContract(id: Tag[Contract]): F[Contract] = {
    val url = uri"https://api.spacetraders.io/v2/my/contracts" / id / "accept"
    client
      .run[Contract](Request(method = Method.POST, uri = url))
  }

}

object GameClient {
  case class Meta(total: Int, page: Int, limit: Int)
  case class Resp[T](data: T, meta: Option[Meta])
}

class HttpClient[F[_]: Async](client: Client[F], token: String) {
  import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder

  private implicit val logger: Logger[F] = Slf4jLogger.getLogger[F]

  val loggingClient = org.http4s.client.middleware.Logger.apply[F](
    logHeaders = false,
    logBody = true,
    logAction = Some(logger.info(_))
  )(client)

  private val auth = Authorization(Credentials.Token(AuthScheme.Bearer, token))

  def run[T: Decoder](req: Request[F]): F[T] = {
    loggingClient.expect[T](req.withHeaders(req.headers.put(auth)))
  }

}
