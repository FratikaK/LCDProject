package com.github.kamunyan.leftcrafterdead.weapons.secondary

import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType

abstract class SecondaryWeapon(weaponTitle: String, weaponType: WeaponType) :LCDWeapon(weaponTitle, weaponType) {
    override fun weaponDataList(): List<String> {
        return listOf()
    }
}