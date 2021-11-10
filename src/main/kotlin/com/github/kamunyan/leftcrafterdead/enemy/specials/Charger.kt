package com.github.kamunyan.leftcrafterdead.enemy.specials

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.enemy.LCDEnemy
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*

class Charger : LCDEnemy() {
    override lateinit var uuid: UUID
    override val metadataKey: String = SPECIAL_ENEMY_KEY
    override val entityType: EntityType = EntityType.RAVAGER
    override val enemyType: EnemyType = EnemyType.SPECIAL
    override val nonHeadShotDamageResistance: Double = 3.0
    override val explosionResistance: Double = 5.0

    override fun getHealth(): Double {
        val defaultHealth = 50.0
        val addHealth = 20.0 * manager.numberOfSurvivors()
        return defaultHealth + addHealth
    }

    override fun getPower(): Double {
        val defaultPower = 6.0
        val addPower = when (manager.campaign.determiningDifficulty()) {
            Campaign.Difficulty.ADVANCED -> 2.0
            Campaign.Difficulty.EXPERT -> 4.0
            else -> 0.0
        }
        return defaultPower + addPower
    }

    override fun attackSpecialEffects(target: LivingEntity) {
        target.velocity = Vector(target.velocity.x, 2.0, target.velocity.z).multiply(3)
        target.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 100, 5, false, false))
    }

}