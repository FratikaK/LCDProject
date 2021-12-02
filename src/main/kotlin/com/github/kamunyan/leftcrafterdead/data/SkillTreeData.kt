package com.github.kamunyan.leftcrafterdead.data

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object SkillTreeData {
    fun loadSkillTree(lcdPlayer: LCDPlayer) {
        transaction {
            createSkillTreeTable()
            lcdPlayer.skillTree.forEach { (t, s) ->
                val table: SkillAchievement = when (t) {
                    SkillType.MASTERMIND -> MasterMindData
                    SkillType.ENFORCER -> EnforcerData
                    SkillType.TECHNICIAN -> TechnicianData
                    SkillType.GHOST -> GhostData
                    SkillType.FUGITIVE -> FugitiveData
                }
                val select = table.select { table.uuid eq lcdPlayer.uuid }
                if (select.empty()) {
                    table.insert {
                        it[uuid] = lcdPlayer.uuid
                        it[tier0] = false
                        it[tier1_1] = false
                        it[tier1_2] = false
                        it[tier1_3] = false
                        it[tier2_1] = false
                        it[tier2_2] = false
                        it[tier2_3] = false
                        it[tier3_1] = false
                        it[tier3_2] = false
                        it[tier3_3] = false
                        it[tier4_1] = false
                        it[tier4_2] = false
                        it[tier4_3] = false
                        it[tier5_1] = false
                        it[tier5_2] = false
                        it[tier5_3] = false
                    }
                }
                table.select { table.uuid eq lcdPlayer.uuid }.forEach {
                    s.skillMap[49] = it[table.tier0]
                    s.skillMap[38] = it[table.tier1_1]
                    s.skillMap[40] = it[table.tier1_2]
                    s.skillMap[42] = it[table.tier1_3]
                    s.skillMap[29] = it[table.tier2_1]
                    s.skillMap[31] = it[table.tier2_2]
                    s.skillMap[33] = it[table.tier2_3]
                    s.skillMap[20] = it[table.tier3_1]
                    s.skillMap[22] = it[table.tier3_2]
                    s.skillMap[24] = it[table.tier3_3]
                    s.skillMap[11] = it[table.tier4_1]
                    s.skillMap[13] = it[table.tier4_2]
                    s.skillMap[15] = it[table.tier4_3]
                    s.skillMap[2] = it[table.tier5_1]
                    s.skillMap[4] = it[table.tier5_2]
                    s.skillMap[6] = it[table.tier5_3]
                }
            }
        }
    }

    fun saveSkillTree(lcdPlayer: LCDPlayer) {
        transaction {
            createSkillTreeTable()
            lcdPlayer.skillTree.forEach { (t, s) ->
                val table: SkillAchievement = when (t) {
                    SkillType.MASTERMIND -> MasterMindData
                    SkillType.ENFORCER -> EnforcerData
                    SkillType.TECHNICIAN -> TechnicianData
                    SkillType.GHOST -> GhostData
                    SkillType.FUGITIVE -> FugitiveData
                }
                table.update({ table.uuid eq lcdPlayer.uuid }) {
                    it[tier0] = s.skillMap[49]!!
                    it[tier1_1] = s.skillMap[38]!!
                    it[tier1_2] = s.skillMap[40]!!
                    it[tier1_3] = s.skillMap[42]!!
                    it[tier2_1] = s.skillMap[29]!!
                    it[tier2_2] = s.skillMap[31]!!
                    it[tier2_3] = s.skillMap[33]!!
                    it[tier3_1] = s.skillMap[20]!!
                    it[tier3_2] = s.skillMap[22]!!
                    it[tier3_3] = s.skillMap[24]!!
                    it[tier4_1] = s.skillMap[11]!!
                    it[tier4_2] = s.skillMap[13]!!
                    it[tier4_3] = s.skillMap[15]!!
                    it[tier5_1] = s.skillMap[2]!!
                    it[tier5_2] = s.skillMap[4]!!
                    it[tier5_3] = s.skillMap[6]!!
                }
            }
        }
    }

    private fun createSkillTreeTable() {
        transaction {
            SchemaUtils.create(MasterMindData)
            SchemaUtils.create(EnforcerData)
            SchemaUtils.create(TechnicianData)
            SchemaUtils.create(GhostData)
            SchemaUtils.create(FugitiveData)
        }
    }

    abstract class SkillAchievement : Table() {
        val uuid = varchar("uuid", 36)
        var tier0 = bool("49")
        var tier1_1 = bool("38")
        var tier1_2 = bool("40")
        var tier1_3 = bool("42")
        var tier2_1 = bool("29")
        var tier2_2 = bool("31")
        var tier2_3 = bool("33")
        var tier3_1 = bool("20")
        var tier3_2 = bool("22")
        var tier3_3 = bool("24")
        var tier4_1 = bool("11")
        var tier4_2 = bool("13")
        var tier4_3 = bool("15")
        var tier5_1 = bool("2")
        var tier5_2 = bool("4")
        var tier5_3 = bool("6")
        override val primaryKey: PrimaryKey = PrimaryKey(uuid, name = "Minecraft UUID")
    }

    object MasterMindData : SkillAchievement()

    object EnforcerData : SkillAchievement()

    object TechnicianData : SkillAchievement()

    object GhostData : SkillAchievement()

    object FugitiveData : SkillAchievement()
}