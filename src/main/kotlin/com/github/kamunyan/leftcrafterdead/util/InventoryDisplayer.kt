package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.configs.SkillTreeConfig
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import java.util.*

object InventoryDisplayer {
    private val util = ItemMetaUtil

    fun mainMenuDisplay(): Inventory {
        val inventory = Bukkit.createInventory(null, 54, "Main Menu")
        val perk = util.generateMetaItem(Material.END_CRYSTAL, "${ChatColor.LIGHT_PURPLE}Perk", 60)
        val skill = util.generateMetaItem(Material.OAK_SAPLING, "${ChatColor.GOLD}スキルツリー", 61)
        val subGadget = util.generateMetaItem(Material.FURNACE_MINECART, "${ChatColor.GREEN}サブガジェット", 62)
        inventory.setItem(20, perk)
        inventory.setItem(21, skill)
        inventory.setItem(22, subGadget)
        return inventory
    }

    fun selectPerkDisplay(): Inventory {
        val inventory = Bukkit.createInventory(null, 9, "${ChatColor.DARK_PURPLE}Select Perk")

        val gunslinger = util.generateMetaItem(
            Material.CROSSBOW,
            "${ChatColor.AQUA}Gunslinger",
            100,
            listOf("ファイアレート武器を一時的に強化する","ガジェットを所持する")
        )
        val medic = util.generateMetaItem(
            Material.REDSTONE,
            "${ChatColor.AQUA}Medic",
            100,
            listOf("自身、味方の体力を回復させる","ガジェットを所持する")
        )
        val fixer = util.generateMetaItem(
            Material.HONEYCOMB,
            "${ChatColor.AQUA}Fixer",
            100,
            listOf("アーマー回復を強化するガジェットを所持する")
        )
        val exterminator = util.generateMetaItem(
            Material.NETHERITE_INGOT,
            "${ChatColor.AQUA}Exterminator",
            100,
            listOf("敵集団の行動を阻害するガジェットを所持する")
        )

        inventory.setItem(0, gunslinger)
        inventory.setItem(1, medic)
        inventory.setItem(2, fixer)
        inventory.setItem(3, exterminator)

        return inventory
    }

    fun skillTreeTypeSelectDisplay(lcdPlayer: LCDPlayer): Inventory {
        val inventory = Bukkit.createInventory(null, 9, "Select Skill Tree")
        val tree = lcdPlayer.skillTree
        var modelData = 500
        var index = 0
        tree.forEach { (t, u) ->
            val item = util.generateMetaItem(
                t.material,
                "${ChatColor.AQUA}${t.name}",
                modelData,
                listOf("使用ポイント ${ChatColor.YELLOW}${u.useSkillPoint}")
            )
            inventory.setItem(index, item)
            modelData++
            index++
        }
        return inventory
    }

    fun skillBuildDisplay(lcdPlayer: LCDPlayer, skillType: SkillType): Inventory {
        val inventory = Bukkit.createInventory(null, 54, skillType.name)
        val returnItem = util.generateMetaItem(Material.OAK_DOOR, "スキルツリー選択へ戻る", 510)
        val skillResetItem = util.generateMetaItem(Material.REDSTONE_BLOCK, "スキルツリーリセット", skillType.skillItemData)
        val useSkillPointInfo = util.generateMetaItem(
            Material.EMERALD_BLOCK,
            "${ChatColor.AQUA}使用ポイント ${ChatColor.GOLD}${lcdPlayer.skillTree[skillType]!!.useSkillPoint}"
        )
        val skillInfoMap = SkillTreeConfig.skillDataHashMap[skillType]
        if (skillInfoMap == null) {
            val item = util.generateMetaItem(Material.BEDROCK, "${ChatColor.RED}スキルツリーデータがありません！")
            lcdPlayer.skillTree[skillType]!!.skillMap.forEach { t, _ ->
                inventory.setItem(t, item)
            }
            return inventory
        }
        val skillTree = lcdPlayer.skillTree[skillType]!!

        lcdPlayer.skillTree[skillType]!!.skillMap.forEach { (index, isGet) ->
            val name: String
            var lore: ArrayList<String>? = null
            val type: String
            var material: Material = Material.BEDROCK
            val isReachedUseSkillPoint = skillTree.useSkillPoint >= SkillTree.requireUnlockSkillTree[index]!!
            val skillHashMap = SkillTreeConfig.skillDataHashMap[skillType]
            val infoHashMap = skillHashMap?.get(index)
            if (infoHashMap != null) {
                name = "${ChatColor.AQUA}${infoHashMap["name"]}"
                if (infoHashMap["info"] != null) {
                    val string = infoHashMap["info"]!!.replace(" ", "")
                    val info = string.split("|")
                    lore = ArrayList()
                    info.forEach { str -> lore.add(str) }
                    lore.add("${ChatColor.AQUA}必要スキルポイント ${ChatColor.GOLD}${SkillTree.requireSkillPoint[index]}")
                    if (!isReachedUseSkillPoint) {
                        lore.add("${ChatColor.RED}アンロックに必要なポイント ${ChatColor.GOLD}${SkillTree.requireUnlockSkillTree[index]}")
                    }
                }
                if (isReachedUseSkillPoint) {
                    if (isGet) {
                        if (infoHashMap["type"] != null) {
                            type = infoHashMap["type"]!!.uppercase(Locale.getDefault())
                            if (Material.getMaterial(type) != null) {
                                material = Material.getMaterial(type)!!
                            }
                        }
                    } else {
                        material = Material.BLUE_STAINED_GLASS_PANE
                    }
                }
                val item = util.generateMetaItem(material, name, skillType.skillItemData, lore)
                inventory.setItem(
                    index,
                    item
                )
            }
        }
        inventory.setItem(45, returnItem)
        inventory.setItem(36, skillResetItem)
        inventory.setItem(27, useSkillPointInfo)
        return inventory
    }
}