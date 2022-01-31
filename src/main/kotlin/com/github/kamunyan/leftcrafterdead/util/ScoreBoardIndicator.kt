package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.PlayerData
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard

object ScoreBoardIndicator {
    private val manager = MatchManager
    private val matchScoreBoard = Bukkit.getScoreboardManager().newScoreboard

    fun updateMatchScoreBoard() {
        if (manager.isMatch || manager.isPreparation) {
            val obj = matchScoreBoard.getObjective("side") ?: matchScoreBoard.registerNewObjective(
                "side",
                "dummy",
                Component.text("${ChatColor.AQUA}====LeftCrafterDead====")
            )
            obj.displaySlot = DisplaySlot.SIDEBAR
            val lines = matchBoardLine()
            setScores(lines, matchScoreBoard, obj)
        }
    }

    private fun updateLobbyScoreBoard(player: Player): Scoreboard {
        val scoreBoard = Bukkit.getScoreboardManager().newScoreboard
        val obj = scoreBoard.registerNewObjective(
            "side",
            "dummy",
            Component.text("${ChatColor.AQUA}====LeftCrafterDead====")
        )
        obj.displaySlot = DisplaySlot.SIDEBAR
        val lines = lobbyBoardLine(player)
        setScores(lines, scoreBoard, obj)
        return scoreBoard
    }

    fun setScoreBoard(player: Player) {
        val lcdPlayer = manager.getLCDPlayer(player)
        if (lcdPlayer.isMatchPlayer) {
            if (player.scoreboard != matchScoreBoard) {
                player.scoreboard = matchScoreBoard
            }
        } else {
            player.scoreboard = updateLobbyScoreBoard(player)
        }
        //player head name
        val displayName: String = if (lcdPlayer.isMatchPlayer) {
            "${ChatColor.RED}HP${player.health.toInt()}/${lcdPlayer.statusData.healthScaleAmount}" +
                    " ${ChatColor.WHITE}${player.name} ${ChatColor.AQUA}Lv${lcdPlayer.playerData.level}"
        } else {
            "${player.name} ${ChatColor.AQUA}Lv${lcdPlayer.playerData.level}"
        }
        player.displayName(Component.text(displayName))
        player.isCustomNameVisible = true
    }

    private fun matchBoardLine(): ArrayList<String> {
        val messageList = ArrayList<String>()
        messageList.add("")
        messageList.add("Stage:  ${manager.campaign.mapName}")
        messageList.add("Difficulty:  ${manager.campaignDifficulty}")
        messageList.add("${ChatColor.WHITE}Survivor:  ${ChatColor.GOLD}${manager.numberOfSurvivors()}")
        messageList.add("")

        for (lcdPlayer in manager.matchPlayer) {
            if (!lcdPlayer.isSurvivor) {
                continue
            }
            val name = lcdPlayer.player.name
            val healthScale = lcdPlayer.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value.toInt()
            val health = lcdPlayer.player.health.toInt()
            val money = lcdPlayer.campaignData.money
            val kill = lcdPlayer.campaignData.kill
            messageList.add(
                "${ChatColor.GOLD}HP${health}/${healthScale} ${ChatColor.WHITE}$name " +
                        "${ChatColor.RED}${kill} ${ChatColor.GOLD}$${money}"
            )
        }
        return messageList
    }

    private fun lobbyBoardLine(player: Player): ArrayList<String> {
        val messageList = ArrayList<String>()
        val lcdPlayer = manager.getLCDPlayer(player)
        val level = lcdPlayer.playerData.level
        val nextExp = PlayerData.getNextLevelRequireEXP(level, lcdPlayer.playerData.exp)
        val totalSkillPoint = lcdPlayer.playerData.totalSkillPoint
        messageList.add("")
        messageList.add("${ChatColor.AQUA}${player.name}")
        messageList.add("${ChatColor.WHITE}レベル:  ${ChatColor.AQUA}${lcdPlayer.playerData.level}")
        messageList.add("${ChatColor.WHITE}総キル数: ${ChatColor.RED}${lcdPlayer.playerData.totalKill}")
        messageList.add("${ChatColor.WHITE}次のレベルまで  ${ChatColor.YELLOW}${nextExp}Exp")
        messageList.add("${ChatColor.WHITE}Perk:  ${ChatColor.LIGHT_PURPLE}${lcdPlayer.perk.perkType.perkName}")
        messageList.add("${ChatColor.AQUA}Skill Point: ${ChatColor.GOLD}${lcdPlayer.skillPoint}/${totalSkillPoint}")

        return messageList
    }

    private fun setScores(lines: ArrayList<String>, scoreboard: Scoreboard, obj: Objective) {
        lines.reverse()

        scoreboard.entries.forEach(scoreboard::resetScores)

        for ((value, msg) in lines.withIndex()) {
            var m = msg
            while (obj.getScore(m).isScoreSet) {
                m += " "
            }
            obj.getScore(m).score = value
        }

    }
}