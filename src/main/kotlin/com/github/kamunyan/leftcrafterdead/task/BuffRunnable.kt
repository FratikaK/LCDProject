package com.github.kamunyan.leftcrafterdead.task

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.Buff
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.scheduler.BukkitRunnable

class BuffRunnable(val lcdPlayer: LCDPlayer, private val time: Int, private val buff: Buff) : BukkitRunnable() {
    private val manager = MatchManager
    private var timeLeft = time.toDouble()
    private val bossBar = Bukkit.createBossBar(buff.buffName, BarColor.YELLOW, BarStyle.SEGMENTED_6)

    override fun run() {
        if (!lcdPlayer.isSurvivor || !manager.isMatchPlayer(lcdPlayer) || timeLeft <= 0) {
            bossBar.removeAll()
            lcdPlayer.buff.remove(buff)
            cancel()
            return
        }

        bossBar.progress = timeLeft / time
        println(bossBar.progress)
        timeLeft -= 1.0
    }

    fun addBuff() {
        if (lcdPlayer.buff.contains(buff)) {
            return
        }
        lcdPlayer.buff.add(buff)
        bossBar.addPlayer(lcdPlayer.player)
        bossBar.isVisible = true
        bossBar.progress = 1.0
        runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 20)
    }
}