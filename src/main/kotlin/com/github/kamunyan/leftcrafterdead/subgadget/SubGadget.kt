package com.github.kamunyan.leftcrafterdead.subgadget

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.skill.StatusData
import com.github.kamunyan.leftcrafterdead.util.InventoryDisplayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class SubGadget {
    abstract val subGadgetName: String
    abstract val subGadgetType: SubGadgetType
    abstract val customData: Int
    abstract val limitAmount: Int
    abstract val material: Material
    abstract val lore: List<Component>
    abstract fun rightInteract(player: Player)

    open fun generateItemStack(data: StatusData): ItemStack {
        val item = ItemMetaUtil.generateMetaItem(material, "${ChatColor.AQUA}$subGadgetName", customData)
        item.amount = limitAmount
        return item
    }

    companion object {
        fun getSubGadget(material: Material): SubGadget? {
            var gadget: SubGadget? = null
            selectGadgetDisplayItemMap.forEach { (_, u) ->
                if (u.getInstance().material == material) {
                    gadget = u.getInstance()
                }
            }
            return gadget
        }

        fun nullItem(): ItemStack {
            return ItemMetaUtil.generateMetaItem(Material.BLACK_STAINED_GLASS_PANE, "No Gadget")
        }

        fun setFirstSubGadget(lcdPlayer: LCDPlayer, subGadget: SubGadget?, customData: Int) {
            if (subGadget == null) return
            val sameItemRemove = fun(player: LCDPlayer, gadgetSlot: Int) {
                player.firstSubGadget.forEach { (t, u) ->
                    if (t == gadgetSlot) return@forEach
                    if (u == subGadget.subGadgetType) {
                        lcdPlayer.firstSubGadget[t] = null
                    }
                }
            }
            when (customData) {
                95 -> lcdPlayer.firstSubGadget[5] = subGadget.subGadgetType
                96 -> lcdPlayer.firstSubGadget[6] = subGadget.subGadgetType
                97 -> lcdPlayer.firstSubGadget[7] = subGadget.subGadgetType
                else -> return
            }
            sameItemRemove(lcdPlayer, customData - 90)
            lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.BLOCK_CHEST_OPEN, 2f, 1f)
            lcdPlayer.player.openInventory(InventoryDisplayer.selectFirstSubGadgetDisplay())
        }

        val selectGadgetDisplayItemMap =
            hashMapOf(
                1 to SubGadgetType.HEAL_POTION,
                2 to SubGadgetType.TRIP_MINE,
                3 to SubGadgetType.SENTRY_GUN,
                4 to SubGadgetType.PAIN_KILLER
            )
    }
}