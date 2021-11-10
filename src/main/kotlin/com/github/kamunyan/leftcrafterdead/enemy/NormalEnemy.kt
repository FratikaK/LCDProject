package com.github.kamunyan.leftcrafterdead.enemy

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import java.util.*

class NormalEnemy : LCDEnemy() {
    override lateinit var uuid: UUID
    override val metadataKey: String = NORMAL_ENEMY_KEY
    override val entityType: EntityType = manager.campaign.normalMobType
    override val enemyType: EnemyType = EnemyType.NORMAL
    override val nonHeadShotDamageResistance: Double = 1.5
    override val explosionResistance: Double = 0.0

    override fun getHealth(): Double {
        val defaultHealth = 20.0
        val addHealth = (defaultHealth / 0.6) * manager.numberOfSurvivors()
        return defaultHealth + addHealth
    }

    override fun getPower(): Double {
        val defaultPower = 4.0
        val addPower = when (manager.campaign.determiningDifficulty()) {
            Campaign.Difficulty.ADVANCED -> 1.5
            Campaign.Difficulty.EXPERT -> 3.0
            else -> 0.0
        }
        return defaultPower + addPower
    }
}