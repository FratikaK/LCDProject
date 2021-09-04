package com.github.kamunyan.leftcrafterdead.weapons

import org.bukkit.entity.Player

object WeaponUtil {

    //武器名からWeaponTypeを特定する
    fun getWeaponType(weaponTitle: String, player: Player): WeaponType {
        if (player.inventory.getItem(0)?.itemMeta?.displayName?.contains(weaponTitle) == true) {
            return WeaponType.Primary
        } else if (player.inventory.getItem(1)?.itemMeta?.displayName?.contains(weaponTitle) == true) {
            return WeaponType.Secondary
        }
        return WeaponType.UNKNOWN
    }

    //WeaponTypeから武器スロットのindexを特定する
    fun getWeaponSlot(weaponType: WeaponType):Int{
        return when(weaponType){
            WeaponType.Primary -> {
                0
            }
            WeaponType.Secondary ->{
                1
            }
            else -> {
                -1
            }
        }
    }
}