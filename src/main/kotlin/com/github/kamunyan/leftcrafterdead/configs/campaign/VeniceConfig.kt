package com.github.kamunyan.leftcrafterdead.configs.campaign

import com.github.kamunyan.leftcrafterdead.configs.Config
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration

object VeniceConfig : Config("spawns", "Venice.yml") {
    lateinit var startLocation: Location
    lateinit var restLocation: Location
    override fun loadConfig() {
        val section = yml.getConfigurationSection("Venice")

        if ((section == null) || !section.contains("start") || !section.contains("rest")) {
            plugin.logger.info("${ChatColor.RED}[$targetFile] There was no Section for start or rest.")
            return
        }
        startLocation = section.getConfigurationSection("start")?.let { getLocation(it) }!!
        restLocation = section.getConfigurationSection("rest")?.let { getLocation(it) }!!
        plugin.logger.info("${ChatColor.AQUA}[$targetFile] Successfully loaded config!")
    }
}