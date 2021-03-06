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
                plugin.logger.info("[loadPlayerData]新しくプレイヤーのデータを作成します")
                PlayerAchievement.insert {
                    it[uuid] = data.uuid
                    it[totalKill] = 0
                    it[exp] = 0
                    it[level] = 0
                    it[totalSkillPoint] = 5
                }
            }
            PlayerAchievement.select { PlayerAchievement.uuid eq data.uuid }.forEach {
                data.totalKill = it[PlayerAchievement.totalKill]
                data.exp = it[PlayerAchievement.exp]
                data.level = it[PlayerAchievement.level]
                data.totalSkillPoint = it[PlayerAchievement.totalSkillPoint]
            }
            plugin.logger.info("LoadPlayerData totalKill${data.totalKill} exp${data.exp} level${data.level} totalSkillPoint${data.totalSkillPoint}")
        }
    }

    fun savePlayerData(data: PlayerData) {
        transaction {
            createPlayerAchievementTable()
            PlayerAchievement.update({ PlayerAchievement.uuid eq data.uuid }) {
                it[totalKill] = data.totalKill
                it[exp] = data.exp
                it[level] = data.level
                it[totalSkillPoint] = data.totalSkillPoint
            }
            plugin.logger.info("SavePlayerData totalKill${data.totalKill} exp${data.exp} level${data.level} totalSkillPoint${data.totalSkillPoint}")
        }
    }
}