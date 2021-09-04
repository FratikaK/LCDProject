package com.github.kamunyan.leftcrafterdead.weapons

import org.bukkit.entity.Player

class LCDWeapon(
    override val weaponTitle: String,
    override val weaponType: WeaponType
) : Weapon() {

    fun sendWeapon(player: Player) {
        if (weaponType == WeaponType.Primary) {
            player.inventory.setItem(0, getWeaponItemStack())
        } else if (weaponType == WeaponType.Secondary) {
            player.inventory.setItem(1, getWeaponItemStack())
        }
    }
}