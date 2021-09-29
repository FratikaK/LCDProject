package com.github.kamunyan.leftcrafterdead.player

class PlayerData(val uuid: String){

    var kill :Int = 0

    val perkLevels = HashMap<String,Int>()

    fun loadPlayerData(){}

    fun updatePlayerData(){}

    init {
        loadPlayerData()
    }
}