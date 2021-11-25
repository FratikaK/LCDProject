package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.subgadget.TripMine
import com.github.kamunyan.leftcrafterdead.util.MetadataUtil
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.WeaponUtil
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent
import com.shampaggon.crackshot.events.WeaponExplodeEvent
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent

class DamageControlListener : Listener {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    @EventHandler
    fun onCrackShotExplosion(e: WeaponExplodeEvent) {
        val location = e.location.clone()
        val lcdPlayer = manager.getLCDPlayer(e.player)
        var radius = plugin.crackShot.handle.getInt("${e.weaponTitle}.Explosions.Explosion_Radius")
        if (e.weaponTitle == "TRIP MINE") {
            radius = ((radius * lcdPlayer.statusData.tripMineRangeMultiplier).toInt())
        }
        val weaponDamage = plugin.crackShot.handle.getInt("${e.weaponTitle}.Shooting.Projectile_Damage")
        val distanceDecay = weaponDamage / radius
        val entities = location.getNearbyLivingEntities(radius.toDouble())

        if (GunCategory.GRENADE.getWeaponList().contains(e.weaponTitle)) {
            lcdPlayer.perk.getGrenade().explosionEffects(location)
        }
        //ダメージを与える処理
        entities.forEach { livingEntity ->
            if (GunCategory.GRENADE.getWeaponList().contains(e.weaponTitle)) {
                lcdPlayer.perk.getGrenade().specialEffects(e.player, livingEntity)
            }
            if (manager.enemyHashMap.containsKey(livingEntity.uniqueId)) {
                val enemy = manager.enemyHashMap[livingEntity.uniqueId]!!
                if (livingEntity is HumanEntity || livingEntity.isDead) {
                    return@forEach
                }
                val distance = (livingEntity.location.distance(location)).toInt()
                if (lcdPlayer.statusData.specialSkillTypes.contains(SpecialSkillType.FIRE_TRAP)) {
                    livingEntity.fireTicks = 200
                }
                var lastDamage = weaponDamage - (distance * distanceDecay) - enemy.explosionResistance
                lastDamage *= lcdPlayer.statusData.explosionDamageMultiplier
                if (lastDamage <= 0) {
                    lastDamage = 0.0
                }
                livingEntity.damage(lastDamage, e.player)
            }
        }
    }

    @EventHandler
    fun onWeaponDamage(e: WeaponDamageEntityEvent) {
        if (GunCategory.GRENADE.getWeaponList().contains(e.weaponTitle)) {
            e.damage = 0.0
            return
        }
        if (e.victim.type == EntityType.PLAYER || e.victim.type == EntityType.SNOWMAN || e.victim.type == EntityType.VILLAGER) {
            e.isCancelled = true
            return
        }
        if (!manager.enemyHashMap.containsKey(e.victim.uniqueId)) {
            return
        }
        val enemy = manager.enemyHashMap[e.victim.uniqueId]!!
        val data = manager.getLCDPlayer(e.player).statusData
        e.damage *= data.weaponDamageMultiplier
        val category = WeaponUtil.getGunCategory(e.weaponTitle)
        if (category == GunCategory.SHOTGUN) {
            e.damage *= data.shotgunDamageMultiplier
        }
        if (data.specialSkillTypes.contains(SpecialSkillType.UNDERDOG)) {
            val entityList = e.player.location.getNearbyLivingEntities(4.0)
            if (entityList.size >= 4) {
                e.damage *= 1.1
                if (category == GunCategory.SHOTGUN && data.specialSkillTypes.contains(SpecialSkillType.CLOSE_BY)) {
                    e.damage *= 1.2
                }
            }
        }

        if (!e.isHeadshot) {
            if (e.damage <= enemy.nonHeadShotDamageResistance) {
                e.damage = 0.0
                return
            }
        }
        if (data.specialSkillTypes.contains(SpecialSkillType.BULLSEYE)) {
            if (Math.random() <= 0.1) {
                e.player.absorptionAmount += 1
                if (e.player.absorptionAmount > data.armorLimit) {
                    e.player.absorptionAmount = data.armorLimit
                }
            }
        }
        e.damage = e.damage - enemy.nonHeadShotDamageResistance
        if (category == GunCategory.ASSAULT_RIFLE || category == GunCategory.SUB_MACHINE_GUN) {
            if (data.specialSkillTypes.contains(SpecialSkillType.GRAZE)) {
                val entities = e.victim.location.getNearbyLivingEntities(5.0)
                entities.remove(e.victim)
                var count = 0
                for (entity in entities) {
                    if (entity.type == EntityType.PLAYER) continue
                    if (count >= 3) break
                    entity.damage(e.damage, e.player)
                    count++
                }
            }
        }
        println("ダメージ量 ${e.damage}")
    }

    @EventHandler
    fun onDeath(e: EntityDeathEvent) {
        val uuid = e.entity.uniqueId
        if (manager.enemyHashMap.containsKey(uuid)) {
            val enemy = manager.enemyHashMap[uuid]!!
            enemy.enemyDeathEffects(e.entity)
            if (e.entity.killer is HumanEntity) {
                val player = e.entity.killer as Player
                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
//                player.sendMessage(
//                    "Killer : ${player.displayName} \n" +
//                            "DeathEntity : ${e.entity.name}"
//                )
                val lcdPlayer = manager.getLCDPlayer(player)
                lcdPlayer.campaignData.kill += 1
                lcdPlayer.campaignData.money += (enemy.money * lcdPlayer.statusData.addMoneyMultiplier).toInt()
                if (lcdPlayer.statusData.specialSkillTypes.contains(SpecialSkillType.FULLY_LOADED)) {
                    if (Math.random() <= 0.05) {
                        lcdPlayer.perk.getGrenade().sendGrenade(lcdPlayer.player, 1)
                        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 24f)
                        player.sendMessage(Component.text("${ChatColor.AQUA}Fully Loadedの効果によりグレネードを回収しました！"))
                    }
                }
                manager.enemyHashMap.remove(uuid)
            }
        }
    }

    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent){
        if (e.entity.type == EntityType.SNOWMAN) e.isCancelled = true
    }

    @EventHandler
    fun onPlayerDamage(e: EntityDamageByEntityEvent) {
        val player = e.entity
        if (player.type != EntityType.PLAYER) {
            return
        }
        if (!manager.enemyHashMap.containsKey(e.damager.uniqueId)) {
            return
        }
        val enemy = manager.enemyHashMap[e.damager.uniqueId]!!
        enemy.attackSpecialEffects(e.damager as LivingEntity, player as LivingEntity)
        e.damage = enemy.getPower()
        println("PlayerDamage ${e.damage}")
    }

    @EventHandler
    fun onPvP(e: EntityDamageByEntityEvent) {
        if (e.entity.type == e.damager.type) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onArrowHit(e: ProjectileHitEvent) {
        if (e.entity.hasMetadata(MetadataUtil.EXPLODE_ARROW)) {
            e.entity.location.createExplosion(2.0f, false, false)
        }
    }
}