package com.github.kamunyan.leftcrafterdead.weapons

import org.bukkit.Material
import org.bukkit.entity.Player

object WeaponUtil {

    /**
     * 武器名から武器スロットタイプを特定する
     * @param weaponTitle 武器の名前
     * @param player
     */
    fun getWeaponType(weaponTitle: String, player: Player): WeaponType {
        if (player.inventory.getItem(0)?.itemMeta?.displayName?.contains(weaponTitle) == true) {
            return WeaponType.Primary
        } else if (player.inventory.getItem(1)?.itemMeta?.displayName?.contains(weaponTitle) == true) {
            return WeaponType.Secondary
        }
        return WeaponType.UNKNOWN
    }

    fun getWeaponType(material: Material, player: Player): WeaponType {
        val inventory = player.inventory
        if (inventory.getItem(0) != null && inventory.getItem(0)!!.type == material) {
            return WeaponType.Primary
        } else if (inventory.getItem(1) != null && inventory.getItem(1)!!.type == material) {
            return WeaponType.Secondary
        } else if (inventory.getItem(2) != null && inventory.getItem(2)!!.type == material) {
            return WeaponType.Grenade
        }
        return WeaponType.UNKNOWN
    }

    /**
     * プレイヤーの武器にリロードタグがあるか判定する
     * @param player
     * @param number 武器スロットの番号
     */
    fun hasReloadTag(player: Player, number: Int): Boolean {
        val item = player.inventory.getItem(number) ?: return false
        if (item.hasItemMeta()) {
            val displayName = item.itemMeta.displayName
            return displayName.contains('ᴿ')
        }
        return false
    }

    fun getGunCategory(weaponTitle: String): GunCategory {
        if (GunCategory.ASSAULT_RIFLE.getWeaponList().contains(weaponTitle)) {
            return GunCategory.ASSAULT_RIFLE
        } else if (GunCategory.SUB_MACHINE_GUN.getWeaponList().contains(weaponTitle)) {
            return GunCategory.SUB_MACHINE_GUN
        } else if (GunCategory.SHOTGUN.getWeaponList().contains(weaponTitle)) {
            return GunCategory.SHOTGUN
        } else if (GunCategory.HANDGUN.getWeaponList().contains(weaponTitle)) {
            return GunCategory.HANDGUN
        } else if (GunCategory.GRENADE.getWeaponList().contains(weaponTitle)) {
            return GunCategory.GRENADE
        }
        return GunCategory.UNKNOWN
    }
}