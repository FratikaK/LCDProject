package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
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
        if (!manager.onlineLCDPlayer.containsKey(uuid)) {
            manager.onlineLCDPlayer[uuid] = LCDPlayer(uuid)
            plugin.logger.info("${ChatColor.AQUA}${e.player.displayName}'s LCDPlayer created.")
        }

        e.player.teleport(manager.lobbySpawnLocation)
        manager.getLCDPlayer(e.player).setLobbyItem()
        e.player.gameMode = manager.getLCDPlayer(e.player).gameMode
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        if (manager.isMatchPlayer(e.player.uniqueId)) {
            manager.leavePlayer(e.player)
        }
        manager.getLCDPlayer(e.player).updatePlayerData()
        manager.onlineLCDPlayer.remove(e.player.uniqueId.toString())
        e.quitMessage = "${ChatColor.AQUA}${e.player.displayName}がログアウトしました"
    }
}