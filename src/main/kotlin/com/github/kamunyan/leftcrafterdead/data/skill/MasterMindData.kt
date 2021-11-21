package com.github.kamunyan.leftcrafterdead.data.skill

import org.jetbrains.exposed.sql.Table

object MasterMindData:Table() {
    val uuid = varchar("uuid", 36)
}