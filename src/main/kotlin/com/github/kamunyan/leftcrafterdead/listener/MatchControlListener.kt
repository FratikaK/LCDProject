package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.event.MatchStartEvent
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MatchControlListener:Listener {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    @EventHandler
    fun onMatchStart(e: MatchStartEvent){
        if (manager.isMatch){
            plugin.logger.info("${ChatColor.RED}[LCD]すでにマッチを開始しています")
            return
        }else {
            manager.isMatch = true
        }
    }
}