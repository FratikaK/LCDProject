package com.github.kamunyan.leftcrafterdead.util.inventory.subGadget

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.subgadget.SubGadgetType
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.inventory.DisplayType
import com.github.kamunyan.leftcrafterdead.util.inventory.InventoryDisplay
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object FirstSubGadgetSelectSlot : InventoryDisplay() {
    override val displayType: DisplayType
        get() = DisplayType.SELECT_FIRST_SUB_GADGET_SLOT_SELECT

    override fun buildDisplay(player: Player): Inventory {
        val inventory = Bukkit.createInventory(null, 9, Component.text("スロット選択"))
        if (viewPlayer.contains(MatchManager.getLCDPlayer(player))) {
            val lcdPlayer = MatchManager.getLCDPlayer(player)
            inventory.setItem(
                0, ItemMetaUtil.generateMetaItem(
                    viewPlayer[lcdPlayer]!!.material,
                    "${ChatColor.GREEN}${viewPlayer[lcdPlayer]!!.itemName}",
                    1,
                    viewPlayer[lcdPlayer]!!.lore
                )
            )
            lcdPlayer.firstSubGadget.forEach { (t, u) ->
                val item: ItemStack = if (u != null) {
                    ItemMetaUtil.generateMetaItem(u.material, "${ChatColor.GREEN}${u.itemName}", 1, u.lore)
                } else {
                    ItemMetaUtil.generateMetaItem(Material.BLACK_STAINED_GLASS_PANE, "空きスロット")
                }
                inventory.setItem(t, item)
            }
        }
        inventory.setItem(8, backItem)
        return inventory
    }

    override fun buildHandlers(): Map<Int, (event: InventoryClickEvent) -> Unit> {
        val handlers: HashMap<Int, (event: InventoryClickEvent) -> Unit> = HashMap()
        handlers[5] = fun(e: InventoryClickEvent) {
            setFirstSubGadget(e.whoClicked as Player, 5)
        }
        handlers[6] = fun(e: InventoryClickEvent) {
            setFirstSubGadget(e.whoClicked as Player, 6)
        }
        handlers[7] = fun(e: InventoryClickEvent) {
            setFirstSubGadget(e.whoClicked as Player, 7)
        }
        handlers[8] = fun(e: InventoryClickEvent) {
            switchDisplay(e.whoClicked as Player, DisplayType.SELECT_FIRST_SUB_GADGET)
            viewPlayer.remove(MatchManager.getLCDPlayer(e.whoClicked.uniqueId))
        }
        return handlers
    }

    private fun setFirstSubGadget(player: Player, slot: Int) {
        val lcdPlayer = MatchManager.getLCDPlayer(player)
        lcdPlayer.firstSubGadget[slot] = viewPlayer[lcdPlayer]!!
        player.playSound(player.location, Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f)
        player.sendMessage(Component.text(""))
        switchDisplay(player, DisplayType.SELECT_FIRST_SUB_GADGET)
        viewPlayer.remove(lcdPlayer)
    }

    val viewPlayer = HashMap<LCDPlayer, SubGadgetType>()
}