package com.github.kamunyan.leftcrafterdead.weapons.grenade

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class Grenade(weaponTitle: String) : LCDWeapon(weaponTitle, WeaponType.Grenade) {
    override fun getGunCategory(): GunCategory {
        return GunCategory.GRENADE
    }

    abstract fun explosionEffects(location: Location)

    fun sendGrenade(player: Player, amount: Int) {
        if (weaponType != WeaponType.Grenade) {
            return
        }
        val grenade = crackShot.generateWeapon(weaponTitle)
        var add = 0
        val item = player.inventory.getItem(2)
        if (item != null) {
            add = item.amount
        }
        grenade.amount = (amount + add)
        player.inventory.setItem(2, grenade)
    }

    override fun weaponDataList(): List<String> {
        return listOf()
    }
}