package com.github.kamunyan.leftcrafterdead.task

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.lang.IllegalArgumentException

object AbsorptionRunnable : BukkitRunnable() {
    private val manager = MatchManager
    override fun run() {
        try {
            Bukkit.getOnlinePlayers().forEach {
                if (it.absorptionAmount > 0) {
                    it.absorptionAmount--
                }
            }
        }catch (_: IllegalArgumentException){

        }
    }

    fun runTask() {
        runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 100)
    }
}