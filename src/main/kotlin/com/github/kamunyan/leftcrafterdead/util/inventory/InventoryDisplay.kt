package com.github.kamunyan.leftcrafterdead.util.inventory

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

abstract class InventoryDisplay {
    companion object {
        fun switchDisplay(player: Player, type: DisplayType?) {
            val lcdPlayer = MatchManager.getLCDPlayer(player)
            if (type == null) {
                lcdPlayer.displayView = type
                player.closeInventory()
                return
            }
            lcdPlayer.displayView = type
            player.openInventory(type.instance.buildDisplay(lcdPlayer.player))
            player.playSound(player.location, Sound.BLOCK_GLASS_BREAK, 1.0f, 2.0f)
        }
    }

    protected val indexHandlers: HashMap<Int, (event: InventoryClickEvent) -> Unit> = HashMap()
    protected val backItem = ItemMetaUtil.generateMetaItem(Material.OAK_DOOR, "–ß‚é")
    abstract val displayType: DisplayType

    abstract fun buildDisplay(player: Player): Inventory
    abstract fun buildHandlers(): Map<Int, (event: InventoryClickEvent) -> Unit>
}