package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.util.MetadataUtil
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent
import com.shampaggon.crackshot.events.WeaponExplodeEvent
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent

class DamageControlListener : Listener {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    @EventHandler
    fun onCrackShotExplosion(e: WeaponExplodeEvent) {
        val location = e.location.clone()
        val radius = plugin.crackShot.handle.getInt("${e.weaponTitle}.Explosions.Explosion_Radius")
        val weaponDamage = plugin.crackShot.handle.getInt("${e.weaponTitle}.Shooting.Projectile_Damage")
        val distanceDecay = weaponDamage / radius
        val entities = location.getNearbyLivingEntities(radius.toDouble())
        val lcdPlayer = manager.getLCDPlayer(e.player)

        lcdPlayer.perk.getGrenade().explosionEffects(location)
        //ダメージを与える処理
        entities.forEach { livingEntity ->
            lcdPlayer.perk.getGrenade().specialEffects(e.player, livingEntity)
            if (manager.enemyHashMap.containsKey(livingEntity.uniqueId)) {
                val enemy = manager.enemyHashMap[livingEntity.uniqueId]!!
                if (livingEntity is HumanEntity || livingEntity.isDead) {
                    return@forEach
                }
                val distance = (livingEntity.location.distance(location)).toInt()
                var lastDamage = weaponDamage - (distance * distanceDecay) - enemy.explosionResistance
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
        if (e.victim.type == EntityType.PLAYER) {
            println("キャンセル！")
            e.isCancelled = true
            return
        }
        if (!manager.enemyHashMap.containsKey(e.victim.uniqueId)) {
            return
        }
        val enemy = manager.enemyHashMap[e.victim.uniqueId]!!
        if (!e.isHeadshot) {
            if (e.damage <= enemy.nonHeadShotDamageResistance) {
                e.damage = 0.0
                return
            }
            e.damage = e.damage - enemy.nonHeadShotDamageResistance
        }
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
                lcdPlayer.campaignData.money += enemy.money
                manager.enemyHashMap.remove(uuid)
            }
        }
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