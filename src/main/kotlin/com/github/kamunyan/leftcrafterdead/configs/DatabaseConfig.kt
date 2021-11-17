package com.github.kamunyan.leftcrafterdead.configs

import com.github.kamunyan.leftcrafterdead.data.SQLDriver
import org.jetbrains.exposed.sql.Database

object DatabaseConfig : Config("data", "data.yml") {
    override fun loadConfig() {
        val config = yml
        if (config.contains("url")) {
            SQLDriver.url = config.getString("url").toString()
        }
        if (config.contains("driver")) {
            SQLDriver.driver = config.getString("driver").toString()
        }
        if (config.contains("user")) {
            SQLDriver.user = config.getString("user").toString()
        }
        SQLDriver.password = if (config.contains("password")) {
            config.getString("password").toString()
        } else {
            ""
        }
        println("${SQLDriver.url} ${SQLDriver.driver} ${SQLDriver.user} ${SQLDriver.password}")
    }
}