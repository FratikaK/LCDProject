package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent
import com.shampaggon.crackshot.events.WeaponExplodeEvent
import org.bukkit.Sound
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
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

        //ダメージを与える処理
        entities.forEach { livingEntity ->
            lcdPlayer.perk.getGrenade().specialEffects(e.player, livingEntity)
            if (livingEntity is HumanEntity || livingEntity.isDead) {
                return@forEach
            }
            val distance = (livingEntity.location.distance(location)).toInt()
            val lastDamage = weaponDamage - (distance * distanceDecay)
            livingEntity.damage(lastDamage.toDouble(), e.player)
        }
    }

    @EventHandler
    fun onWeaponDamage(e: WeaponDamageEntityEvent){

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
    }
}