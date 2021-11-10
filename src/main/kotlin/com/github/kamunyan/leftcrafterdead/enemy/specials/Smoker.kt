package com.github.kamunyan.leftcrafterdead.enemy.specials

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.enemy.LCDEnemy
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import java.util.*

class Smoker : LCDEnemy() {
    override lateinit var uuid: UUID
    override val metadataKey: String = SPECIAL_ENEMY_KEY
    override val entityType: EntityType = EntityType.SKELETON
    override val enemyType: EnemyType = EnemyType.SPECIAL
    override val nonHeadShotDamageResistance: Double = 7.0
    override val explosionResistance: Double = 0.0

    override fun getHealth(): Double {
        val defaultHealth = 30
        val addHealth = 15.0 * manager.numberOfSurvivors()
        return defaultHealth + addHealth
    }

    override fun getPower(): Double {
        val defaultPower = 2.0
        val addPower = when (manager.campaign.determiningDifficulty()) {
            Campaign.Difficulty.ADVANCED -> 2.0
            Campaign.Difficulty.EXPERT -> 3.0
            else -> 0.0
        }
        return defaultPower + addPower
    }

    override fun attackSpecialEffects(target: LivingEntity) {
        target.velocity = Vector(-target.velocity.x, 1.0, -target.velocity.z).multiply(10f)
    }
}