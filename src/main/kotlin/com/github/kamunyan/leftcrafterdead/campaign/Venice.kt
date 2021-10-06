package com.github.kamunyan.leftcrafterdead.campaign

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.configs.Config
import com.github.kamunyan.leftcrafterdead.configs.campaign.VeniceConfig
import org.bukkit.*
import org.bukkit.entity.EntityType

class Venice : Campaign {

    override val campaignTitle: String = "Venice"
    override val campaignSymbol: Material = Material.BRICKS
    override lateinit var startLocation: Location
    override lateinit var restLocation: Location
    override val gameProgressLimit: Int = 3
    override val normalMobType: EntityType = EntityType.ZOMBIE
    override val config: Config = VeniceConfig
    override val world: World? = Bukkit.getWorld(campaignTitle)

    override fun createMapWorld() {
        WorldCreator(campaignTitle).type(WorldType.NORMAL).createWorld()
        startLocation = VeniceConfig.startLocation
        restLocation = VeniceConfig.restLocation
        if (world != null) {
            startLocation.chunk.load()
        } else {
            LeftCrafterDead.instance.logger.info("[createMapWorld]${ChatColor.RED}world is Null!")
        }
    }

    override fun determiningDifficulty(): Campaign.Difficulty {
        return Campaign.Difficulty.NORMAL
    }

    override fun startRush() {
        super.startRush()
    }
}