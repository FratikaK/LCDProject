package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent
import com.shampaggon.crackshot.events.WeaponExplodeEvent
import org.bukkit.Sound
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent

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
            if (manager.enemyHashMap.containsKey(livingEntity.uniqueId)) {
                val enemy = manager.enemyHashMap[livingEntity.uniqueId]!!
                lcdPlayer.perk.getGrenade().specialEffects(e.player, livingEntity)
                if (livingEntity is HumanEntity || livingEntity.isDead) {
                    return@forEach
                }
                val distance = (livingEntity.location.distance(location)).toInt()
                var lastDamage = weaponDamage - (distance * distanceDecay) - enemy.explosionResistance
                if (lastDamage <= 0){
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
        if (e.victim is Player) {
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
        if (e.entity.killer is HumanEntity) {
            val player = e.entity.killer as Player
            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 0f)
            player.sendMessage(
                "Killer : ${player.displayName} \n" +
                        "DeathEntity : ${e.entity.name}"
            )
        }

        val uuid = e.entity.uniqueId
        if (manager.enemyHashMap.containsKey(uuid)) {
            manager.enemyHashMap[uuid]?.enemyDeathEffects()
            manager.enemyHashMap.remove(uuid)
        }
    }

    @EventHandler
    fun onPlayerDamage(e: EntityDamageByEntityEvent) {
        val player = e.entity
        if (player !is Player) {
            return
        }
        if (!manager.enemyHashMap.containsKey(e.damager.uniqueId)) {
            return
        }
        val enemy = manager.enemyHashMap[e.damager.uniqueId]!!
        e.damage = enemy.getPower()
        enemy.attackSpecialEffects(player)
    }
}