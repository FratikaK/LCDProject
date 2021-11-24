package com.github.kamunyan.leftcrafterdead.enemy.specials

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.campaign.CampaignDifficulty
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*


object Smoker : LCDSpecialEnemy() {
    override val entityType: EntityType = EntityType.SKELETON
    override fun specialEnemyRunnable(livingEntity: LivingEntity) {
        object : BukkitRunnable() {
            val random = Random()
            override fun run() {
                if (livingEntity.isDead || manager.numberOfSurvivors() <= 0) {
                    cancel()
                    return
                }
                val arrow = livingEntity.launchProjectile(Arrow::class.java)
                arrow.damage = getPower()
                arrow.velocity.add(Vector(random.nextInt(5).toFloat(), 0.0f, random.nextInt(5).toFloat()).normalize())
            }
        }.runTaskTimer(LeftCrafterDead.instance, 0, 5)
    }

    override val nonHeadShotDamageResistance: Double = 7.0
    override val explosionResistance: Double = 0.0
    override val money: Int = 50

    override fun getHealth(): Double {
        val defaultHealth = 20
        val addHealth = 15.0 * manager.numberOfSurvivors()
        return defaultHealth + addHealth
    }

    override fun getPower(): Double {
        val defaultPower = 1.5
        val addPower = when (manager.campaignDifficulty) {
            CampaignDifficulty.ADVANCED -> 1.0
            CampaignDifficulty.EXPERT -> 2.0
            else -> 0.0
        }
        return defaultPower + addPower
    }

    override fun setLivingEntitySettings(livingEntity: LivingEntity) {
        super.setLivingEntitySettings(livingEntity)
        livingEntity.equipment?.helmet = ItemStack(Material.DIAMOND_HELMET)
        livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.baseValue = 0.1
        livingEntity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.baseValue = 3.0
    }
}