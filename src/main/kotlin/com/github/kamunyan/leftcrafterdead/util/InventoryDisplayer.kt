package com.github.kamunyan.leftcrafterdead.util

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
        val fixer = util.generateMetaItem(
            Material.HONEYCOMB,
            "${ChatColor.AQUA}Fixer",
            100,
            listOf("味方をサポートするPerk","敵、味方に対して足止め、補助をするアイテムを所持する")
        )
        val exterminator = util.generateMetaItem(
            Material.NETHERITE_INGOT,
            "${ChatColor.AQUA}Exterminator",
            100,
            listOf("対集団に特化したPerk","敵集団に対して有用なアイテムを所持する")
        )

        inventory.setItem(0,gunslinger)
        inventory.setItem(1,medic)
        inventory.setItem(2,fixer)
        inventory.setItem(3,exterminator)

        return inventory
    }
}
