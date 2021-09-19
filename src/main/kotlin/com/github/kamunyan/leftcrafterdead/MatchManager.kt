package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.campaign.Venice
import com.github.kamunyan.leftcrafterdead.event.MatchStartEvent
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
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
    }

    fun finishCampaign() {}

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