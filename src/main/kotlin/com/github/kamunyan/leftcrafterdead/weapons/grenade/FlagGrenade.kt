package com.github.kamunyan.leftcrafterdead.weapons.grenade

import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class FlagGrenade :Grenade("Flag Grenade") {
    override fun explosionEffects(location: Location) {
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
    }
}