package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.command.AdminCommand
import com.github.kamunyan.leftcrafterdead.configs.CampaignConfig
import com.github.kamunyan.leftcrafterdead.configs.DatabaseConfig
import com.github.kamunyan.leftcrafterdead.configs.LobbySpawnConfig
import com.github.kamunyan.leftcrafterdead.configs.SkillTreeConfig
import com.github.kamunyan.leftcrafterdead.data.SQLDriver
import com.github.kamunyan.leftcrafterdead.listener.*
import com.github.kamunyan.leftcrafterdead.task.LagFixRunnable
import com.github.kamunyan.leftcrafterdead.task.ScoreBoardRunnable
import com.shampaggon.crackshot.CSUtility
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import java.io.File

class LeftCrafterDead : JavaPlugin() {

    companion object {
        lateinit var instance: LeftCrafterDead
    }

    val crackShot = CSUtility()

    init {
        instance = this
    }

    override fun onEnable() {

        val log = this.logger
        log.info("${ChatColor.AQUA}-------------------------------------")
        log.info("${ChatColor.AQUA}LeftCrafterDead preparing...")

        //register commands
        getCommand("admin")?.setExecutor(AdminCommand())

        //load configs
        DatabaseConfig.loadConfig()
        LobbySpawnConfig.loadConfig()
        SkillTreeConfig.loadConfig()
        CampaignConfig.loadAllCampaign()

        val manager = this.server.pluginManager
        manager.registerEvents(DamageControlListener(), this)
        manager.registerEvents(EntityControlListener(), this)
        manager.registerEvents(InventoryDisplayListener(), this)
        manager.registerEvents(JoinQuitListener(), this)
        manager.registerEvents(MatchControlListener(), this)
        manager.registerEvents(PerkListener(), this)
        manager.registerEvents(WeaponControlListener(), this)

        if (!manager.isPluginEnabled("CrackShot")) {
            val crackShotPlugin = manager.loadPlugin(File("CrackShot.jar"))
            if (crackShotPlugin == null) {
                log.info("${ChatColor.RED}CrackShot, a prerequisite plugin, is missing.")
                manager.disablePlugin(this)
                return
            }
        }

        //loadWeapon handle

        //Server tasks
        ScoreBoardRunnable.runTask()
        LagFixRunnable.runTask()

        try {
            SQLDriver.database = Database.connect(SQLDriver.url, SQLDriver.driver, SQLDriver.user, SQLDriver.password)
        } catch (e: Exception) {
            log.info("${ChatColor.RED}Cannot connect to database.")
            manager.disablePlugin(this)
            return
        }
        log.info("${ChatColor.AQUA}LeftCrafterDead Start!")
        log.info("${ChatColor.AQUA}-------------------------------------")
    }

    override fun onDisable() {
        Bukkit.getScheduler().cancelTasks(this)
    }

    /**
     * 全プレイヤーにメッセージを表示する
     */
    fun sendBroadCastMessage(message: String) {
        Bukkit.broadcastMessage("[L4D]${message}")
    }
}