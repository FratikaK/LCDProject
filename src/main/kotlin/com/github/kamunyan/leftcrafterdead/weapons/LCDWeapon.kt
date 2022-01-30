package com.github.kamunyan.leftcrafterdead.weapons

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.ChatColor
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class LCDWeapon(val weaponTitle: String, val weaponType: WeaponType) {

    companion object {
        const val DAMAGE = ".Shooting.Projectile_Damage"
        const val SPEED = ".Shooting.Projectile_Speed"
        const val SPREAD = ".Shooting.Bullet_Spread"
        const val AMOUNT = ".Shooting.Projectile_Amount"
        const val FIRE_RATE = ".Fully_Automatic.Fire_Rate"
        const val RELOAD_AMOUNT = ".Reload.Reload_Amount"
        const val RELOAD_DURATION = ".Reload.Reload_Duration"
        const val HEADSHOT_DAMAGE = ".Headshot.Bonus_Damage"
    }

    val crackShot = LeftCrafterDead.instance.crackShot

    fun weaponDataInfo(infoName: String, node: String): String {
        return "${ChatColor.YELLOW}${infoName}${ChatColor.WHITE}: " +
                "${ChatColor.RED}${crackShot.handle.getInt(weaponTitle + node)}"
    }


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

    abstract fun getAmmoType(): AmmoCategory

    abstract fun weaponDataList(): List<String>
}