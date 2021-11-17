package com.github.kamunyan.leftcrafterdead.data

import org.jetbrains.exposed.sql.Table

object PlayerAchievement : Table() {
    val uuid = varchar("uuid", 36)
    var totalKill = integer("totalKill")
    var experience = integer("experience")
    var level = integer("level")
    override val primaryKey: PrimaryKey = PrimaryKey(uuid, name = "Minecraft UUID")
}