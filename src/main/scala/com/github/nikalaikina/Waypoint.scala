package com.github.nikalaikina

import com.github.nikalaikina.Waypoint.*
import io.circe.literal.json
import io.circe._

import java.time.Instant
import io.circe.generic.auto.*
import Ids._

case class Waypoint(
    symbol: Tag[WaypointSymbol],
    `type`: String,
    systemSymbol: Tag[SystemSymbol],
    x: Int,
    y: Int,
    orbitals: Seq[Orbitals],
    orbits: Option[String],
    faction: Option[Orbitals],
    traits: Seq[Traits],
    chart: Option[Chart]
)


case class Chart(
    waypointSymbol: Option[Tag[WaypointSymbol]],
    submittedBy: Option[String],
    submittedOn: Option[String]
)

case class Orbitals(
    symbol: String
)

case class Traits(
    symbol: String,
    name: String,
    description: String
)
