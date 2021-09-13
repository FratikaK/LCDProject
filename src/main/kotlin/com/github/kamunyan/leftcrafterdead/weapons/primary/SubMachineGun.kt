package com.github.kamunyan.leftcrafterdead.weapons.primary

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import org.bukkit.entity.Entity

class SubMachineGun(weaponTitle: String) :PrimaryWeapon(weaponTitle){

    override fun getGunCategory(): GunCategory {
        return GunCategory.SUB_MACHINE_GUN
    }

    override fun loadWeaponCapabilities() {
    }

    override fun specialEffects(entity: Entity) {
    }
}