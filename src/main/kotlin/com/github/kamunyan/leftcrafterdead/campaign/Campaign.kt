package com.github.kamunyan.leftcrafterdead.campaign

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.EntityType

data class Campaign(
    val campaignTitle: String,
    val mapName:String,
    val gameProgressLimit: Int,
    val startLocations: List<Location>,
    val endLocations: List<Location>,
    val restLocation: Location,
    val supplyLocations: List<Location>,
    val traderLocation: Location?,
    val normalEnemyLocations: Map<Int, List<Location>>,
    val normalMobType: EntityType = EntityType.ZOMBIE,
) {
    private val plugin = LeftCrafterDead.instance
    fun campaignInformation() {
        plugin.logger.info("${ChatColor.GREEN}===================================")
        plugin.logger.info("マップ名: $campaignTitle")
        plugin.logger.info("セクション数: $gameProgressLimit")
        plugin.logger.info("プレイヤー開始地点")
        startLocations.forEach {
            plugin.logger.info("world: ${it.world.name}, x: ${it.x}, y: ${it.y}, z: ${it.z}")
        }
        plugin.logger.info("Enemyスポーン地点の数")
        normalEnemyLocations.forEach { (t, u) ->
            plugin.logger.info("section${t}: ${u.size}")
        }
        plugin.logger.info("通常Mobのタイプ: $normalMobType")
        plugin.logger.info("${ChatColor.GREEN}===================================")
    }
}