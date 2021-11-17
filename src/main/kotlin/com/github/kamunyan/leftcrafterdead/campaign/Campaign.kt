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
) {
    override fun toString(): String {
        val builder = StringBuilder()
        startLocations.forEach {
            builder.append("world: ${it.world.name}, x: ${it.x}, y: ${it.y}, z: ${it.z} \n")
        }
        val start = builder.toString()
        builder.clear()
        normalEnemyLocations.forEach {
            builder.append("${it.key}: ${it.value.size}, ")
        }
        val normal = builder.toString()
        return "マップ名: $campaignTitle \n" +
                "進行度上限: $gameProgressLimit \n" +
                "スタート地点: $start" +
                "チェックポイント地点: world: ${restLocation.world.name}, x: ${restLocation.x}, y: ${restLocation.y}, z: ${restLocation.z} \n" +
                "通常Enemyスポーン地点数: $normal \n" +
                "通常MobType: ${normalMobType.name}"
    }
}