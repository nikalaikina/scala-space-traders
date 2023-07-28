package com.github.nikalaikina

import cats.effect.kernel.Async
import cats.effect.{Concurrent, IO}
import cats.implicits.*
import com.github.nikalaikina.Contract.{Payment, Procurement}
import com.github.nikalaikina.GameClient.Resp
import com.github.nikalaikina.Ids.*
import io.circe.{Decoder, Json}
import io.circe.syntax.*
import org.http4s.*
import org.http4s.client.Client
import org.http4s.headers.Authorization
import org.http4s.implicits.uri
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class GameClient[F[_]: Concurrent](client: HttpClient[F]) {
  import io.circe.generic.auto.*

  def waypoints(systemSymbol: String): F[List[Waypoint]] = {
    val url = uri"https://api.spacetraders.io/v2/systems" / systemSymbol / "waypoints"
    client
      .run[Resp[List[Waypoint]]](Request(uri = url))
      .map(_.data)
  }

  def getContracts: F[List[Procurement]] = {
    val url = uri"https://api.spacetraders.io/v2/my/contracts"
    client
      .run[Resp[List[Procurement]]](Request(uri = url))
      .map(_.data)
  }

  def acceptContract(id: Tag[Contract]): F[Unit] = {
    val url = uri"https://api.spacetraders.io/v2/my/contracts" / id / "accept"
    client
      .run[Unit](Request(method = Method.POST, uri = url))
  }

}

object GameClient {
  case class Meta(total: Int, page: Int, limit: Int)
  case class Resp[T](data: T, meta: Meta)
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
    loggingClient.expect[T](req.withHeaders(Headers(auth)))
  }

}
