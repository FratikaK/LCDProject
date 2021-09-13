package com.github.kamunyan.leftcrafterdead.weapons.secondary

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import org.bukkit.entity.Entity

class HandGun(weaponTitle: String, weaponType: WeaponType) :SecondaryWeapon(weaponTitle, weaponType) {
    override fun getGunCategory(): GunCategory {
        return GunCategory.HANDGUN
    }

    override fun loadWeaponCapabilities() {
    }

    override fun specialEffects(entity: Entity) {
    }
}