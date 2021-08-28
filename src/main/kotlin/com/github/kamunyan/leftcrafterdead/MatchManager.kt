package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.player.L4DPlayer
import org.bukkit.Location
import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull

object MatchManager {

    lateinit var lobbySpawnLocation: Location
    val onlineL4DPlayer = HashMap<String, L4DPlayer>()
    val matchPlayer = HashMap<String, L4DPlayer>()

    @Synchronized
    fun getL4DPlayer(@NotNull target: Player): L4DPlayer {
        val uuid = target.uniqueId.toString()
        if (!onlineL4DPlayer.containsKey(uuid)) {
            onlineL4DPlayer[uuid] = L4DPlayer(uuid)
        }
        return onlineL4DPlayer[uuid]!!
    }
}