package com.github.nikalaikina

import Waypoint.*

import java.time.Instant

case class Waypoint(
    systemSymbol: String,
    symbol: String,
    `type`: String,
    x: Long,
    y: Long,
    // orbitals ??
    traits: List[Trait],
    chart: Chart,
    faction: Faction
)

object Waypoint {

  case class Trait(
      symbol: String,
      name: String,
      description: String
  )

//  object Trait {
//    enum TraitSymbol:
//      case Marketplace, Green, Blue
//    MARKETPLACE
//  }

  case class Chart(submittedBy: String, submittedOn: Instant)
  case class Faction(symbol: String)

}
