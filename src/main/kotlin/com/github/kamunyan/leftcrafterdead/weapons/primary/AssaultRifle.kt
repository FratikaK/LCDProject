package com.github.kamunyan.leftcrafterdead.weapons.primary

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class AssaultRifle(weaponTitle: String) :PrimaryWeapon(weaponTitle){

    override fun getGunCategory(): GunCategory {
        return GunCategory.ASSAULT_RIFLE
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
    }
}