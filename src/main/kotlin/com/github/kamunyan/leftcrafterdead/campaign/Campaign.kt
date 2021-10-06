package com.github.kamunyan.leftcrafterdead.campaign

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.configs.Config
import com.github.kamunyan.leftcrafterdead.event.RushStartEvent
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.scheduler.BukkitRunnable

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
        object : BukkitRunnable() {
            var timeLeft = 60
            override fun run() {
                if (manager.isCheckPoint || !manager.isMatch) {
                    plugin.logger.info("[startRush]${ChatColor.AQUA}ラッシュのタイマーを停止しました")
                    this.cancel()
                    return
                }

                if (timeLeft == 30) {
                    plugin.logger.info("[startRush]${ChatColor.AQUA}次のラッシュまで30秒")
                }

                if (timeLeft == 10) {
                    plugin.logger.info("[startRush]${ChatColor.AQUA}次のラッシュまで10秒")
                }

                if (timeLeft <= 0) {
                    Bukkit.getPluginManager().callEvent(RushStartEvent())
                    timeLeft = 60
                }
                timeLeft -= 1
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