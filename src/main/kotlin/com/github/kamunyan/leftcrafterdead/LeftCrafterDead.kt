package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.listener.JoinQuitListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class LeftCrafterDead : JavaPlugin() {

    companion object {
        lateinit var instance: JavaPlugin
    }

    init {
        instance = this
    }

    override fun onEnable() {

        sendLog("${ChatColor.AQUA}-------------------------------------")
        sendLog("${ChatColor.AQUA}LeftCrafterDead preparing...")

        registerEvent(JoinQuitListener())

        sendLog("${ChatColor.AQUA}LeftCrafterDead Start!")
        sendLog("${ChatColor.AQUA}-------------------------------------")

    }

    override fun onDisable() {

    }

    /**
     * 全プレイヤーにメッセージを表示する
     */
    fun sendBroadCastMessage(message: String){
        Bukkit.broadcastMessage("[L4D]${message}")
    }

    /**
     * ログを出力する
     */
    fun sendLog(log:String){
        this.logger.info(log)
    }

    private fun registerEvent(listener :Listener){
        this.server.pluginManager.registerEvents(listener,this)
    }

}