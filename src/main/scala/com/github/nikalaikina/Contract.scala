package com.github.nikalaikina

import java.time.Instant

sealed trait Contract

object Contract {
  opaque type ContractId = String
  opaque type TradeSymbol = String
  opaque type Location = String

  case class Payment(onAccepted: Long, onFulfilled: Long)

  case class Terms(deadline: Instant, payment: Payment, deliver: List[Deliver])

  case class Deliver(
      tradeSymbol: TradeSymbol,
      destinationSymbol: Location,
      unitsRequired: Long,
      unitsFulfilled: Long
  )

  case class Procurement(
      id: ContractId,
      factionSymbol: String,
      terms: Terms,
      accepted: Boolean,
      fulfilled: Boolean,
      expiration: Instant,
      deadlineToAccept: Instant
  ) extends Contract

}
