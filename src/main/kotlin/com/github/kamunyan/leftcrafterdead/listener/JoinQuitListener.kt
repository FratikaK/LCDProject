package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.L4DPlayer
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinQuitListener : Listener {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage = "${ChatColor.AQUA}${e.player.displayName}がサーバーに参加しました"

        val uuid = e.player.uniqueId.toString()
        if (!manager.onlineL4DPlayer.containsKey(uuid)) {
            manager.onlineL4DPlayer[uuid] = L4DPlayer(uuid)
        }
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.quitMessage = "${ChatColor.AQUA}${e.player.displayName}がログアウトしました"
    }
}