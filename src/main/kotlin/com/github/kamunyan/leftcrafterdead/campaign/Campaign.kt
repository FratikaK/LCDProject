package com.github.kamunyan.leftcrafterdead.campaign

import com.github.kamunyan.leftcrafterdead.configs.Config
import org.bukkit.Location
import org.bukkit.Material

interface Campaign {

    //Campaignタイトル
    val campaignTitle: String
    //Campaignを表すMaterial
    val campaignSymbol:Material
    //スタートLocation
    val startLocation: Location
    //チェックポイント等に使われるポイント
    val restLocation:Location

    fun createMapWorld()

    /**
     * 難易度の決定　　
     */
    fun determiningDifficulty(): Difficulty

    fun startRush()

    enum class Difficulty {
        NORMAL,
        ADVANCED,
        EXPERT
    }
}