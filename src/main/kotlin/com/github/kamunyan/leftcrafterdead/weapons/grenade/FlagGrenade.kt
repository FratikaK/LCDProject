package com.github.kamunyan.leftcrafterdead.weapons.grenade

import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

object FlagGrenade :Grenade("Flag Grenade") {
    override val grenadeType: GrenadeType
        get() = GrenadeType.FLAG_GRENADE
    override fun explosionEffects(location: Location) {
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
    }
}