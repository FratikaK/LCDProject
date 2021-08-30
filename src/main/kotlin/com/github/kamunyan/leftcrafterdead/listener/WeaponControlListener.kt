package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.weapons.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import com.shampaggon.crackshot.CSUtility
import com.shampaggon.crackshot.events.WeaponPreShootEvent
import com.shampaggon.crackshot.events.WeaponReloadEvent
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.scheduler.BukkitRunnable

class WeaponControlListener : Listener {
    private val plugin = LeftCrafterDead.instance

    @EventHandler
    fun onReload(e: WeaponReloadEvent) {
        //reloadSpeedはリロードのはやさ。数値が大きいほど遅くなる
        //reloadDurationはリロード完了までの時間
        plugin.logger.info("${e.reloadDuration}")
        plugin.logger.info("${e.reloadSpeed}")

        val crackShot = CSUtility()
        plugin.logger.info("${crackShot.handle.getInt("${e.weaponTitle}.Reload.Reload_Amount")}")

        //TODO 仮の処理であるため、今後プライマリ、セカンダリに分けて対応出来るようにする
        val weapon = e.player.inventory.getItem(0)?.clone()
        val type = weapon?.type
        val weaponMeta = e.player.inventory.getItem(0)!!.itemMeta
        val weaponName = weaponMeta.displayName
        plugin.logger.info(weaponName)
        object : BukkitRunnable() {
            var reloadDuration = (e.reloadDuration * e.reloadSpeed).toInt()
            override fun run() {
                if (e.player.itemInHand.type != type) {
                    plugin.logger.info("もちかえやがって...")
                    e.player.inventory.setItem(0, weapon)
                    cancel()
                    return
                }
                if (reloadDuration <= 0) {
                    PrimaryWeapon("AK12", WeaponType.ASSAULT_RIFLE).sendWeapon(e.player)
                    cancel()
                    return
                }
                val meta = e.player.inventory.getItem(0)?.itemMeta
                meta?.setDisplayName("$weaponName ${ChatColor.RED}[RELOADING... $reloadDuration]")
                e.player.inventory.getItem(0)?.itemMeta = meta
                reloadDuration -= 1
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1)
    }

    /**
     * 武器を撃つときに呼ばれる処理
     * リロード中の射撃を制限する
     */
    @EventHandler
    fun onShoot(e: WeaponPreShootEvent) {
        if (e.player.inventory.getItem(0)!!.itemMeta.displayName.contains("RELOADING")) {
            e.isCancelled = true
        }
    }

    /**
     * 武器スロットのインベントリクリックを制限する
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onWeaponClick(e: InventoryClickEvent) {
        if (e.slot == 0 || e.slot == 1) {
            e.isCancelled = true
        }
    }
}