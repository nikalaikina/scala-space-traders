package com.github.nikalaikina

import io.circe.Decoder

import java.time.Instant

object Ids {
  import io.circe.generic.semiauto._

  opaque type Tag[+A] = String

  type TradeSymbol
  type Location

  given typeClass[TC[_], A](using tc: TC[String]): TC[Tag[A]] = tc

}

sealed trait Contract

object Contract {
  import Ids._

  case class Payment(onAccepted: Long, onFulfilled: Long)

  case class Terms(deadline: Instant, payment: Payment, deliver: List[Deliver])

  case class Deliver(
      tradeSymbol: Tag[TradeSymbol],
      destinationSymbol: Tag[Location],
      unitsRequired: Long,
      unitsFulfilled: Long
  )

  case class Procurement(
      id: Tag[Contract],
      factionSymbol: String,
      terms: Terms,
      accepted: Boolean,
      fulfilled: Boolean,
      expiration: Instant,
      deadlineToAccept: Instant
  ) extends Contract

}
