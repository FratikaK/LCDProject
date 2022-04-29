package com.github.kamunyan.leftcrafterdead.enemy

import com.github.kamunyan.leftcrafterdead.campaign.CampaignDifficulty
import org.bukkit.entity.Ageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

object NormalEnemy : LCDEnemy() {
    override val metadataKey: String = NORMAL_ENEMY_KEY
    override val entityType: EntityType = manager.campaign.normalMobType
    override val enemyType: EnemyType = EnemyType.NORMAL
    override val nonHeadShotDamageResistance: Double = 1.5
    override val explosionResistance: Double= 0.0
    override val money: Int = 10

    override fun getHealth(): Double {
        val defaultHealth = 15.0
        val addHealth = 6 * manager.numberOfSurvivors()
        return defaultHealth + addHealth
    }

    override fun getPower(): Double {
        val defaultPower = 2.0
        val addPower = when (manager.campaignDifficulty) {
            CampaignDifficulty.ADVANCED -> 1.5
            CampaignDifficulty.EXPERT -> 3.0
            else -> 0.0
        }
        return defaultPower + addPower
    }

    override fun setLivingEntitySettings(livingEntity: LivingEntity) {
        super.setLivingEntitySettings(livingEntity)
        if (livingEntity is Ageable){
            livingEntity.setAdult()
        }
        livingEntity.getTargetEntity(30)
    }
}