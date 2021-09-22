package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.campaign.Venice
import com.github.kamunyan.leftcrafterdead.event.MatchStartEvent
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import com.github.kamunyan.leftcrafterdead.weapons.secondary.HandGun
import org.bukkit.*
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
    val onlineL4DPlayer = ConcurrentHashMap<String, LCDPlayer>()

    //使用するCampaign
    var campaign: Campaign = Venice()

    val matchPlayer = mutableListOf<LCDPlayer>()

    var isPreparation = false

    var isMatch = false

    var isCheckPoint = false

    @Synchronized
    fun getL4DPlayer(@NotNull target: Player): LCDPlayer {
        val uuid = target.uniqueId.toString()
        if (!onlineL4DPlayer.containsKey(uuid)) {
            onlineL4DPlayer[uuid] = LCDPlayer(uuid)
        }
        return onlineL4DPlayer[uuid]!!
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
                if (onlineL4DPlayer.isEmpty()) {
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

    fun startCampaign() {
        if (!isMatch) {
            plugin.logger.info("${ChatColor.RED}[LCD](startCampaign)isMatch isn't true.")
            return
        }
        campaign.createMapWorld()
        if (!campaign.startLocation.isChunkLoaded) {
            campaign.startLocation.chunk.load()
        }

        matchPlayer.forEach { lcdPlayer ->
            lcdPlayer.player.teleport(campaign.startLocation)
            lcdPlayer.player.health = 20.0
            lcdPlayer.player.foodLevel = 6
            lcdPlayer.perk.setFirstWeapon(lcdPlayer)
            lcdPlayer.perk.firstPrimaryWeapon()
        }
    }

    fun finishCampaign() {}

    fun joinPlayer(player: Player) {
        if (isMatchPlayer(player.uniqueId)) {
            player.sendMessage("${ChatColor.RED}すでにゲームに参加しています")
            return
        }
        val lcdPlayer = getL4DPlayer(player)
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

    fun isMatchPlayer(uuid: UUID): Boolean {
        val lcdPlayer = Bukkit.getPlayer(uuid)?.let { getL4DPlayer(it) }
        if (lcdPlayer != null) {
            return lcdPlayer.isMatchPlayer
        }
        return false
    }

    fun isMatchPlayer(lcdPlayer: LCDPlayer): Boolean {
        return lcdPlayer.isMatchPlayer
    }
}