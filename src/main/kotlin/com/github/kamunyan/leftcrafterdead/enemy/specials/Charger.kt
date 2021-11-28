package com.github.kamunyan.leftcrafterdead.enemy.specials

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.campaign.CampaignDifficulty
import com.github.kamunyan.leftcrafterdead.enemy.LCDEnemy
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

object Charger : LCDSpecialEnemy() {
    override val entityType: EntityType = EntityType.RAVAGER
    override val nonHeadShotDamageResistance: Double = 3.0
    override val explosionResistance: Double = 5.0
    override val money: Int = 200

    override fun specialEnemyRunnable(livingEntity: LivingEntity) {}

    override fun setLivingEntitySettings(livingEntity: LivingEntity) {
        super.setLivingEntitySettings(livingEntity)
        livingEntity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)!!.baseValue = 30.0
    }

    override fun getHealth(): Double {
        val defaultHealth = 50.0
        val addHealth = 20.0 * manager.numberOfSurvivors()
        return defaultHealth + addHealth
    }

    override fun getPower(): Double {
        val defaultPower = 8.0
        val addPower = when (manager.campaignDifficulty) {
            CampaignDifficulty.ADVANCED -> 2.0
            CampaignDifficulty.EXPERT -> 4.0
            else -> 0.0
        }
        return defaultPower + addPower
    }

    override fun attackSpecialEffects(attacker: LivingEntity, target: LivingEntity) {
        attacker.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 100, 5, false, false))
        target.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 100, 5, false, false))
    }

}