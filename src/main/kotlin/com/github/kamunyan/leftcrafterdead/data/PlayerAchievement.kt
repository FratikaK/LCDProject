package com.github.kamunyan.leftcrafterdead.data

import org.jetbrains.exposed.sql.Table

object PlayerAchievement : Table() {
    val uuid = varchar("uuid", 36)
    var totalKill = integer("totalKill")
    var exp = integer("exp")
    var level = integer("level")
    var totalSkillPoint = integer("totalSkillPoint")
    override val primaryKey: PrimaryKey = PrimaryKey(uuid, name = "Minecraft UUID")
}