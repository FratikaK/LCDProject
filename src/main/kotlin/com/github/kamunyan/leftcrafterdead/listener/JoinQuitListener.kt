package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinQuitListener :Listener{
    private val plugin = LeftCrafterDead.instance

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent){
        e.joinMessage = "${ChatColor.AQUA}${e.player.displayName}がサーバーに参加しました"
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent){
        e.quitMessage = "${ChatColor.AQUA}${e.player.displayName}がログアウトしました"
    }
}