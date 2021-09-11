package com.github.kamunyan.leftcrafterdead.weapons

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

    /**
     *
     */
    fun hasReloadTag(player: Player, number: Int): Boolean {
        val item = player.inventory.getItem(number) ?: return false
        if (item.hasItemMeta()) {
            val displayName = item.itemMeta.displayName
            return displayName.contains('ᴿ')
        }
        return false
    }
}