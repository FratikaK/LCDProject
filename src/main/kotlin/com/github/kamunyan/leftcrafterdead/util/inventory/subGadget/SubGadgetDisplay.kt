package com.github.kamunyan.leftcrafterdead.util.inventory.subGadget

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.subgadget.SubGadgetType
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.inventory.DisplayType
import com.github.kamunyan.leftcrafterdead.util.inventory.InventoryDisplay
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object SubGadgetDisplay : InventoryDisplay() {
    override val displayType: DisplayType
        get() = DisplayType.SELECT_FIRST_SUB_GADGET

    private val display = Bukkit.createInventory(null, 9, Component.text("サブガジェット選択"))

    override fun buildDisplay(player: Player): Inventory {
        return display
    }

    override fun buildHandlers(): Map<Int, (event: InventoryClickEvent) -> Unit> {
        indexHandlers[0] = fun(e: InventoryClickEvent) {
            switchSelectFirstSubGadgetDisplay(e.whoClicked as Player,SubGadgetType.HEAL_POTION)
        }
        indexHandlers[1] = fun(e: InventoryClickEvent){
            switchSelectFirstSubGadgetDisplay(e.whoClicked as Player,SubGadgetType.TRIP_MINE)
        }
        indexHandlers[2] = fun(e: InventoryClickEvent){
            switchSelectFirstSubGadgetDisplay(e.whoClicked as Player,SubGadgetType.SENTRY_GUN)
        }
        indexHandlers[3] = fun(e: InventoryClickEvent){
            switchSelectFirstSubGadgetDisplay(e.whoClicked as Player,SubGadgetType.PAIN_KILLER)
        }
        indexHandlers[8] = fun(e: InventoryClickEvent) {
            switchDisplay(e.whoClicked as Player, null)
        }
        return indexHandlers
    }

    private fun switchSelectFirstSubGadgetDisplay(player: Player, type: SubGadgetType) {
        FirstSubGadgetSelectSlot.viewPlayer[MatchManager.getLCDPlayer(player)] = type
        switchDisplay(player, DisplayType.SELECT_FIRST_SUB_GADGET_SLOT_SELECT)
    }

    init {
        var index = 0
        SubGadgetType.values().forEach {
            val item = ItemMetaUtil.generateMetaItem(it.material, "${ChatColor.GREEN}${it.itemName}", 1, it.lore)
            display.setItem(index, item)
            index++
            if (index >= 8) {
                return@forEach
            }
        }
        display.setItem(8, backItem)
    }
}