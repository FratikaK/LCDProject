package com.github.kamunyan.leftcrafterdead.command

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.util.MetadataUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class AdminCommand : CommandExecutor {
    private val plugin = LeftCrafterDead.instance
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

        try {
            when (args[0]) {
                "reload" -> {
                    MatchManager.onlineLCDPlayer.forEach { (_, l) ->
                        l.initialize()
                    }
                    plugin.server.reload()
                }
                "playerInit" -> {
                    MatchManager.onlineLCDPlayer.forEach { (_, lcdPlayer) ->
                        lcdPlayer.initialize()
                    }
                    sender.sendMessage("全てのプレイヤーをinitしました")
                    flag = true
                }
                "info" -> {
                    if (args.size == 2) {
                        val player = Bukkit.getPlayer(args[1])
                        if (player != null && MatchManager.onlineLCDPlayer.containsKey(player.uniqueId.toString())) {
                            val lcdPlayer = MatchManager.onlineLCDPlayer[player.uniqueId.toString()]
                            sender.sendMessage(lcdPlayer.toString())
                            flag = true
                        }
                    }
                }
                "removeEntity" -> {
                    MatchManager.deleteEnemyMob()
                    flag = true
                }
                "creative" -> {
                    if (args.size == 2) {
                        val player = Bukkit.getPlayer(args[1])
                        if (player != null) {
                            MatchManager.getLCDPlayer(player).gameMode = GameMode.CREATIVE
                            flag = true
                        }
                    }
                }
                "campaignMob" -> {
                    if (args.size == 3) {
                        when (args[1]) {
                            "spawn" -> {
                                val player = Bukkit.getPlayer(args[2])
                                if (player != null) {
                                    val mob =
                                        player.world.spawnEntity(player.location, EntityType.SHEEP) as LivingEntity
                                    mob.setAI(false)
                                    mob.customName = "${ChatColor.AQUA}JOIN GAME"
                                    mob.isCustomNameVisible = true
                                    MetadataUtil.setLivingEntityMetadata(mob, MetadataUtil.CAMPAIGN_JOIN_MOB)
                                    flag = true
                                }
                            }
                        }
                    }
                }
                "merchant" -> {
                    if (args.size == 3) {
                        when (args[1]) {
                            "spawn" -> {
                                val player = Bukkit.getPlayer(args[2])
                                if (player != null) {
                                    val merchant =
                                        player.world.spawnEntity(player.location, EntityType.VILLAGER) as LivingEntity
                                    merchant.setAI(false)
                                    merchant.customName = "${ChatColor.AQUA}商人"
                                    merchant.isCustomNameVisible = true
                                    MetadataUtil.setLivingEntityMetadata(merchant, MetadataUtil.MERCHANT)
                                    flag = true
                                }
                            }
                            "remove" -> {
                                val player = Bukkit.getPlayer(args[2])
                                player?.location?.getNearbyLivingEntities(3.0)?.forEach {
                                    if (it.type == EntityType.VILLAGER) {
                                        if (it.hasMetadata(MetadataUtil.MERCHANT)) {
                                            it.remove()
                                            flag = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "teleport" -> {
                    if (args.size == 3) {
                        if (Bukkit.getWorld(args[1]) != null && Bukkit.getPlayer(args[2]) != null) {
                            MatchManager.campaignList.forEach {
                                if (it.mapName == args[1]) {
                                    Bukkit.getPlayer(args[2])!!.teleport(it.startLocations[0])
                                    flag = true
                                }
                            }
                        }
                    }
                }
                else -> {
                }
            }
            if (!flag) {
                sendAdminCommandInfo(sender)
            }
        } catch (exception: Exception) {
            sendAdminCommandInfo(sender)
        }
        return flag
    }

    private fun sendAdminCommandInfo(sender: CommandSender) {
        sender.sendMessage(
            "admin Command usage\n" +
                    "admin reload: プレイヤーデータをセーブして、サーバーをリロードします\n" +
                    "admin info [player name]: LCDPlayerの詳細を返します\n" +
                    "admin removeEntity: マップの敵性mobを削除します\n" +
                    "admin creative [player name]: 対象プレイヤーをクリエイティブにします"
        )
    }
}