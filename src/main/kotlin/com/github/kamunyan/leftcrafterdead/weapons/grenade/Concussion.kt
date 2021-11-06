package com.github.kamunyan.leftcrafterdead.weapons.grenade

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Concussion : Grenade("Concussion") {
    override fun explosionEffects(location: Location) {
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
        if (entity.type == EntityType.PLAYER) {
            return
        }
        entity.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 100, 5, false, false))
    }
}