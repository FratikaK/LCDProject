package com.github.kamunyan.leftcrafterdead.weapons.grenade

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable

class HealGrenade : Grenade("Heal Grenade") {
    override fun loadWeaponCapabilities() {
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
        if (entity is HumanEntity) {
            object : BukkitRunnable() {
                var healHealth = 18.0
                override fun run() {
                    if (entity.health == entity.maxHealth) {
                        return
                    }
                    if (healHealth <= 0.0) {
                        this.cancel()
                        return
                    }
                    entity.health += 0.5
                    healHealth -= 0.5
                }
            }.runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 10)
        } else if (entity.type != EntityType.VILLAGER) {
            object : BukkitRunnable() {
                var attackDamage = 10
                override fun run() {
                    if (entity.isDead || attackDamage < 0) {
                        this.cancel()
                        return
                    }
                    entity.damage(1.0, attacker)
                    attackDamage -= 1
                }
            }.runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 10)
        }
    }
}