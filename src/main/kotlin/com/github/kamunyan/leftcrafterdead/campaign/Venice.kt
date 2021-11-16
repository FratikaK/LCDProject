package com.github.kamunyan.leftcrafterdead.campaign

import com.github.kamunyan.leftcrafterdead.configs.Config
import com.github.kamunyan.leftcrafterdead.configs.campaign.spawn.VeniceConfig
import org.bukkit.Material
import org.bukkit.entity.EntityType

class Venice : Campaign {

    override val campaignTitle: String = "Venice"
    override val campaignSymbol: Material = Material.BRICKS
    override val gameProgressLimit: Int = 3
    override val normalMobType: EntityType = EntityType.ZOMBIE
    override val config: Config = VeniceConfig

    override fun determiningDifficulty(): Campaign.Difficulty {
        return Campaign.Difficulty.NORMAL
    }

    override fun startRush() {
        super.startRush()
    }
}