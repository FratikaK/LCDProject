package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import org.bukkit.Location
import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull
import java.util.concurrent.ConcurrentHashMap

object MatchManager {

    lateinit var lobbySpawnLocation: Location
    val onlineL4DPlayer = ConcurrentHashMap<String, LCDPlayer>()
    val matchPlayer = ConcurrentHashMap<String, LCDPlayer>()

    @Synchronized
    fun getL4DPlayer(@NotNull target: Player): LCDPlayer {
        val uuid = target.uniqueId.toString()
        if (!onlineL4DPlayer.containsKey(uuid)) {
            onlineL4DPlayer[uuid] = LCDPlayer(uuid)
        }
        return onlineL4DPlayer[uuid]!!
    }
}