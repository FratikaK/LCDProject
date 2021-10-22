package com.github.kamunyan.leftcrafterdead.weapons

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.entity.LivingEntity
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

    fun getWeaponItemStack(): ItemStack? {
        return crackShot.generateWeapon(weaponTitle)
    }

    abstract fun getGunCategory(): GunCategory

    abstract fun specialEffects(attacker: Player, entity: LivingEntity)
}