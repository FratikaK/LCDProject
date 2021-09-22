package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.event.MatchStartEvent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class MatchControlListener : Listener {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    @EventHandler
    fun onMatchStart(e: MatchStartEvent) {
        if (manager.isMatch) {
            plugin.logger.info("${ChatColor.RED}[LCD]すでにマッチを開始しています")
            return
        } else {
            manager.isMatch = true
        }
        manager.startCampaign()
    }

    @EventHandler
    fun onLobbyItemRightInteract(e: PlayerInteractEvent) {
        if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
            return
        }

        if (e.item != null && e.item!!.hasItemMeta()) {
            val item = e.item!!
            when (item.type) {
                Material.DIAMOND -> manager.joinPlayer(e.player)
                else -> return
            }
        }
    }
}