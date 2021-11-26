package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.enemy.NormalEnemy
import com.github.kamunyan.leftcrafterdead.event.*
import com.github.kamunyan.leftcrafterdead.util.InventoryDisplayer
import com.github.kamunyan.leftcrafterdead.util.MetadataUtil
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
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
                Material.ENCHANTED_BOOK -> e.player.openInventory(InventoryDisplayer.mainMenuDisplay())
                else -> return
            }
        }
    }

    @EventHandler
    fun onCampaignEntityDamage(e: EntityDamageByEntityEvent) {
        if (e.entity.type == EntityType.SHEEP) {
            e.isCancelled = true
            if (e.damager.type == EntityType.PLAYER) {
                manager.joinPlayer(e.damager as Player)
                e.entity.playEffect(EntityEffect.HURT)
                e.entity.world.playSound(e.entity.location, Sound.ENTITY_SHEEP_AMBIENT, 3f, 0f)
            }
        }
    }

    @EventHandler
    fun onLCDPlayerDeath(e: LCDPlayerDeathEvent) {
        val lcdPlayer = e.lcdPlayer
        lcdPlayer.isSurvivor = false

        val deathMessage = "${ChatColor.YELLOW}DEATH${ChatColor.RED}  =====>>>>" +
                "  ${ChatColor.YELLOW}${lcdPlayer.player.displayName}"
        Bukkit.getOnlinePlayers().forEach { player ->
            player.playSound(player.location, Sound.ENTITY_WOLF_HOWL, 100f, 1f)
        }
        Bukkit.broadcastMessage(
            deathMessage + "\n" +
                    "[LCD]${ChatColor.AQUA}残り生存者数 : ${ChatColor.YELLOW}${manager.numberOfSurvivors()}"
        )
        if (manager.numberOfSurvivors() <= 0) {
            Bukkit.broadcastMessage("[LCD]${ChatColor.RED}プレイヤーが全滅しました")
            manager.finishCampaign()
        }
    }

    @EventHandler
    fun onStartCheckPoint(e: StartCheckPointEvent) {
        if (manager.isMatch && !manager.isCheckPoint) {
            manager.startCheckPoint()
        }
    }

    @EventHandler
    fun onMatchReStart(e: MatchReStartEvent) {
        manager.isCheckPoint = false
        manager.matchPlayer.forEach {
            if (it.isMatchPlayer && it.isSurvivor) {
                plugin.chiyogamiLib.smoothTeleport(it.player, manager.campaign.startLocations[manager.gameProgress])
                it.player.sendTitle("Stage ${MatchManager.gameProgress + 1} Start!", null, 20, 100, 20)
            }
        }
        manager.startRush()
        if (manager.isBossParse) {
            Bukkit.getPluginManager().callEvent(BossParseStartEvent())
            return
        }
        manager.determineDifficulty()
        manager.spawnNormalEnemyMob()
    }

    @EventHandler
    fun onBossStart(e: BossParseStartEvent) {
        val locations = manager.campaign.normalEnemyLocations[manager.gameProgress]
        if (locations != null) {
            if (locations.isEmpty()) {
                plugin.logger.info("${ChatColor.RED}[onBossStart]ボスが湧ける場所がありません！")
                manager.finishCampaign()
                return
            }

            val random = Random().nextInt(manager.bossList.size)
            manager.bossList[random].spawnEnemy(locations[0])
        } else {
            manager.finishCampaign()
        }
    }

    @EventHandler
    fun onDefeatBoss(e: DefeatBossEvent) {
        if (!manager.isMatch) {
            return
        }
        manager.deleteEnemyMob()
        val addExp = 5 * manager.campaignDifficulty.expRate
        val getExp = 30 * manager.numberOfSurvivors() + addExp
        manager.matchPlayer.forEach {
            it.player.sendTitle("${ChatColor.AQUA}Clear", "", 30, 100, 30)
            it.campaignData.exp += getExp
            it.player.sendMessage(Component.text("${ChatColor.GOLD}生存者数ボーナス ===> ${addExp}Exp"))
            it.player.sendMessage(Component.text("獲得経験値 ===> ${getExp}Exp"))
        }
        plugin.sendBroadCastMessage("[LCD]${ChatColor.AQUA}生存者がボスを撃破しました！ゲームクリアです！")
        manager.finishCampaign()
    }

    @EventHandler
    fun onRushStart(e: RushStartEvent) {
        if (manager.matchPlayer.isNotEmpty()) {
            val survivorList = ArrayList<Player>()
            manager.matchPlayer.forEach {
                if (it.isSurvivor) {
                    survivorList.add(it.player)
                }
            }
            if (survivorList.isEmpty()) {
                return
            }
            val random = Random().nextInt(survivorList.size)
            val player = survivorList[random]
            val playerLocation = player.location.clone()

            //プレイヤーの座標から一番近いmobスポーンポイントにスポーンさせる
            var minLocation: Location? = null
            var minDistance: Double? = null

            val enemyLocations = manager.campaign.normalEnemyLocations[manager.gameProgress]
            if (enemyLocations == null) {
                plugin.logger.info("[RushStartEvent]${ChatColor.RED}RushEnemyを沸かせるLocationが存在しません！")
                return
            }

            try {
                manager.campaign.normalEnemyLocations[manager.gameProgress]!!.forEach { location ->
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
                if (minLocation == null) {
                    plugin.logger.info("[onRushStart]${ChatColor.RED}minLocation is Null!")
                    return
                }
                for (i in 0..(manager.matchPlayer.size * manager.campaignDifficulty.normalMobSpawnAmount)) {
                    val locations = arrayOf(
                        minLocation,
                        minLocation!!.clone().add(1.0, 0.0, 0.0),
                        minLocation!!.clone().add(-1.0, 0.0, 0.0),
                        minLocation!!.clone().add(0.0, 0.0, 1.0),
                        minLocation!!.clone().add(0.0, 0.0, -1.0)
                    )
                    locations.forEach { location ->
                        NormalEnemy.spawnEnemy(location!!)
                    }
                }
                manager.spawnSpecialEnemyMob(minLocation!!)
                manager.matchPlayer.forEach {
                    it.player.playSound(player.location, Sound.ENTITY_WITHER_SPAWN, 10f, 1f)
                }
                plugin.sendBroadCastMessage("${ChatColor.RED}奴らが来る...")
                plugin.logger.info("[onRushStart]${ChatColor.AQUA}RushMobがスポーンしました")
            } catch (exception: IllegalArgumentException) {
                plugin.logger.info("[onRushStart]Canceled MatchStartEvent.")
                e.isCancelled = true
            }
        }
    }
}