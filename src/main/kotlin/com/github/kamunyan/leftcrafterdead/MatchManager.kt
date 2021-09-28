package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.campaign.Venice
import com.github.kamunyan.leftcrafterdead.event.MatchStartEvent
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import com.github.kamunyan.leftcrafterdead.weapons.secondary.HandGun
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.NotNull
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

object MatchManager {
    private val plugin = LeftCrafterDead.instance

    //ロビーの初期スポーン
    lateinit var lobbySpawnLocation: Location

    //オンライン上のプレイヤーHashMap
    val onlineLCDPlayer = ConcurrentHashMap<String, LCDPlayer>()

    //使用するCampaign
    var campaign: Campaign = Venice()

    val matchPlayer = mutableListOf<LCDPlayer>()

    var gameProgress = 1

    val mobSpawnLocationList = ArrayList<Location>()

    var isPreparation = false

    var isMatch = false

    var isCheckPoint = false

    @Synchronized
    fun getLCDPlayer(@NotNull target: Player): LCDPlayer {
        val uuid = target.uniqueId.toString()
        if (!onlineLCDPlayer.containsKey(uuid)) {
            onlineLCDPlayer[uuid] = LCDPlayer(uuid)
        }
        return onlineLCDPlayer[uuid]!!
    }

    @Synchronized
    fun getLCDPlayer(@NotNull uuid: UUID): LCDPlayer {
        return getLCDPlayer(Bukkit.getPlayer(uuid)!!)
    }

