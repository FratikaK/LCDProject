package com.github.kamunyan.leftcrafterdead.task

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.event.StartCheckPointEvent
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.color.RegularColor
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

class EndPointRunnable : BukkitRunnable() {
    private val manager = MatchManager
    private val plugin = LeftCrafterDead.instance
    private var isAnnounced = false
    private var isAddPlayer = false
    private val timeLeft = 30
    override fun run() {
        if (!manager.isMatch || manager.isCheckPoint || manager.isBossParse) {
            plugin.logger.info("[EndPointRunnable]Canceled Tasks.")
            cancel()
            return
        }

        displayParticle()

        manager.campaign.endLocations.forEach {
            it.getNearbyPlayers(4.0).forEach{ p ->
                if (manager.getLCDPlayer(p).isMatchPlayer && manager.getLCDPlayer(p).isSurvivor){
                    Bukkit.getPluginManager().callEvent(StartCheckPointEvent())
                    cancel()
                    return
                }
            }
        }
    }

    fun runTask() {
        runTaskTimer(plugin, 0, 20)
    }

    private fun displayParticle() {
        if (manager.campaign.endLocations.isEmpty()) {
            plugin.logger.info("endLocationÇ™Ç†ÇËÇ‹ÇπÇÒÅI")
            cancel()
            return
        }
        manager.campaign.endLocations.forEach {
            for (i in 0..360) {
                val sin = sin(Math.toRadians(i.toDouble())) * 4
                val cos = cos(Math.toRadians(i.toDouble())) * 4
                ParticleBuilder(ParticleEffect.REDSTONE)
                    .setLocation(
                        it.clone().set(
                            it.x + sin,
                            it.y + 1.0,
                            it.z + cos
                        )
                    )
                    .setAmount(1)
                    .setParticleData(RegularColor(Color.YELLOW))
                    .display()
            }
        }
    }
}