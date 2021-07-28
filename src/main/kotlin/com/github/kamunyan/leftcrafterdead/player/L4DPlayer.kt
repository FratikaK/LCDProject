package com.github.kamunyan.leftcrafterdead.player

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class L4DPlayer(private val uuid: String) {

    val player: Player = Bukkit.getPlayer(uuid)!!

    var isMatchPlayer = false

}