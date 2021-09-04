package com.github.kamunyan.leftcrafterdead.weapons

import com.shampaggon.crackshot.CSUtility
import org.bukkit.inventory.ItemStack

abstract class Weapon {
    abstract val weaponTitle: String
    abstract val weaponType: WeaponType
    val crackShot = CSUtility()

    fun getWeaponItemStack(): ItemStack? {
        return crackShot.generateWeapon(weaponTitle)
    }
}