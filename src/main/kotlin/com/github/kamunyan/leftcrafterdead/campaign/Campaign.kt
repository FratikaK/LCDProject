package com.github.kamunyan.leftcrafterdead.campaign

import org.bukkit.Location
import org.bukkit.entity.EntityType

data class Campaign(
    val campaignTitle: String,
    val gameProgressLimit: Int,
    val startLocations: List<Location>,
    val restLocation: Location,
    val normalEnemyLocations: Map<Int, List<Location>>,
    val normalMobType: EntityType = EntityType.ZOMBIE,
)