package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.campaign.Venice
import com.github.kamunyan.leftcrafterdead.event.MatchStartEvent
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.ArrayList

class Match {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    //使用するCampaign
    var campaign: Campaign = Venice()

    val matchPlayer = ArrayList<LCDPlayer>()

    var isPreparation = false

    var isMatch = false

    fun startPreparation() {
        isPreparation = true
        object : BukkitRunnable() {
            var timeLeft = 30
            override fun run() {
                if (timeLeft <= 0){
                    cancel()
                    MatchStartEvent().callEvent()
                    isPreparation = false
                    return
                }
                timeLeft--
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20)
    }

    fun startCampaign() {
    }

    fun isMatchPlayer(uuid: UUID): Boolean {
        val lcdPlayer = Bukkit.getPlayer(uuid)?.let { manager.getL4DPlayer(it) }
        if (lcdPlayer != null) {
            return lcdPlayer.isMatchPlayer
        }
        return false
    }

    fun isMatchPlayer(lcdPlayer: LCDPlayer): Boolean {
        return lcdPlayer.isMatchPlayer
    }
}