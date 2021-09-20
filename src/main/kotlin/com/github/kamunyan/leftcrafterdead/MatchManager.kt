package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.campaign.Venice
import com.github.kamunyan.leftcrafterdead.event.MatchStartEvent
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import com.github.kamunyan.leftcrafterdead.weapons.secondary.HandGun
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
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

    val matchPlayer = ArrayList<LCDPlayer>()

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

    fun startPreparation() {
        if (isMatch) {
            plugin.logger.info("${ChatColor.RED}[LCD]すでにマッチを開始しています")
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
                if (timeLeft <= 0) {
                    cancel()
                    isPreparation = false
                    MatchStartEvent().callEvent()
                    return
                }
                timeLeft--
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20)
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