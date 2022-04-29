package com.github.kamunyan.leftcrafterdead.util.inventory.trader

import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.inventory.DisplayType
import com.github.kamunyan.leftcrafterdead.util.inventory.InventoryDisplay
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object TraderMenu: InventoryDisplay() {
    override val displayType: DisplayType = DisplayType.TRADER_MENU

    private val display = Bukkit.createInventory(null,9, Component.text("Traderƒƒjƒ…["))
    override fun buildDisplay(player: Player): Inventory {
        return display
    }

    override fun buildHandlers(): Map<Int, (InventoryClickEvent) -> Unit> {
        indexHandlers[0] = fun(event: InventoryClickEvent){
            switchDisplay(event.whoClicked as Player,DisplayType.TRADER_ENHANCEMENT)
        }
        indexHandlers[1] = fun (event: InventoryClickEvent){
            switchDisplay(event.whoClicked as Player,DisplayType.TRADER_PRIMARY_WEAPON)
        }
        return indexHandlers
    }

    init {
        val util = ItemMetaUtil
        val itemArray = arrayOf(
            util.generateMetaItem(Material.ENCHANTED_BOOK,"${ChatColor.GOLD}‘•”õ‹­‰»"),
            util.generateMetaItem(Material.NETHERITE_AXE,"${ChatColor.AQUA}•Šíw“ü"),
        )
        display.addItem(*itemArray)
    }
}