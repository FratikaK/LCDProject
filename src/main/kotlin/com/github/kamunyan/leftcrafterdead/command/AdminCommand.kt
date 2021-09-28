package com.github.kamunyan.leftcrafterdead.command

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AdminCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        var flag = false
        if (command.name != "admin") return flag
        if (sender is Player) {
            sender.sendMessage("${ChatColor.RED}コンソールから実行してください")
        }

        if (args.isEmpty()) {
            sendAdminCommandInfo(sender)
            return flag
        }

        when (args[0]) {
            "info" -> {
                if (args.size == 2) {
                    val player = Bukkit.getPlayer(args[1])
                    if (player != null && MatchManager.onlineL4DPlayer.containsKey(player.uniqueId.toString())) {
                        val lcdPlayer = MatchManager.onlineL4DPlayer[player.uniqueId.toString()]
                        sender.sendMessage(lcdPlayer.toString())
                        flag = true
                    }
                }
            }
            "removeEntity" -> {
                MatchManager.deleteEnemyMob()
                flag = true
            }
            "reload" -> {
                LeftCrafterDead.instance.server.reload()
                flag = true
            }
            else -> {
            }
        }
        if (!flag) {
            sendAdminCommandInfo(sender)
        }
        return flag
    }

    private fun sendAdminCommandInfo(sender: CommandSender) {
        sender.sendMessage(
            "admin Command usage\n" +
                    "admin info [player name]: LCDPlayerの詳細を返します\n" +
                    "admin info [reload]: サーバをリロードします\n" +
                    "admin removeEntity: マップの敵性mobを削除します"
        )
    }
}