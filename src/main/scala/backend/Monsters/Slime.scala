package slime

import attack._
import fighter._

class Slime(id : Int) extends Fighter(id : Int) {
    override val fighterID = id
    override def toString = "Slime"

    val faction = FactionAlignment.Monster
    val maxLifePoints = 10
    var lifePoints = maxLifePoints
    val meleeCapacity = 1
    val rangeCapacity = 1
    val strength = 1
    val toughness = 1
    val initiative = 1

    val visual = getClass.getResourceAsStream("/slime.png")

    val attacks = new Array[Attack](4)
    attacks(0) = new SlimyPunch
    attacks(1) = new AcidShot
    attacks(2) = new Wrap
    attacks(3) = new Rush
}