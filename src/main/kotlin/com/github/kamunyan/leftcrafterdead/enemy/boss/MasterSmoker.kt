package com.github.kamunyan.leftcrafterdead.enemy.boss

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.campaign.CampaignDifficulty
import com.github.kamunyan.leftcrafterdead.enemy.specials.Smoker
import com.github.kamunyan.leftcrafterdead.util.MetadataUtil
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class MasterSmoker : LCDBoss() {
    override val bossName: String = "Master Smoker"
    override val entityType: EntityType = EntityType.SKELETON
    override val nonHeadShotDamageResistance: Double = 5.0
    override val explosionResistance: Double = 3.0
    override val bossSkillCoolDown: Int = 10
    override val bossSkills: List<BossSkill> = listOf(MultiShot(), AirStrikeShot())

    override fun spawnEnemy(location: Location) {
        super.spawnEnemy(location)
        livingEntity.getTargetEntity(100)
        manager.matchPlayer.forEach {
            it.player.playSound(it.player.location, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
        }
    }

    override fun bossNormalAttackRunnable() {
        val random = Random()
        object : BukkitRunnable() {
            override fun run() {
                if (livingEntity.isDead) {
                    cancel()
                    return
                }
                livingEntity.world.playSound(livingEntity.location, Sound.ITEM_FIRECHARGE_USE, 3f, 1f)
                for (amount in 0..10) {
                    val arrow = livingEntity.launchProjectile(Arrow::class.java)
                    MetadataUtil.setProjectileMetadata(arrow, MetadataUtil.ENEMY_ARROW)
                    arrow.damage = getPower()
                    val yaw = Math.toRadians((-livingEntity.location.yaw - 90.0f).toDouble())
                    val pitch = Math.toRadians((-livingEntity.location.pitch).toDouble())
                    val spread = doubleArrayOf(1.0, 1.0, 1.0)
                    for (i in 0..2) {
                        spread[i] = (random.nextDouble() - random.nextDouble()) * 2.0 * 0.1
                    }
                    val x = cos(pitch) * cos(yaw) + spread[0]
                    val y = sin(pitch) + spread[1]
                    val z = -sin(yaw) * cos(pitch) + spread[2]
                    val dirVel = Vector(x, y, z)
                    arrow.velocity = dirVel.multiply(5)
                }
            }
        }.runTaskTimer(LeftCrafterDead.instance, 0, 30)
    }

    override fun setLivingEntitySettings(livingEntity: LivingEntity) {
        super.setLivingEntitySettings(livingEntity)
        livingEntity.equipment?.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
        livingEntity.equipment?.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
        livingEntity.equipment?.boots = ItemStack(Material.DIAMOND_BOOTS)
        livingEntity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.baseValue = 3.0
    }

    override fun getHealth(): Double {
        val baseHealth = 500.0
        val addHealth = when (manager.campaignDifficulty) {
            CampaignDifficulty.ADVANCED -> 100.0
            CampaignDifficulty.EXPERT -> 150.0
            else -> 20.0
        }
        return baseHealth + (addHealth * manager.numberOfSurvivors())
    }

    override fun getPower(): Double {
        val basePower = 2.0
        val addPower = when (manager.campaignDifficulty) {
            CampaignDifficulty.ADVANCED -> 2.0
            CampaignDifficulty.EXPERT -> 2.5
            else -> 0.0
        }
        return basePower + addPower
    }

    inner class MultiShot : BossSkill() {
        override fun activationSkill(livingEntity: LivingEntity) {
            object : BukkitRunnable() {
                val random = Random()
                var amount = 100
                override fun run() {
                    if (amount <= 0 || livingEntity.isDead) {
                        cancel()
                        return
                    }
                    livingEntity.world.playSound(livingEntity.location, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f, 1f)
                    val arrow = livingEntity.launchProjectile(Arrow::class.java)
                    MetadataUtil.setProjectileMetadata(arrow, MetadataUtil.ENEMY_ARROW)
                    arrow.damage = getPower()
                    val yaw = Math.toRadians((-livingEntity.location.yaw - 90.0f).toDouble())
                    val pitch = Math.toRadians((-livingEntity.location.pitch).toDouble())
                    val spread = doubleArrayOf(1.0, 1.0, 1.0)
                    for (i in 0..2) {
                        spread[i] = (random.nextDouble() - random.nextDouble()) * 2.0 * 0.1
                    }
                    val x = cos(pitch) * cos(yaw) + spread[0]
                    val y = sin(pitch) + spread[1]
                    val z = -sin(yaw) * cos(pitch) + spread[2]
                    val dirVel = Vector(x, y, z)
                    arrow.velocity = dirVel.multiply(5)
                    amount--
                }
            }.runTaskTimer(LeftCrafterDead.instance, 0, 1)
        }
    }

    inner class AirStrikeShot : BossSkill() {
        override fun activationSkill(livingEntity: LivingEntity) {
            val location = livingEntity.location.clone().add(0.0, 2.0, 0.0)
            location.world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5f, 1f)
            location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 3f, 1f)
            object : BukkitRunnable() {
                var count = 10
                var angle = 0.0
                override fun run() {
                    while (angle <= 360) {
                        val vector = Vector(
                            sin(Math.toRadians(angle)) / 50,
                            1.0,
                            cos(Math.toRadians(angle)) / 50
                        ).normalize()
                        val arrow = livingEntity.world.spawnArrow(location, vector, 2f, 12f)
                        MetadataUtil.setProjectileMetadata(arrow, MetadataUtil.EXPLODE_ARROW)
                        arrow.shooter = livingEntity
                        arrow.fireTicks = 100
                        angle += 10.0
                    }
                    count--
                    if (count <= 0 || livingEntity.isDead) {
                        cancel()
                        return
                    }
                }
            }.runTaskTimer(LeftCrafterDead.instance, 0, 5)
        }
    }
}