package com.github.kamunyan.leftcrafterdead.player

import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class L4DPlayer(uuid: String) {
    private val manager = MatchManager

    val player: Player = Bukkit.getPlayer(uuid)!!

    var isMatchPlayer = false

    var isSurvivor = false
}