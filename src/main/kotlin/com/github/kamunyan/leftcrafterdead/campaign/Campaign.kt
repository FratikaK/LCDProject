package com.github.kamunyan.leftcrafterdead.campaign

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.configs.Config
import com.github.kamunyan.leftcrafterdead.event.RushStartEvent
import org.bukkit.*
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.entity.EntityType
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.floor

interface Campaign {
    val plugin: LeftCrafterDead
        get() = LeftCrafterDead.instance

    val manager: MatchManager
        get() = MatchManager

    //Campaignタイトル
    val campaignTitle: String

    //Campaignを表すMaterial
    val campaignSymbol: Material

    //ゲーム進行度の上限
    val gameProgressLimit: Int

    //ゲームに使われる通常の敵性Mob
    val normalMobType: EntityType

    //使用するConfig
    val config: Config

    /**
     * 難易度の決定　　
     */
    fun determiningDifficulty(): Difficulty

    fun startRush() {
        val bossBar = Bukkit.createBossBar("Time to Rush", BarColor.RED, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG)
        manager.matchPlayer.forEach { lcdPlayer ->
            bossBar.addPlayer(lcdPlayer.player)
        }
        bossBar.isVisible = true
        object : BukkitRunnable() {
            var timeLeft = 1.0
            override fun run() {
                if (manager.isCheckPoint || !manager.isMatch) {
                    plugin.logger.info("[startRush]${ChatColor.AQUA}ラッシュのタイマーを停止しました")
                    bossBar.removeAll()
                    bossBar.isVisible = false
                    this.cancel()
                    return
                }

                if (timeLeft <= 0) {
                    Bukkit.getPluginManager().callEvent(RushStartEvent())
                    timeLeft = 1.0
                }

                timeLeft -= 0.02
                if (timeLeft < 0) return
                bossBar.progress = timeLeft
            }
        }.runTaskTimer(plugin, 0, 20)
    }

    companion object {
        fun createWorld(worldName: String) {
            if (Bukkit.getWorld(worldName) == null) {
                WorldCreator(worldName)
                    .environment(World.Environment.NORMAL)
                    .type(WorldType.NORMAL)
                    .hardcore(false)
                    .generateStructures(false)
                    .createWorld()
            }
        }
    }

    enum class Difficulty {
        NORMAL {
            override val normalMobSpawnAmount: Int
                get() = 3
        },
        ADVANCED {
            override val normalMobSpawnAmount: Int
                get() = 5
        },
        EXPERT {
            override val normalMobSpawnAmount: Int
                get() = 8
        };

        abstract val normalMobSpawnAmount: Int
    }
}