package com.github.kamunyan.leftcrafterdead.weapons.grenade

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class FlagGrenade :Grenade("Flag Grenade") {

    override fun loadWeaponCapabilities() {
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
    }
}