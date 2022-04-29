package com.github.kamunyan.leftcrafterdead.util.inventory.trader

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.trader.Trader
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.inventory.DisplayType
import com.github.kamunyan.leftcrafterdead.util.inventory.InventoryDisplay
import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponUtil
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object TraderEnhancement : InventoryDisplay() {
    override val displayType: DisplayType = DisplayType.TRADER_ENHANCEMENT

    override fun buildDisplay(player: Player): Inventory {
        val inventory = Bukkit.createInventory(null, 9, Component.text("‘•”õ‹­‰»"))
        val lcdPlayer = MatchManager.getLCDPlayer(player)
        val cs = LeftCrafterDead.instance.crackShot
        val primary = ItemMetaUtil.generateMetaItem(
            cs.generateWeapon(lcdPlayer.primary.weaponTitle).type,
            lcdPlayer.primary.weaponTitle
        )
        val primaryLore = LCDWeapon.weaponInformation(lcdPlayer.primary.weaponTitle)
        primaryLore.add(Component.text("${ChatColor.AQUA}Lv${lcdPlayer.primary.weaponLevel}"))
        primaryLore.add(Component.text("${ChatColor.AQUA}‹­‰»”ï—p ${ChatColor.GOLD}$${lcdPlayer.primary.weapon.coin * 2}"))
        primary.lore(primaryLore)
        inventory.setItem(2, primary)
        var index = 4
        lcdPlayer.subGadget.forEach { (_, gadget) ->
            if (gadget != null) {
                val item = ItemMetaUtil.generateMetaItem(
                    gadget.material,
                    "${ChatColor.GREEN}${gadget.subGadgetName}"
                )
                item.lore(
                    listOf(
                        Component.text("${ChatColor.AQUA}Lv${gadget.subGadgetLevel}"),
                        Component.text("${ChatColor.AQUA}‹­‰»”ï—p ${ChatColor.GOLD}$500")
                    )
                )
                inventory.setItem(index, item)
            }
            index++
        }
        inventory.setItem(8, backItem)
        return inventory
    }

    override fun buildHandlers(): Map<Int, (event: InventoryClickEvent) -> Unit> {
        val handlers = HashMap<Int, (event: InventoryClickEvent) -> Unit>()
        handlers[2] = fun(e: InventoryClickEvent) { Trader.levelUpPrimary(e.whoClicked as Player) }
        handlers[4] = fun(e: InventoryClickEvent) { Trader.levelUpSubGadget(e.whoClicked as Player, e.slot + 1) }
        handlers[5] = fun(e: InventoryClickEvent) { Trader.levelUpSubGadget(e.whoClicked as Player, e.slot + 1) }
        handlers[6] = fun(e: InventoryClickEvent) { Trader.levelUpSubGadget(e.whoClicked as Player, e.slot + 1) }
        handlers[8] = fun(e: InventoryClickEvent) { switchDisplay(e.whoClicked as Player, DisplayType.TRADER_MENU) }
        return handlers
    }
}