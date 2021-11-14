package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.primary.AssaultRifle
import com.github.kamunyan.leftcrafterdead.weapons.primary.SubMachineGun
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.Inventory

object InventoryDisplayer {
    private val util = ItemMetaUtil

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
        return inventory
    }

    private fun weaponDisplay(name: String): Inventory {
        val inventory = Bukkit.createInventory(null, 54, "${ChatColor.AQUA}$name")
        val exit = util.generateMetaItem(Material.REDSTONE_BLOCK, "${ChatColor.RED}戻る", 198)
        val next = util.generateMetaItem(Material.EMERALD_BLOCK, "${ChatColor.GREEN}次の武器カテゴリ", 199)
        inventory.setItem(45, exit)
        inventory.setItem(52, next)
        return inventory
    }
}