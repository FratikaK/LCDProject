package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.command.AdminCommand
import com.github.kamunyan.leftcrafterdead.configs.CampaignConfig
import com.github.kamunyan.leftcrafterdead.configs.DatabaseConfig
import com.github.kamunyan.leftcrafterdead.configs.LobbySpawnConfig
import com.github.kamunyan.leftcrafterdead.data.SQLDriver
import com.github.kamunyan.leftcrafterdead.listener.*
import com.github.kamunyan.leftcrafterdead.task.LagFixRunnable
import com.github.kamunyan.leftcrafterdead.task.ScoreBoardRunnable
import com.shampaggon.crackshot.CSUtility
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import world.chiyogami.chiyogamilib.ChiyogamiLib

class LeftCrafterDead : JavaPlugin() {

    companion object {
        lateinit var instance: LeftCrafterDead
    }

    val crackShot = CSUtility()
    val chiyogamiLib: ChiyogamiLib

    init {
        instance = this
        chiyogamiLib = ChiyogamiLib(instance)
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
        CampaignConfig.loadAllCampaign()

        val manager = this.server.pluginManager
        manager.registerEvents(DamageControlListener(), this)
        manager.registerEvents(EntityControlListener(), this)
        manager.registerEvents(InventoryDisplayListener(), this)
        manager.registerEvents(JoinQuitListener(), this)
        manager.registerEvents(MatchControlListener(), this)
        manager.registerEvents(PerkListener(), this)
        manager.registerEvents(WeaponControlListener(), this)

        //Server tasks
        ScoreBoardRunnable.runTask()
        LagFixRunnable.runTask()

        SQLDriver.database = Database.connect(SQLDriver.url,SQLDriver.driver,SQLDriver.user,SQLDriver.password)
        log.info("${ChatColor.AQUA}LeftCrafterDead Start!")
        log.info("${ChatColor.AQUA}-------------------------------------")
    }

    override fun onDisable() {

    }

    /**
     * 全プレイヤーにメッセージを表示する
     */
    fun sendBroadCastMessage(message: String) {
        Bukkit.broadcastMessage("[L4D]${message}")
    }
}