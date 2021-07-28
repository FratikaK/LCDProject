package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.player.L4DPlayer
import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull

object MatchManager {

    val onlineL4DPlayer = HashMap<String, L4DPlayer>()
    val matchPlayer = mutableListOf<L4DPlayer>()

    @NotNull
    @Synchronized
    fun getL4DPlayer(@NotNull target: Player): L4DPlayer {
        val uuid = target.uniqueId.toString()
        if (!onlineL4DPlayer.containsKey(uuid)) {
            onlineL4DPlayer[uuid] = L4DPlayer(uuid)
        }
        return onlineL4DPlayer[uuid]!!
    }
}