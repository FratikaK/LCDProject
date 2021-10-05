package com.github.kamunyan.leftcrafterdead.weapons.grenade

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class Grenade(weaponTitle: String) : LCDWeapon(weaponTitle, WeaponType.Grenade) {
    override fun getGunCategory(): GunCategory {
        return GunCategory.GRENADE
    }

    fun sendGrenade(player: Player, amount: Int) {
        if (weaponType != WeaponType.Grenade) {
            return
        }
        val grenade = crackShot.generateWeapon(weaponTitle)
        val itemMeta = grenade.itemMeta
        val itemStack = ItemStack(grenade.type, amount)
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(2, itemStack)
    }
}