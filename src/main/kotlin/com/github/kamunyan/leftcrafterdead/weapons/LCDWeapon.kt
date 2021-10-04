package com.github.kamunyan.leftcrafterdead.weapons

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class LCDWeapon(val weaponTitle: String, val weaponType: WeaponType) {

    val crackShot = LeftCrafterDead.instance.crackShot

    fun sendWeapon(player: Player) {
        if (weaponType == WeaponType.Primary) {
            player.inventory.setItem(0, getWeaponItemStack())
        } else if (weaponType == WeaponType.Secondary) {
            player.inventory.setItem(1, getWeaponItemStack())
        }
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

    fun getWeaponItemStack(): ItemStack? {
        return crackShot.generateWeapon(weaponTitle)
    }

    abstract fun getGunCategory(): GunCategory

    abstract fun loadWeaponCapabilities()

    abstract fun specialEffects(entity: Entity)
}