package com.github.kamunyan.leftcrafterdead.weapons.grenade

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.scheduler.BukkitRunnable

class HealGrenade : Grenade("Heal Grenade") {
    override fun loadWeaponCapabilities() {
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
        try {
            if (entity.type == EntityType.PLAYER) {
                object : BukkitRunnable() {
                    var healHealth = 18.0
                    override fun run() {
                        if (healHealth <= 0.0 || entity.isDead) {
                            this.cancel()
                            return
                        }

                        if (entity.health == entity.maxHealth) {
                            healHealth -= 0.5
                            return
                        }

                        entity.health += 0.5
                        healHealth -= 0.5
                    }
                }.runTaskTimer(LeftCrafterDead.instance, 0, 10)
            } else if (entity.type != EntityType.VILLAGER && entity.type != EntityType.PLAYER) {
                object : BukkitRunnable() {
                    var attackDamage = crackShot.handle.getInt("${weaponTitle}.Shooting.Projectile_Damage")
                    override fun run() {
                        if (entity.isDead) {
                            Bukkit.getPluginManager().callEvent(EntityDeathEvent(entity, mutableListOf()))
                        }

                        if (entity.isDead || attackDamage < 0) {
                            this.cancel()
                            return
                        }
                        entity.damage(1.0)
                        attackDamage -= 1
                    }
                }.runTaskTimer(LeftCrafterDead.instance, 0, 10)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}