package com.github.kamunyan.leftcrafterdead.configs

import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.ChatColor
import org.bukkit.Location

object LobbySpawnConfig : Config("spawns", "lobby.yml") {

    override fun loadConfig() {
        if (!yml.contains("lobby")) {
            MatchManager.lobbySpawnLocation = plugin.server.getWorld("world")!!.spawnLocation
            plugin.logger.info("${ChatColor.RED}[$targetFile] There was no Section for Lobby.")
            return
        }

        val section = yml.getConfigurationSection("lobby")!!
        val location = Location(
            section.getString("world")?.let { plugin.server.getWorld(it) },
            section.getDouble("x"),
            section.getDouble("y"),
            section.getDouble("z"),
            section.getDouble("yaw").toFloat(),
            section.getDouble("pitch").toFloat()
        )
        MatchManager.lobbySpawnLocation = location
        location.world.spawnLocation = location
        plugin.logger.info("${ChatColor.AQUA}[$targetFile] Successfully loaded config!")
    }
}