package com.github.kamunyan.leftcrafterdead.player

import org.bukkit.entity.Player


data class PlayerData(
    var uuid: String,
    var totalKill: Int,
    var exp: Int,
    var level: Int,
    var totalSkillPoint: Int
) {
    companion object {
        private val expTable = listOf(
            0, 7, 10, 14, 17,
            21, 24, 28, 31, 35,
            38, 42, 45, 49, 52,
            56, 59, 63, 66, 70,
            73, 77, 80, 84, 87,
            91, 94, 98, 101, 105,
            108, 112, 115, 119, 122,
            126, 129, 133, 136, 140,
            143, 147, 150, 154, 157,
            161, 164, 168, 171, 175,
            178
        )

        fun getNextLevelRequireEXP(level: Int, exp: Int): Int {
            if (level >= 50) return 0
            return expTable[level + 1] - exp
        }

        fun addExp(addExp: Int, data: PlayerData) {
            if (data.level >= 50) {
                data.level = 50
                data.exp = 0
                return
            }
            var exp = addExp
            var nextLevelExp = expTable[data.level + 1]
            while (exp > 0) {
                exp--
                data.exp++
                if (data.exp == nextLevelExp) {
                    data.level++
                    data.exp = 0
                    nextLevelExp = expTable[data.level + 1]
                    data.totalSkillPoint += 3
                    println("レベルが${data.level}になったよ")
                    println("合計スキルポイント ${data.totalSkillPoint}")
                }
            }
        }
    }
}

fun main() {
    val data = PlayerData("", 0, 0, 0, 0)
    PlayerData.addExp(600, data)
}