package com.github.kamunyan.leftcrafterdead.weapons.primary

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import org.bukkit.entity.Entity

class Shotgun(weaponTitle: String) : PrimaryWeapon(weaponTitle) {

    override fun getGunCategory(): GunCategory {
       return GunCategory.SHOTGUN
    }

    override fun loadWeaponCapabilities() {
    }

    override fun specialEffects(entity: Entity) {
    }
}