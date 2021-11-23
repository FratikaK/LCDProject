package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.configs.SkillTreeConfig
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.type.MasterMind
import com.github.kamunyan.leftcrafterdead.subgadget.SubGadget
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.primary.AssaultRifle
import com.github.kamunyan.leftcrafterdead.weapons.primary.Shotgun
import com.github.kamunyan.leftcrafterdead.weapons.primary.SubMachineGun
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayList

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
            listOf("銃の射撃に特化したPerk", "敵性Mobに追加ダメージを与えることが出来る")
        )
        val medic = util.generateMetaItem(
            Material.REDSTONE,
            "${ChatColor.AQUA}Medic",
            100,
            listOf("チームを治癒で支えるPerk", "自身、味方の体力を回復させる武器、アイテムを所持する")
        )
        val fixer = util.generateMetaItem(
            Material.HONEYCOMB,
            "${ChatColor.AQUA}Fixer",
            100,
            listOf("味方をサポートするPerk", "敵、味方に対して足止め、補助をするアイテムを所持する")
        )
        val exterminator = util.generateMetaItem(
            Material.NETHERITE_INGOT,
            "${ChatColor.AQUA}Exterminator",
            100,
            listOf("対集団に特化したPerk", "敵集団に対して有用なアイテムを所持する")
        )

        inventory.setItem(0, gunslinger)
        inventory.setItem(1, medic)
        inventory.setItem(2, fixer)
        inventory.setItem(3, exterminator)

        return inventory
    }

    fun merchantWeaponSelectDisplay(): Inventory {
        val inventory = Bukkit.createInventory(null, 9, "${ChatColor.GREEN}Select Weapon Type")
        val primary = util.generateMetaItem(Material.WOODEN_PICKAXE, "${ChatColor.AQUA}プライマリ", 200)
        val secondary = util.generateMetaItem(Material.WOODEN_HOE, "${ChatColor.AQUA}セカンダリ", 201)
        val grenade = util.generateMetaItem(Material.FIREWORK_STAR, "${ChatColor.AQUA}グレネード", 202)
        inventory.setItem(0, primary)
        inventory.setItem(1, secondary)
        inventory.setItem(2, grenade)
        return inventory
    }

    fun primaryDisplay(): Inventory {
        val inventory = weaponDisplay("プライマリ")
        val assault = util.generateMetaItem(Material.BLACK_STAINED_GLASS, "${ChatColor.GREEN}アサルトライフル")
        val sub = util.generateMetaItem(Material.BLACK_STAINED_GLASS, "${ChatColor.GREEN}サブマシンガン")
        val shotgun = util.generateMetaItem(Material.BLACK_STAINED_GLASS, "${ChatColor.GREEN}ショットガン")
        inventory.setItem(0, assault)
        inventory.setItem(9, sub)
        inventory.setItem(18, shotgun)
        var index = 1
        GunCategory.ASSAULT_RIFLE.getWeaponList().forEach {
            val weapon = AssaultRifle(it)
            val type = weapon.getWeaponItemStack()?.type
            if (type != null) {
                val item = util.generateMetaItem(type, it, 210, weapon.weaponDataList())
                inventory.setItem(index, item)
                index += 1
            }
        }
        index = 10
        GunCategory.SUB_MACHINE_GUN.getWeaponList().forEach {
            val weapon = SubMachineGun(it)
            val type = weapon.getWeaponItemStack()?.type
            if (type != null) {
                val item = util.generateMetaItem(type, it, 210, weapon.weaponDataList())
                inventory.setItem(index, item)
                index += 1
            }
        }
        index = 19
        GunCategory.SHOTGUN.getWeaponList().forEach {
            val weapon = Shotgun(it)
            val type = weapon.getWeaponItemStack()?.type
            if (type != null) {
                val item = util.generateMetaItem(type, it, 210, weapon.weaponDataList())
                inventory.setItem(index, item)
                index += 1
            }
        }
        return inventory
    }

    private fun weaponDisplay(name: String): Inventory {
        val inventory = Bukkit.createInventory(null, 54, "${ChatColor.AQUA}$name")
        val exit = util.generateMetaItem(Material.REDSTONE_BLOCK, "${ChatColor.RED}戻る", 198)
        val next = util.generateMetaItem(Material.EMERALD_BLOCK, "${ChatColor.GREEN}次の武器カテゴリ", 199)
        inventory.setItem(45, exit)
        inventory.setItem(53, next)
        return inventory
    }

    fun subGadgetSlotSelectDisplay(lcdPlayer: LCDPlayer, itemStack: ItemStack): Inventory {
        val inventory = Bukkit.createInventory(null, 9, "Select Slot")
        val exit = util.generateMetaItem(Material.OAK_DOOR, "${ChatColor.RED}戻る", 62)
        inventory.setItem(0, exit)
        inventory.setItem(8, itemStack)
        lcdPlayer.subGadget.forEach { (t, u) ->
            if (u != null) {
                val gadget = SubGadget.getSubGadget(u)
                val meta = gadget.generateItemStack().itemMeta
                val item = ItemStack(gadget.material)
                meta.setCustomModelData(90 + t)
                item.itemMeta = meta
                inventory.setItem(t - 2, item)
            } else {
                val item =
                    util.generateMetaItem(Material.BLACK_STAINED_GLASS_PANE, "${ChatColor.BOLD}アイテムがセットされていません", 90 + t)
                inventory.setItem(t - 2, item)
            }
        }
        return inventory
    }

    fun selectFirstSubGadgetDisplay(): Inventory {
        val inventory = Bukkit.createInventory(null, 9, "サブガジェット")
        val exit = util.generateMetaItem(Material.OAK_DOOR, "${ChatColor.RED}戻る", 91)
        inventory.setItem(0, exit)
        SubGadget.selectGadgetDisplayItemMap.forEach { (t, u) ->
            val gadget = SubGadget.getSubGadget(u)
            val meta = gadget.generateItemStack().itemMeta
            meta.setCustomModelData(92)
            meta.lore(listOf())
            val item = ItemStack(gadget.material)
            item.itemMeta = meta
            inventory.setItem(t, item)
        }
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
            var name = ""
            var lore: ArrayList<String>? = null
            var type = ""
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