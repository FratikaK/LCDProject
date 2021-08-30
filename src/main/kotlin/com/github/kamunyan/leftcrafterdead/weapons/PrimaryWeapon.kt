package com.github.kamunyan.leftcrafterdead.weapons

import org.bukkit.entity.Player

class PrimaryWeapon(override val weaponTitle: String, override val weaponType: WeaponType) : Weapon() {

    fun sendWeapon(player: Player) {
        player.inventory.setItem(0, getWeaponItemStack())
    }

    init {

    }
}