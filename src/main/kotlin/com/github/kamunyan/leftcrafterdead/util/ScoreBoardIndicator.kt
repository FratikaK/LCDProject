package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.MatchManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import kotlin.collections.ArrayList

object ScoreBoardIndicator {
    private val manager = MatchManager
    private val matchScoreBoard = Bukkit.getScoreboardManager().newScoreboard

    fun updateMatchScoreBoard() {
        val obj = matchScoreBoard.getObjective("side") ?: matchScoreBoard.registerNewObjective(
            "side",
            "dummy",
            Component.text("${ChatColor.AQUA}====LeftCrafterDead====")
        )
        obj.displaySlot = DisplaySlot.SIDEBAR
        val lines = matchBoardLine()
        setScores(lines, matchScoreBoard, obj)
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
    }

    private fun matchBoardLine(): ArrayList<String> {
        val messageList = ArrayList<String>()
        messageList.add("")
        messageList.add("Stage:  ${manager.campaign.campaignTitle}")
        messageList.add("Difficulty:  ${manager.campaign.determiningDifficulty()}")
        messageList.add("${ChatColor.WHITE}Survivor:  ${ChatColor.GOLD}${manager.numberOfSurvivors()}")
        messageList.add("")

        for (lcdPlayer in manager.matchPlayer) {
            if (!lcdPlayer.isSurvivor){
                continue
            }
            val name = lcdPlayer.player.name
            val healthScale = lcdPlayer.player.healthScale
            val health = lcdPlayer.player.health
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
        messageList.add("")
        messageList.add("${ChatColor.AQUA}${player.name}")
        messageList.add("${ChatColor.WHITE}レベル:  ${ChatColor.AQUA}${player.level}")
        messageList.add("${ChatColor.WHITE}次のレベルまで  ${ChatColor.YELLOW}${player.expToLevel}Exp")
        messageList.add("${ChatColor.WHITE}Perk:  ${ChatColor.LIGHT_PURPLE}${lcdPlayer.perk.perkType.perkName}")

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