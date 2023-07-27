package com.github.nikalaikina

import cats.effect.Concurrent
import cats.implicits.*
import io.circe.Json
import org.http4s.implicits.uri
import org.http4s.*
import org.http4s.client.Client
import org.http4s.headers.Authorization

class GameClient[F[_]: Concurrent](client: HttpClient[F]) {

  def printHello: F[Json] = {
    client.get(uri"https://api.spacetraders.io/v2/my/contracts")
  }

}

object GameClient {
  case class Meta(total: Int, page: Int, limit: Int)
  case class Resp[T](data: T, meta: Meta)
}

class HttpClient[F[_]: Concurrent](client: Client[F], token: String) {
  import org.http4s.circe.jsonDecoder
  
  def get(uri: Uri): F[Json] = {
    val req = Request(
      uri = uri,
      headers =
        Headers(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    )
    client.expect[Json](req)
  }
}
