package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import com.github.kamunyan.leftcrafterdead.weapons.WeaponUtil
import com.shampaggon.crackshot.events.WeaponPreShootEvent
import com.shampaggon.crackshot.events.WeaponReloadEvent
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class WeaponControlListener : Listener {
    private val plugin = LeftCrafterDead.instance

    @EventHandler
    fun onReload(e: WeaponReloadEvent) {
        //reloadSpeedはリロードのはやさ。数値が大きいほど遅くなる
        //reloadDurationはリロード完了までの時間
        plugin.logger.info(e.weaponTitle)
        plugin.logger.info("${e.reloadDuration}")
        plugin.logger.info("${e.reloadSpeed}")

        val weapon: ItemStack?
        val weaponType = WeaponUtil.getWeaponType(e.weaponTitle, e.player)
        val weaponSlot = WeaponUtil.getWeaponSlot(weaponType)
        if (weaponType == WeaponType.UNKNOWN || weaponSlot == -1) {
            plugin.logger.info("[onReload]${ChatColor.RED}WeaponTypeを取得出来ませんでした")
            return
        }

        weapon = e.player.inventory.getItem(weaponSlot)?.clone()
        val weaponMeta = e.player.inventory.getItem(weaponSlot)!!.itemMeta
        val weaponName = weaponMeta.displayName
        val type = weapon?.type

        object : BukkitRunnable() {
            var reloadDuration = (e.reloadDuration * e.reloadSpeed).toInt()
            override fun run() {
                try {
                    if (e.player.itemInHand.type != type) {
                        e.player.inventory.setItem(weaponSlot, weapon)
                        cancel()
                        return
                    }

                    if (reloadDuration <= 0) {
                        LCDWeapon(e.weaponTitle, weaponType).sendWeapon(e.player)
                        cancel()
                        return
                    }

                    //武器名の表示を変える
                    val meta = e.player.inventory.getItem(weaponSlot)?.itemMeta
                    meta?.setDisplayName("$weaponName ${ChatColor.RED}[RELOADING... $reloadDuration]")
                    e.player.inventory.getItem(weaponSlot)?.itemMeta = meta

                    reloadDuration -= 1
                } catch (exception: NullPointerException) {
                    e.player.inventory.setItem(weaponSlot, weapon)
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1)
    }

    /**
     * 武器を撃つときに呼ばれる処理
     * リロード中の射撃を制限する
     */
    @EventHandler
    fun onShoot(e: WeaponPreShootEvent) {
        if (e.player.inventory.getItem(0)?.itemMeta?.displayName?.contains("RELOADING") == true
            || e.player.inventory.getItem(1)?.itemMeta?.displayName?.contains("RELOADING") == true
        ) {
            e.isCancelled = true
        }
    }

    /**
     * 武器スロットのインベントリクリックを制限する
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onWeaponClick(e: InventoryClickEvent) {
        if (e.whoClicked.isOp) {
            return
        }
        if (e.slot == 0 || e.slot == 1) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onItemDrop(e: PlayerDropItemEvent) {
        e.isCancelled = true
    }
}