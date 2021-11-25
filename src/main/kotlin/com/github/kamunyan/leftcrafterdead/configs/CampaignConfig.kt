package com.github.kamunyan.leftcrafterdead.configs

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.WorldCreator
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import java.io.File
import java.lang.IllegalArgumentException

object CampaignConfig {
    private val manager = MatchManager
    private val plugin = LeftCrafterDead.instance

    fun loadAllCampaign() {
        plugin.logger.info("${ChatColor.YELLOW}[CampaignConfig]Loading Campaign config...")
        val dir = File("plugins/LeftCrafterDead/campaign")

        dir.parentFile.mkdir()
        dir.mkdir()
        var files = dir.listFiles()
        if (files == null) {
            plugin.saveResource("campaign/Venice.yml", false)
            files = dir.listFiles()
        }

        files?.forEach { file ->
            val campaignTitle = file.name.replace(".yml", "")
            var gameProgressLimit = 0
            val startLocations = ArrayList<Location>()
            var restLocation: Location = manager.lobbySpawnLocation
            val normalEnemyLocations = HashMap<Int, List<Location>>()
            var normalMobType: EntityType = EntityType.ZOMBIE
            val yml = YamlConfiguration.loadConfiguration(file)

            if (yml.contains("start")) {
                yml.getStringList("start").forEach {
                    startLocations.add(locationByString(it, campaignTitle))
                }
                gameProgressLimit = startLocations.size - 1
            }

            if (yml.contains("rest")) {
                restLocation = yml.getString("rest")?.let { locationByString(it, campaignTitle) }!!
            } else {
                plugin.logger.info("[CampaignConfig]${ChatColor.RED}Failed to load restLocation.")
            }

            if (yml.contains("mobs")) {
                for (i in 0..gameProgressLimit) {
                    val locations = ArrayList<Location>()
                    if (yml.contains("mobs.$i")) {
                        yml.getStringList("mobs.$i").forEach {
                            val location = locationByString(it, campaignTitle)
                            locations.add(location)
                        }
                    }
                    normalEnemyLocations[i] = locations
                }
            }

            if (yml.contains("mob-type")) {
                val type = yml.getString("mob-type")?.let { EntityType.valueOf(it) }
                if (type != null) {
                    normalMobType = type
                }
            }
            val campaign = Campaign(
                campaignTitle,
                gameProgressLimit,
                startLocations,
                restLocation,
                normalEnemyLocations,
                normalMobType
            )
            if (manager.campaignList.isEmpty()){
                manager.campaign = campaign
            }
            manager.campaignList.add(campaign)
            plugin.logger.info("[CampaignConfig]${ChatColor.AQUA}Campaign '${campaign.campaignTitle}' has been successfully created!")
            campaign.campaignInformation()
        }
        if (manager.campaignList.isEmpty()) {
            plugin.logger.info("[CampaignConfig]${ChatColor.RED}None of the loaded Campaigns are available.")
        }
    }

    private fun locationByString(locString: String, campaignTitle: String): Location {
        val string = locString.replace(" ", "")
        val args = string.split(",")

        if (args.size != 3 && args.size != 5) {
            throw IllegalArgumentException("[CampaignConfig]The format is different. {x, y, z} or {x, y, z, yaw, pitch}")
        }

        val world = Bukkit.getWorld(campaignTitle) ?: Bukkit.createWorld(WorldCreator(campaignTitle))

        val x = args[0].toDouble()
        val y = args[1].toDouble()
        val z = args[2].toDouble()
        var yaw = 0.0
        var pitch = 0.0
        if (args.size == 5) {
            yaw = args[3].toDouble()
            pitch = args[4].toDouble()
        }
        return Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
    }
}