package com.github.kamunyan.leftcrafterdead.weapons.grenade

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType

abstract class Grenade(weaponTitle: String) : LCDWeapon(weaponTitle, WeaponType.Grenade) {
    override fun getGunCategory(): GunCategory {
        return GunCategory.GRENADE
    }
}