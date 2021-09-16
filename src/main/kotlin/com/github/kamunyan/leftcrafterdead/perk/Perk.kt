package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import org.bukkit.ChatColor
import org.bukkit.Material

abstract class Perk(var level: Int, val perkType: PerkType) {
    abstract fun perkSymbolMaterial(): Material
    abstract fun getGrenade(): Grenade
    abstract fun firstPrimaryWeapon(): PrimaryWeapon

    fun setFirstWeapon(lcdPlayer: LCDPlayer) {
        lcdPlayer.primary = firstPrimaryWeapon()
        lcdPlayer.primary.sendWeapon(lcdPlayer.player)
    }

    fun setSymbolItem(lcdPlayer: LCDPlayer) {
        val inventory = lcdPlayer.player.inventory
        if (inventory.getItem(8) == null) {

        }
        val material = perkSymbolMaterial()
        inventory.setItem(8,ItemMetaUtil.generateMetaItem(material,"${ChatColor.AQUA}${perkType.perkName}"))
    }
}