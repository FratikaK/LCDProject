package com.github.kamunyan.leftcrafterdead.util.inventory.trader

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.trader.Trader
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.inventory.DisplayType
import com.github.kamunyan.leftcrafterdead.util.inventory.InventoryDisplay
import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.SecondaryType
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.GrenadeType
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object TraderSecondary : InventoryDisplay() {
    override val displayType: DisplayType
        get() = DisplayType.TRADER_SECONDARY_WEAPON

    override fun buildDisplay(player: Player): Inventory {
        return display
    }

    override fun buildHandlers(): Map<Int, (event: InventoryClickEvent) -> Unit> {
        return indexHandlers
    }

    private val display = Bukkit.createInventory(null, 54, Component.text("セカンダリ・グレネード"))

    init {
        display.setItem(9, ItemMetaUtil.generateMetaItem(Material.BLACK_STAINED_GLASS_PANE, "${ChatColor.AQUA}セカンダリ"))
        display.setItem(27, ItemMetaUtil.generateMetaItem(Material.BLACK_STAINED_GLASS_PANE, "${ChatColor.AQUA}グレネード"))
        val cs = LeftCrafterDead.instance.crackShot
        var index = 10
        SecondaryType.values().forEach {
            if (index == 18) {
                index++
            }
            val item = LeftCrafterDead.instance.crackShot.generateWeapon(it.weaponTitle)
            val loreList = LCDWeapon.weaponInformation(it.weaponTitle)
            loreList.add(Component.text("${ChatColor.GOLD}$${it.coin}"))
            loreList.add(Component.text("${ChatColor.YELLOW}カテゴリ: ${it.category.categoryName}"))
            item.lore(loreList)
            display.setItem(index, item)
            indexHandlers[index] =
                fun(e: InventoryClickEvent) { Trader.tradeSecondaryWeapon(e.whoClicked as Player, it) }
            index++
        }

        index = 28
        GrenadeType.values().forEach {
            val item = cs.generateWeapon(it.weaponTitle)
            item.lore(
                listOf(
                    Component.text("${ChatColor.GOLD}\$${Grenade.money}")
                )
            )
            display.setItem(index, item)

            indexHandlers[index] = fun(e: InventoryClickEvent) { Trader.tradeGrenade(e.whoClicked as Player, it) }
            index++
        }

        display.setItem(45, backItem)
        display.setItem(53, ItemMetaUtil.generateMetaItem(Material.DIAMOND_BLOCK, "${ChatColor.GREEN}プライマリ"))
        indexHandlers[45] = fun(e: InventoryClickEvent) {
            switchDisplay(e.whoClicked as Player, DisplayType.TRADER_MENU)
        }
        indexHandlers[53] = fun(e: InventoryClickEvent) {
            switchDisplay(e.whoClicked as Player, DisplayType.TRADER_PRIMARY_WEAPON)
        }
    }
}