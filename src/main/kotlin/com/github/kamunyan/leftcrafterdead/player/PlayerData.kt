package com.github.kamunyan.leftcrafterdead.player

import java.io.Serializable

data class PlayerData(
    var uuid: String,
    var totalKill: Int,
    var totalExperience: Int,
    var level:Int
) : Serializable