    private fun startPreparation() {
        if (isMatch) {
            plugin.logger.info("${ChatColor.RED}[LCD]すでにマッチを開始しています")
            return
        } else if (isPreparation) {
            return
        }
        isPreparation = true
        object : BukkitRunnable() {
            var timeLeft = 30
            override fun run() {
                if (onlineLCDPlayer.isEmpty()) {
                    cancel()
                    isPreparation = false
                    return
                }

                if (timeLeft <= 30) {
                    if (timeLeft == 30) {
                        Bukkit.getOnlinePlayers().forEach { player ->
                            player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 2f, 0f)
                            player.sendMessage("[LCD]${ChatColor.AQUA}30秒後にゲームを開始します")
                        }
                    }
                    Bukkit.getOnlinePlayers().forEach { player ->
                        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                        if (timeLeft <= 5) {
                            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 24f)
                            player.sendTitle("$timeLeft", "", 5, 10, 5)
                        }
                    }
                }

                if (timeLeft <= 0) {
                    cancel()
                    isPreparation = false
                    Bukkit.getPluginManager().callEvent(MatchStartEvent())
                    return
                }
                timeLeft--
            }
        }.runTaskTimer(plugin, 0, 20)
    }

    @Synchronized
    fun startCampaign() {
        if (!isMatch) {
            plugin.logger.info("${ChatColor.RED}[LCD](startCampaign)isMatch isn't true.")
            return
        }

        campaign.createMapWorld()
        campaign.config.loadCampaignConfig()

        matchPlayer.forEach { lcdPlayer ->
            lcdPlayer.player.teleport(campaign.startLocation)
            lcdPlayer.player.health = 20.0
            lcdPlayer.player.foodLevel = 6
            lcdPlayer.perk.setFirstWeapon(lcdPlayer)
            lcdPlayer.perk.firstPrimaryWeapon()
            lcdPlayer.isMatchPlayer = true
            lcdPlayer.isSurvivor = true
        }
        spawnNormalEnemyMob()
        campaign.startRush()
    }

    @Synchronized
    fun finishCampaign() {
        if (!isMatch) {
            plugin.logger.info("[finishCampaign]isMatch isn't false!")
            return
        }

        object : BukkitRunnable() {
            var timeLeft = 15
            override fun run() {
                if (timeLeft <= 15) {
                    Bukkit.getOnlinePlayers().forEach { player ->
                        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                        if (timeLeft <= 5) {
                            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 24f)
                            player.sendTitle("$timeLeft", "", 5, 10, 5)
                        }
                    }
                }
                if (timeLeft <= 5) {
                    Bukkit.broadcastMessage("[LCD]ゲーム終了まで${timeLeft}秒")
                }

                if (timeLeft <= 0) {
                    initializeGame()
                    this.cancel()
                    return
                }
                timeLeft--
            }
        }.runTaskTimer(plugin, 0, 20)
    }

    fun initializeGame() {
        isMatch = false
        campaign = Venice()
        gameProgress = 1
        mobSpawnLocationList.clear()
        isCheckPoint = false
        deleteEnemyMob()

        matchPlayer.forEach { lcdPlayer ->
            lcdPlayer.initialize()
        }

        Bukkit.getOnlinePlayers().forEach { player ->
            player.teleport(lobbySpawnLocation)
        }
        if (matchPlayer.isNotEmpty()) {
            startPreparation()
        }
    }

    @Synchronized
    fun joinPlayer(player: Player) {
        if (isMatchPlayer(player.uniqueId)) {
            player.sendMessage("${ChatColor.RED}すでにゲームに参加しています")
            return
        }
        val lcdPlayer = getLCDPlayer(player)
        lcdPlayer.isMatchPlayer = true
        matchPlayer.add(lcdPlayer)

        if (!isMatch && !isPreparation && matchPlayer.size == 1) {
            startPreparation()
        }

        if (!isMatch && isPreparation) {
            lcdPlayer.isSurvivor = true
        } else if (isCheckPoint) {
            lcdPlayer.isSurvivor = true
            lcdPlayer.perk.setFirstWeapon(lcdPlayer)
            lcdPlayer.secondaryWeapon = HandGun("P226", WeaponType.Secondary)
            lcdPlayer.player.health = 20.0
            lcdPlayer.player.foodLevel = 6
            lcdPlayer.player.teleport(campaign.restLocation)
        } else {
            //途中参加
            if (matchPlayer.isNotEmpty()) {
                lcdPlayer.player.gameMode = GameMode.SPECTATOR
                matchPlayer.forEach { p ->
                    if (lcdPlayer != p) {
                        lcdPlayer.player.teleport(p.player)
                        return
                    }
                }
            }
        }
    }

    /**
     * マッチプレイヤーであるかを返す
     */
    fun isMatchPlayer(uuid: UUID): Boolean {
        val lcdPlayer = Bukkit.getPlayer(uuid)?.let { getLCDPlayer(it) }
        if (lcdPlayer != null) {
            return lcdPlayer.isMatchPlayer
        }
        return false
    }

    fun isMatchPlayer(lcdPlayer: LCDPlayer): Boolean {
        return lcdPlayer.isMatchPlayer
    }

    /**
     * ゲームの生存者数を返す
     */
    fun numberOfSurvivors(): Int {
        var count = 0
        matchPlayer.forEach { lcdPlayer ->
            if (lcdPlayer.isSurvivor) {
                count += 1
            }
        }
        return count
    }

    /**
     * 通常の敵性Mobを湧かせる
     */
    fun spawnNormalEnemyMob() {
        if (mobSpawnLocationList.isEmpty()) {
            return
        }
        val world = campaign.world ?: return
        val mobType = campaign.normalMobType
        mobSpawnLocationList.forEach { location ->
            val location1 = location.clone().add(1.0, 0.0, 0.0)
            val location2 = location.clone().add(0.0, 0.0, 1.0)
            val location3 = location.clone().add(-1.0, 0.0, 0.0)
            val location4 = location.clone().add(0.0, 0.0, -1.0)

            var mobAmount = campaign.determiningDifficulty().normalMobSpawnAmount
            while (mobAmount > 0) {
                world.spawnEntity(location, mobType)
                world.spawnEntity(location1, mobType)
                world.spawnEntity(location2, mobType)
                world.spawnEntity(location3, mobType)
                world.spawnEntity(location4, mobType)
                mobAmount--
            }
        }
    }

    /**
     * ゲームで発生した敵性Mobを削除する
     */
    fun deleteEnemyMob() {
        val world = Bukkit.getWorld(campaign.campaignTitle)
        if (world == null) {
            plugin.logger.info("${ChatColor.RED}Worldが読み込めませんでした")
            return
        }
        var count = 0
        world.livingEntities.forEach { livingEntity ->
            if (livingEntity.type == EntityType.PLAYER || livingEntity.type == EntityType.VILLAGER) {
                return@forEach
            }
            livingEntity.remove()
            count += 1
        }
        plugin.logger.info("[deleteEnemyMob]${ChatColor.AQUA}${count}体のmobを削除しました")
    }
}