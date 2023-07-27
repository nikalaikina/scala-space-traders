package com.github.nikalaikina

import cats.effect.Concurrent
import cats.implicits.*
import com.github.nikalaikina.Contract.{Payment, Procurement}
import com.github.nikalaikina.GameClient.Resp
import com.github.nikalaikina.Ids._
import io.circe.Json
import org.http4s.implicits.uri
import org.http4s.*
import org.http4s.client.Client
import org.http4s.headers.Authorization
import io.circe.Decoder
import io.circe.syntax.*

class GameClient[F[_]: Concurrent](client: HttpClient[F]) {
  import io.circe.generic.auto._

  def printHello: F[List[Procurement]] = {
    client.get[Resp[List[Procurement]]](uri"https://api.spacetraders.io/v2/my/contracts")
      .map(_.data)
  }

}

object GameClient {
  case class Meta(total: Int, page: Int, limit: Int)
  case class Resp[T](data: T, meta: Meta)
}

class HttpClient[F[_]: Concurrent](client: Client[F], token: String) {

  def get[T: Decoder](uri: Uri): F[T] = {
    import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder
    val req = Request(
      uri = uri,
      headers =
        Headers(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    )
    client.expect[T](req)
  }
}
