package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.event.LCDPlayerDeathEvent
import com.github.kamunyan.leftcrafterdead.event.MatchStartEvent
import com.github.kamunyan.leftcrafterdead.event.RushStartEvent
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.lang.IllegalArgumentException
import java.util.*

class MatchControlListener : Listener {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    @EventHandler
    fun onMatchStart(e: MatchStartEvent) {
        if (manager.isMatch) {
            plugin.logger.info("${ChatColor.RED}[LCD]すでにマッチを開始しています")
            return
        } else {
            manager.isMatch = true
        }
        manager.startCampaign()
    }

    @EventHandler
    fun onLobbyItemRightInteract(e: PlayerInteractEvent) {
        if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
            return
        }

        if (e.item != null && e.item!!.hasItemMeta()) {
            val item = e.item!!
            when (item.type) {
                Material.DIAMOND -> manager.joinPlayer(e.player)
                else -> return
            }
        }
    }

    @EventHandler
    fun onLCDPlayerDeath(e: LCDPlayerDeathEvent) {
        val lcdPlayer = e.lcdPlayer
        lcdPlayer.isSurvivor = false
        if (manager.numberOfSurvivors() <= 0){
            manager.finishCampaign()
        }
    }

    @EventHandler
    fun onRushStart(e: RushStartEvent) {
        if (manager.matchPlayer.isNotEmpty()) {
            //ランダムにプレイヤーを決定する
            val random = Random().nextInt(manager.matchPlayer.size)
            val player = manager.matchPlayer[random].player
            val playerLocation = player.location.clone()

            //プレイヤーの座標から一番近いmobスポーンポイントにスポーンさせる
            var minLocation: Location? = null
            var minDistance: Double? = null

            try {
                manager.mobSpawnLocationList.forEach { location ->
                    if (minLocation == null) {
                        minLocation = location
                        minDistance = location.distance(playerLocation)
                        return@forEach
                    }
                    val distance = location.distance(playerLocation)
                    if (distance < minDistance!!) {
                        minLocation = location
                        minDistance = distance
                    }
                }
                if (manager.campaign.world != null) {
                    if (minLocation == null) {
                        plugin.logger.info("[onRushStart]${ChatColor.RED}minLocation is Null!")
                        return
                    }
                    for (i in 0..(manager.matchPlayer.size * manager.campaign.determiningDifficulty().normalMobSpawnAmount)) {
                        val locations = arrayOf(
                            minLocation,
                            minLocation!!.clone().add(1.0, 0.0, 0.0),
                            minLocation!!.clone().add(-1.0, 0.0, 0.0),
                            minLocation!!.clone().add(0.0, 0.0, 1.0),
                            minLocation!!.clone().add(0.0, 0.0, -1.0)
                        )
                        locations.forEach { location ->
                            manager.campaign.world!!.spawnEntity(
                                minLocation!!,
                                EntityType.ZOMBIE,
                                CreatureSpawnEvent.SpawnReason.CUSTOM
                            )
                        }
                    }
                    plugin.logger.info("[onRushStart]${ChatColor.AQUA}RushMobがスポーンしました")
                }
            } catch (exception: IllegalArgumentException) {
                plugin.logger.info("[onRushStart]Canceled MatchStartEvent.")
                e.isCancelled = true
            }
        }
    }
}