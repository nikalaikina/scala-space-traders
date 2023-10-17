package com.github.nikalaikina

import com.github.nikalaikina.Ids._

case class Shipyard(
    symbol: String,
    shipTypes: Seq[ShipTypes],
    transactions: Seq[Transaction],
    ships: Seq[Ship],
    modificationsFee: Int
)

case class Transaction(
    waypointSymbol: Tag[WaypointSymbol],
    shipSymbol: String,
    price: Int,
    agentSymbol: String,
    timestamp: String
)

case class ShipTypes(
    `type`: String
)

case class Ship(
    symbol: Option[String],
    shipSymbol: Option[String],
    registration: Option[Registration],
    nav: Option[Nav],
    crew: Crew,
    frame: Frame,
    reactor: Reactor,
    engine: Engine,
    cooldown: Option[Cooldown],
    modules: Seq[Module],
    mounts: Seq[Mounts],
    cargo: Option[Cargo],
    fuel: Option[Fuel]
)

case class Cargo(
    capacity: Int,
    units: Int,
    inventory: Seq[Inventory]
)

case class Consumed(
    amount: Int,
    timestamp: String
)

case class Cooldown(
    shipSymbol: String,
    totalSeconds: Int,
    remainingSeconds: Int,
    expiration: String
)

case class Crew(
    required: Int,
    capacity: Int,
    current: Option[Int],
    rotation: Option[String],
    morale: Option[Int],
    wages: Option[Int]
)

case class Engine(
    symbol: String,
    name: String,
    description: String,
    condition: Option[Int],
    speed: Int,
    requirements: Requirements
)

case class Frame(
    symbol: String,
    name: String,
    description: String,
    condition: Option[Int],
    moduleSlots: Int,
    mountingPoints: Int,
    fuelCapacity: Int,
    requirements: Requirements
)

case class Fuel(
    current: Int,
    capacity: Int,
    consumed: Consumed
)

case class Inventory(
    symbol: String,
    name: String,
    description: String,
    units: Int
)

case class Module(
    symbol: String,
    capacity: Option[Int],
    range: Option[Int],
    name: String,
    description: String,
    requirements: Requirements
)

case class Requirements(
    power: Option[Int],
    crew: Option[Int],
    slots: Option[Int]
)

case class Mounts(
    symbol: String,
    name: String,
    description: String,
    strength: Option[Int],
    deposits: Option[Seq[String]],
    requirements: Requirements
)

case class Nav(
    systemSymbol: String,
    waypointSymbol: String,
    route: Route,
    status: String,
    flightMode: String
)

case class Reactor(
    symbol: String,
    name: String,
    description: String,
    condition: Option[Int],
    powerOutput: Int,
    requirements: Requirements
)

case class Registration(
    name: String,
    factionSymbol: String,
    role: String
)

case class Route(
    destination: Destination,
    departure: Destination,
    origin: Destination,
    departureTime: String,
    arrival: String
)

case class Destination(
    symbol: String,
    `type`: String,
    systemSymbol: String,
    x: Int,
    y: Int
)

case class BoughtShip(
    agent: Agent,
    ship: Ship,
    transaction: Transaction
)

case class Agent(
    accountId: String,
    symbol: String,
    headquarters: String,
    credits: Int,
    startingFaction: String,
    shipCount: Int
)

case class BuyShip(
    shipType: String,
    waypointSymbol: Tag[WaypointSymbol]
)
