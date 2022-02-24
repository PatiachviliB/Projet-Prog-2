package goblin

import attack._
import fighter._


object Rush extends Attack {
    override def toString = "Charge"
    override val targetAlignment = FactionAlignment.Hero
    override val attackType : AttackType.EnumVal = AttackType.MeleeAttack
    override val attackDifficulty = 4
    override val damageModifier = 2
}

object Backstab extends Attack {
    override def toString = "Poignarder dans le dos"
    override val targetAlignment = FactionAlignment.Hero
    override val attackType : AttackType.EnumVal = AttackType.MeleeAttack
    override val attackDifficulty = 9
    override val damageModifier = 4
}

object PoisonousDagger extends Attack {
    override def toString = "Dague empoisonnée"
    override val targetAlignment = FactionAlignment.Hero
    override val attackType : AttackType.EnumVal = AttackType.MeleeAttack
    override val attackDifficulty = 2
    override val damageModifier = 1
}

object SurpriseAttack extends Attack {
    override def toString = "Attaque surprise"
    override val targetAlignment = FactionAlignment.Hero
    override val attackType : AttackType.EnumVal = AttackType.MeleeAttack
    override val attackDifficulty = 7
    override val damageModifier = 3
}

class Goblin(id : Int) extends Fighter(id : Int) {
    override val fighterID = id
    override def toString = "Gobelin"

    val faction = FactionAlignment.Monster
    var maxLifePoints = 8
    var lifePoints = maxLifePoints
    var meleeCapacity = 4
    var rangeCapacity = 2
    var strength = 5
    var toughness = 3
    var initiative = 6

    override val visual = getClass.getResourceAsStream("/goblin.png")

    val attacks = Array(Rush, Backstab, PoisonousDagger, SurpriseAttack)
}