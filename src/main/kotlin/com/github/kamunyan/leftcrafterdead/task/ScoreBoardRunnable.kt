package com.github.kamunyan.leftcrafterdead.task

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.util.ScoreBoardIndicator
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

object ScoreBoardRunnable : BukkitRunnable() {
    override fun run() {
        Bukkit.getOnlinePlayers().forEach {
            ScoreBoardIndicator.setScoreBoard(it)
        }
        ScoreBoardIndicator.updateMatchScoreBoard()
    }

    fun runTask() {
        runTaskTimer(LeftCrafterDead.instance, 0, 20)
    }
}