package com.github.kamunyan.leftcrafterdead.campaign

import com.github.kamunyan.leftcrafterdead.configs.Config
import com.github.kamunyan.leftcrafterdead.configs.campaign.VeniceConfig
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.WorldCreator
import org.bukkit.WorldType

class Venice : Campaign {

    override val campaignTitle: String = "Venice"
    override val campaignSymbol: Material = Material.BRICKS
    override lateinit var startLocation: Location
    override lateinit var restLocation: Location
    override val config: Config = VeniceConfig

    override fun createMapWorld() {
        WorldCreator(campaignTitle).type(WorldType.NORMAL).createWorld()
        startLocation = VeniceConfig.startLocation
        restLocation = VeniceConfig.restLocation
    }

    override fun determiningDifficulty(): Campaign.Difficulty {
        return Campaign.Difficulty.NORMAL
    }

    override fun startRush() {
    }
}