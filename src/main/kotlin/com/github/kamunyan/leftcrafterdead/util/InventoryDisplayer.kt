package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.perk.PerkType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.Inventory

object InventoryDisplayer {
    private val util = ItemMetaUtil

    fun selectPerkDisplay(): Inventory {
        val inventory = Bukkit.createInventory(null, 9, "${ChatColor.DARK_PURPLE}Select Perk")

        val gunslinger = util.generateMetaItem(
            Material.CROSSBOW,
            "${ChatColor.AQUA}Gunslinger",
            100,
            listOf("銃の射撃に特化したPerk", "敵性Mobに追加ダメージを与えることが出来る")
        )
        val medic = util.generateMetaItem(
            Material.REDSTONE,
            "${ChatColor.AQUA}Medic",
            100,
            listOf("チームを治癒で支えるPerk","自身、味方の体力を回復させる武器、アイテムを所持する")
        )

        inventory.setItem(0,gunslinger)
        inventory.setItem(2,medic)

        return inventory
    }
}
