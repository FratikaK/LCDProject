package com.github.kamunyan.leftcrafterdead.campaign

import com.github.kamunyan.leftcrafterdead.configs.Config
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType

interface Campaign {

    //Campaignタイトル
    val campaignTitle: String

    //Campaignを表すMaterial
    val campaignSymbol: Material

    //スタートLocation
    val startLocation: Location

    //チェックポイント等に使われるポイント
    val restLocation: Location

    //ゲーム進行度の上限
    val gameProgressLimit: Int

    //ゲームに使われる通常の敵性Mob
    val normalMobType: EntityType

    //使用するConfig
    val config: Config

    val world: World?
        get() = Bukkit.getWorld(campaignTitle)

    fun createMapWorld()

    /**
     * 難易度の決定　　
     */
    fun determiningDifficulty(): Difficulty

    fun startRush()

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