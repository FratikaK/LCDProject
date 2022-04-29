package com.github.kamunyan.leftcrafterdead.weapons.grenade

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Concussion : Grenade("Concussion") {
    override val grenadeType: GrenadeType
        get() = GrenadeType.CONCUSSION
    override fun explosionEffects(location: Location) {
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
        if (entity.type == EntityType.PLAYER) {
            return
        }
        entity.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 200, 5, false, false))
    }
}