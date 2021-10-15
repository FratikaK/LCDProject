package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

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
}