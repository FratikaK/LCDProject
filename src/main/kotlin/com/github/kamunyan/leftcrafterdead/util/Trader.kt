package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.weapons.AmmoCategory
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object Trader {
    private val manager = MatchManager
    val weaponShopList = linkedMapOf(
        GunCategory.ASSAULT_RIFLE to LinkedHashMap<String, Int>(),
        GunCategory.SUB_MACHINE_GUN to LinkedHashMap(),
        GunCategory.SHOTGUN to LinkedHashMap(),
        GunCategory.HANDGUN to LinkedHashMap(),
        GunCategory.GRENADE to LinkedHashMap()
    )

    private val u = ItemMetaUtil

    const val DEFAULT_MONEY = 300

    //non item
    private val N = Pair(u.generateMetaItem(Material.BLACK_STAINED_GLASS_PANE, ""), fun(_: Player) {})

    //back item
    private val backItem = u.generateMetaItem(Material.OAK_DOOR, "${ChatColor.RED}戻る")

    private val shopDisplay = Bukkit.createInventory(null, 9, Component.text("Select Category"))
    private val weaponDisplay = Bukkit.createInventory(null, 54, Component.text("Weapons"))
    private val ammoDisplay = Bukkit.createInventory(null, 9, Component.text("弾薬補給"))

    val shopDisplayFuncs = HashMap<Int, Pair<ItemStack, (player: Player) -> Unit>>()

    fun createTraderDisplay() {
        createShopDisplay()
        createWeaponDisplay()
        createAmmoDisplay()
    }

    fun spawnTrader() {}

    fun deleteTrader() {}

    private fun createShopDisplay() {
        val weapon = u.generateMetaItem(Material.NETHERITE_AXE, "${ChatColor.AQUA}武器を購入する")
        val gadget = u.generateMetaItem(Material.TNT_MINECART, "${ChatColor.GREEN}サブガジェットを購入する")
        val ammo = u.generateMetaItem(Material.GOLD_NUGGET, "${ChatColor.YELLOW}弾薬補給")
        val weaponPair = Pair(weapon, fun(p: Player) { p.openInventory(weaponDisplay) })
        val ammoPair = Pair(ammo, fun(p: Player) { p.openInventory(ammoDisplay) })
        val itemArray = arrayOf(N, N, N, weaponPair, N, ammoPair, N, N, N)
        for (i in (0..8)) {
            shopDisplay.setItem(i, itemArray[i].first)
            shopDisplayFuncs[i] = itemArray[i]
        }
    }

    private fun createWeaponDisplay() {

    }

    private fun createAmmoDisplay() {
    }

}