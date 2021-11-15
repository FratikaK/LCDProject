package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.command.AdminCommand
import com.github.kamunyan.leftcrafterdead.configs.LobbySpawnConfig
import com.github.kamunyan.leftcrafterdead.configs.campaign.VeniceConfig
import com.github.kamunyan.leftcrafterdead.listener.*
import com.github.kamunyan.leftcrafterdead.task.LagFixRunnable
import com.github.kamunyan.leftcrafterdead.task.ScoreBoardRunnable
import com.shampaggon.crackshot.CSUtility
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
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

        Campaign.createWorld("Venice")

        //load configs
        LobbySpawnConfig.loadConfig()
        VeniceConfig.loadConfig()

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