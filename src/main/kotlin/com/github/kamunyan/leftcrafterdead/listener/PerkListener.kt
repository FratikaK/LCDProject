package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.subgadget.PainKiller
import com.github.kamunyan.leftcrafterdead.subgadget.SentryGun
import com.github.kamunyan.leftcrafterdead.subgadget.TripMine
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.MetadataUtil
import com.github.kamunyan.leftcrafterdead.util.SupplyMinions
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class PerkListener : Listener {
    private val manager = MatchManager

    /**
     * パークガジェットをインタラクトした時、
     * それぞれの効果を発生させる
     */
    @EventHandler
    fun onPerkGadgetInteract(e: PlayerInteractEvent) {
        if (e.item == null) {
            return
        }
        if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
            val lcdPlayer = manager.getLCDPlayer(e.player)
            if (e.item!!.itemMeta.hasCustomModelData()) {
                if (e.item!!.itemMeta.customModelData == 110) {
                    lcdPlayer.perk.gadgetRightInteract(lcdPlayer)
                } else if (e.item!!.itemMeta.customModelData == 111) {
                    e.player.sendMessage("${ChatColor.RED}クールダウン中です！")
                }
            }
        }
    }

    @EventHandler
    fun onSubGadgetInteract(e: PlayerInteractEvent) {
        if (!ItemMetaUtil.hasItemMetaCustomModelData(e.item)) return
        if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
            val lcdPlayer = manager.getLCDPlayer(e.player)
            val index = e.player.inventory.indexOf(e.player.inventory.itemInMainHand)
            if (lcdPlayer.subGadget.containsKey(index)){
                lcdPlayer.subGadget[index]?.rightInteract(e.player)
            }
        }
    }

    @EventHandler
    fun onSupplyMinionsInteract(e: PlayerInteractEntityEvent){
        if (e.rightClicked.hasMetadata(MetadataUtil.SUPPLY_CART)){
            SupplyMinions.supplies[e.rightClicked.uniqueId]?.retrievedSupply(e.player)
        }
    }
}