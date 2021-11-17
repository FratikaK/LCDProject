package com.github.kamunyan.leftcrafterdead.data

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.player.PlayerData
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object SQLDriver {
    private val plugin = LeftCrafterDead.instance

    lateinit var database: Database
    lateinit var url: String
    lateinit var driver: String
    lateinit var user: String
    var password: String = ""

    private fun createPlayerAchievementTable() {
        transaction {
            SchemaUtils.create(PlayerAchievement)
        }
    }

    fun loadPlayerData(data: PlayerData) {
        transaction {
            createPlayerAchievementTable()
            val select = PlayerAchievement.select { PlayerAchievement.uuid eq data.uuid }
            if (select.empty()) {
                PlayerAchievement.insert {
                    it[uuid] = data.uuid
                    it[totalKill] = 0
                    it[experience] = 0
                    it[level] = 0
                }
            }
            PlayerAchievement.select { PlayerAchievement.uuid eq data.uuid }.forEach {
                data.totalKill = it[PlayerAchievement.totalKill]
                data.totalExperience = it[PlayerAchievement.experience]
                data.level = it[PlayerAchievement.level]
            }
        }
    }

    fun savePlayerData(data: PlayerData) {
        transaction {
            createPlayerAchievementTable()
            PlayerAchievement.update({ PlayerAchievement.uuid eq data.uuid }) {
                it[totalKill] = data.totalKill
                it[experience] = data.totalExperience
                it[level] = data.level
            }
        }
    }
}