package com.github.kamunyan.leftcrafterdead.util.inventory.trader

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.trader.Trader
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.inventory.DisplayType
import com.github.kamunyan.leftcrafterdead.util.inventory.InventoryDisplay
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.PrimaryType
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object TraderPrimary : InventoryDisplay() {
    override val displayType: DisplayType = DisplayType.TRADER_PRIMARY_WEAPON

    override fun buildDisplay(player: Player): Inventory {
        return display
    }

    override fun buildHandlers(): Map<Int, (event: InventoryClickEvent) -> Unit> {
        return indexHandlers
    }

    private val display = Bukkit.createInventory(null, 54, Component.text("プライマリ武器"))

    init {
        val weaponShopHashMap = LinkedHashMap<GunCategory, List<PrimaryType>>()
        val assault = ArrayList<PrimaryType>()
        val shotgun = ArrayList<PrimaryType>()
        val sub = ArrayList<PrimaryType>()
        val sniper = ArrayList<PrimaryType>()
        val lmg = ArrayList<PrimaryType>()
        PrimaryType.values().forEach {
            when (it.category) {
                GunCategory.ASSAULT_RIFLE -> assault.add(it)
                GunCategory.SHOTGUN -> shotgun.add(it)
                GunCategory.SUB_MACHINE_GUN -> sub.add(it)
                GunCategory.SNIPER -> sniper.add(it)
                GunCategory.LMG -> lmg.add(it)
                else -> {}
            }
        }
        weaponShopHashMap[GunCategory.ASSAULT_RIFLE] = assault
        weaponShopHashMap[GunCategory.SHOTGUN] = shotgun
        weaponShopHashMap[GunCategory.SUB_MACHINE_GUN] = sub
        weaponShopHashMap[GunCategory.SNIPER] = sniper
        weaponShopHashMap[GunCategory.LMG] = lmg
        val indexes = listOf(0, 9, 18, 27, 36, 45)
        var index = 0

        val weaponShopMap = HashMap<Int, PrimaryType>()

        val tradeHandler = fun(event: InventoryClickEvent) {
            println("ぷれいまりとれーど！")
            val primary = weaponShopMap[event.slot] ?: return
            println("せいこうした！")
            Trader.tradePrimaryWeapon(event.whoClicked as Player, primary)
        }

        weaponShopHashMap.forEach { (category, list) ->
            var slot = indexes[index]
            display.setItem(
                slot,
                ItemMetaUtil.generateMetaItem(
                    Material.BLACK_STAINED_GLASS_PANE,
                    "${ChatColor.AQUA}${category.categoryName}"
                )
            )
            slot += 1
            list.forEach { it ->
                if (slot > indexes[index + 1]) {
                    return@forEach
                }
                val item = LeftCrafterDead.instance.crackShot.generateWeapon(it.weaponTitle)
                val loreList = LCDWeapon.weaponInformation(it.weaponTitle)
                loreList.add(Component.text("${ChatColor.GOLD}$${it.coin}"))
                loreList.add(Component.text("${ChatColor.YELLOW}カテゴリ: ${it.category.categoryName}"))
                item.lore(loreList)
                display.setItem(slot, item)
                weaponShopMap[slot] = it
                indexHandlers[slot] = tradeHandler
                slot++
            }
            index++
        }

        display.setItem(45, backItem)
        display.setItem(53, ItemMetaUtil.generateMetaItem(Material.EMERALD_BLOCK, "${ChatColor.GREEN}セカンダリ、グレネード"))
        indexHandlers[45] = fun(e: InventoryClickEvent) {
            switchDisplay(e.whoClicked as Player, DisplayType.TRADER_MENU)
        }
        indexHandlers[53] = fun(e: InventoryClickEvent) {
            switchDisplay(e.whoClicked as Player, DisplayType.TRADER_SECONDARY_WEAPON)
        }
    }
}