package com.github.kamunyan.leftcrafterdead.weapons.primary

import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import org.bukkit.ChatColor

abstract class PrimaryWeapon(weaponTitle: String) : LCDWeapon(weaponTitle, WeaponType.Primary) {
    override fun weaponDataList(): List<String> {
        val handle = crackShot.handle
        return listOf(
            weaponDataInfo("威力", DAMAGE),
            weaponDataInfo("弾速", SPEED),
            weaponDataInfo("拡散率", SPREAD),
            weaponDataInfo("弾数", AMOUNT),
            weaponDataInfo("レートLv", FIRE_RATE),
            weaponDataInfo("装弾数", RELOAD_AMOUNT),
            weaponDataInfo("リロード速度", RELOAD_DURATION),
            weaponDataInfo("ヘッドショットボーナス", HEADSHOT_DAMAGE)
        )
    }
}