package com.github.kamunyan.leftcrafterdead.configs

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import org.bukkit.*
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import java.io.File
import kotlin.IllegalArgumentException

object CampaignConfig {
    private val manager = MatchManager
    private val plugin = LeftCrafterDead.instance

    fun loadAllCampaign() {
        plugin.logger.info("${ChatColor.YELLOW}[CampaignConfig]Loading Campaign config...")
        val dir = File("plugins/LeftCrafterDead/campaign")

        dir.parentFile.mkdir()
        dir.mkdir()
        var files = dir.listFiles()
        if (files.isEmpty()) {
            plugin.saveResource("campaign/Tokyo.yml", false)
            files = dir.listFiles()
        }

        files?.forEach { file ->
            val campaignTitle = file.name.replace(".yml", "")
            var mapName: String?
            var gameProgressLimit = 0
            val startLocations = ArrayList<Location>()
            val endLocations = ArrayList<Location>()
            var restLocation: Location = manager.lobbySpawnLocation
            val supplyLocations = ArrayList<Location>()
            var traderLocation: Location? = null
            val normalEnemyLocations = HashMap<Int, List<Location>>()
            var normalMobType: EntityType = EntityType.ZOMBIE
            val yml = YamlConfiguration.loadConfiguration(file)

            mapName = yml.getString("map-name")
            if (mapName == null) {
                mapName = campaignTitle
            }

            if (yml.contains("start")) {
                yml.getStringList("start").forEach {
                    startLocations.add(locationByString(it, campaignTitle))
                }
                gameProgressLimit = startLocations.size - 1
            }

            if (yml.contains("end")) {
                yml.getStringList("end").forEach {
                    endLocations.add(locationByString(it, campaignTitle))
                }
            }

            if (yml.contains("rest")) {
                restLocation = yml.getString("rest")?.let { locationByString(it, campaignTitle) }!!
            } else {
                plugin.logger.info("[CampaignConfig]${ChatColor.RED}Failed to load restLocation.")
            }

            if (yml.contains("supply")) {
                yml.getStringList("supply").forEach {
                    try {
                        val location = locationByString(it, campaignTitle)
                        supplyLocations.add(location)
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    }
                }
            }

            if (yml.contains("trader")) {
                try {
                    val location = locationByString(yml.getString("trader")!!, campaignTitle)
                    traderLocation = location
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }
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
                mapName,
                gameProgressLimit,
                startLocations,
                endLocations,
                restLocation,
                supplyLocations,
                traderLocation,
                normalEnemyLocations,
                normalMobType
            )
            if (manager.campaignList.isEmpty()) {
                manager.campaign = campaign
            }
            manager.campaignList.add(campaign)
            plugin.logger.info("[CampaignConfig]${ChatColor.AQUA}Campaign '${campaign.campaignTitle}' has been successfully created!")
            campaign.campaignInformation()

            //load chunks
            startLocations.forEach {
                if (!it.isChunkLoaded) {
                    it.chunk.isForceLoaded = true
                    it.chunk.load()
                }
            }
            if (!restLocation.isChunkLoaded) {
                restLocation.chunk.isForceLoaded = true
                restLocation.chunk.load()
            }
            supplyLocations.forEach {
                if (!it.isChunkLoaded) {
                    it.chunk.isForceLoaded = true
                    it.chunk.load()
                }
            }
            val world = Bukkit.getWorld(mapName)!!
            world.time = 20000L
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            world.setGameRule(GameRule.DO_FIRE_TICK, false)
            world.setGameRule(GameRule.MOB_GRIEFING, false)
            world.setGameRule(GameRule.KEEP_INVENTORY, false)
            world.setGameRule(GameRule.DO_MOB_LOOT, false)
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
            world.setGameRule(GameRule.FALL_DAMAGE, false)
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false)
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
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