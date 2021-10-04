package com.github.kamunyan.leftcrafterdead.weapons.grenade

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class HealGrenade : Grenade("Heal Grenade") {
    override fun loadWeaponCapabilities() {
    }

    override fun specialEffects(entity: Entity) {
        if (entity !is Player) {
            return
        }
        object : BukkitRunnable() {
            var healHealth = 18.0
            override fun run() {
                if (entity.health == entity.healthScale) {
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
    }
}