package com.github.kamunyan.leftcrafterdead.skill

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.InventoryDisplayer
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Sound

abstract class SkillTree {
    companion object {
        val requireSkillPoint = linkedMapOf(
            49 to 1,
            38 to 2,
            40 to 2,
            42 to 2,
            29 to 4,
            31 to 4,
            33 to 4,
            20 to 6,
            22 to 6,
            24 to 6,
            11 to 8,
            13 to 8,
            15 to 8,
            2 to 12,
            4 to 12,
            6 to 12
        )

        val requireUnlockSkillTree = linkedMapOf(
            49 to 0,
            38 to 1,
            40 to 1,
            42 to 1,
            29 to 3,
            31 to 3,
            33 to 3,
            20 to 7,
            22 to 7,
            24 to 7,
            11 to 17,
            13 to 17,
            15 to 17,
            2 to 30,
            4 to 40,
            6 to 30
        )
    }

    val skillMap: HashMap<Int, Boolean> = linkedMapOf(
        49 to false,
        38 to false,
        40 to false,
        42 to false,
        29 to false,
        31 to false,
        33 to false,
        20 to false,
        22 to false,
        24 to false,
        11 to false,
        13 to false,
        15 to false,
        2 to false,
        4 to false,
        6 to false
    )

    abstract val skillType: SkillType
    abstract fun setStatusData(data: StatusData)

    fun selectSkill(lcdPlayer: LCDPlayer, index: Int) {
        if (!skillMap.containsKey(index)) return
        if (skillMap[index] == true) {
            lcdPlayer.player.sendMessage(Component.text("${ChatColor.RED}既にスキルを習得しています！"))
            lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.BLOCK_LAVA_POP, 1f, 1f)
            return
        }
        val requirePoint = requireSkillPoint[index]!!
        if (lcdPlayer.skillPoint >= requirePoint) {
            skillMap[index] = true
            lcdPlayer.skillPoint -= requirePoint
            useSkillPoint += requirePoint
            lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 0f)
            lcdPlayer.player.openInventory(InventoryDisplayer.skillBuildDisplay(lcdPlayer, skillType))
        }else{
            lcdPlayer.player.sendMessage(Component.text("${ChatColor.RED}スキルポイントが足りません！"))
            lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.BLOCK_LAVA_POP, 1f, 1f)
        }
    }
    var useSkillPoint = 0
}
