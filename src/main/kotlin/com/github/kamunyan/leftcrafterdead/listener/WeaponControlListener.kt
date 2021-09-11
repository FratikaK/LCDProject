package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import com.github.kamunyan.leftcrafterdead.weapons.WeaponUtil
import com.shampaggon.crackshot.events.WeaponPreShootEvent
import com.shampaggon.crackshot.events.WeaponReloadCompleteEvent
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

        val weapon: ItemStack?
        val weaponType = WeaponUtil.getWeaponType(e.weaponTitle, e.player)
        val weaponSlot = weaponType.getWeaponSlot()
        if (weaponType == WeaponType.UNKNOWN || weaponSlot == -1) {
            plugin.logger.info("[onReload]${ChatColor.RED}WeaponTypeを取得出来ませんでした")
            return
        }

        weapon = e.player.inventory.getItem(weaponSlot)?.clone()
        val type = weapon?.type

        object : BukkitRunnable() {
            var reloadDuration = (e.reloadDuration * e.reloadSpeed).toInt()
            override fun run() {
                try {
                    if (e.player.itemInHand.type != type) {
                        e.player.sendActionBar(" ")
                        cancel()
                        return
                    }

                    if (!WeaponUtil.hasReloadTag(e.player, weaponSlot)) {
                        e.player.sendActionBar(" ")
                        cancel()
                        return
                    }

                    if (reloadDuration <= 0) {
                        e.player.sendActionBar(" ")
                        cancel()
                        return
                    }

                    e.player.sendActionBar("${ChatColor.RED}${e.weaponTitle} [RELOADING... ${reloadDuration}]")
                    reloadDuration -= 1

                } catch (exception: NullPointerException) {
                    e.player.inventory.setItem(weaponSlot, weapon)
                    e.player.sendActionBar(" ")
                    cancel()
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1)
    }

    @EventHandler
    fun onReloadComplete(e: WeaponReloadCompleteEvent) {
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