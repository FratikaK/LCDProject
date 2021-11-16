package com.github.kamunyan.leftcrafterdead.configs.campaign.spawn

import com.github.kamunyan.leftcrafterdead.configs.Config
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location

abstract class SpawnConfig(private val campaignName:String) :Config("spawns","${campaignName}.yml"){
    override fun loadConfig() {
        val section = yml.getConfigurationSection(campaignName)

        if ((section == null) || !section.contains("start") || !section.contains("rest")) {
            plugin.logger.info("${ChatColor.RED}[$targetFile] There was no Section for start or rest.")
            return
        }
        plugin.logger.info("${ChatColor.AQUA}[$targetFile] Successfully loaded config!")
    }

    fun loadCampaignConfig() {
        val section = yml.getConfigurationSection("${campaignName}.mobs") ?: return
        var parse = manager.gameProgress.toString()
        if (manager.isBossParse) {
            parse = "boss"
        }
        for (map in section.getMapList(parse)) {
            val location = Location(
                Bukkit.getWorld(campaignName),
                map["x"] as Double,
                map["y"] as Double,
                map["z"] as Double
            )
            manager.mobSpawnLocationList.add(location)
        }
        if (Bukkit.getWorld("Venice") != null) {
            manager.world = Bukkit.getWorld(campaignName)!!
        }
    }
}