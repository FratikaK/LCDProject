package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import com.github.kamunyan.leftcrafterdead.weapons.WeaponUtil
import com.shampaggon.crackshot.CSUtility
import com.shampaggon.crackshot.events.*
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
    private val manager = MatchManager

    @EventHandler
    fun onReload(e: WeaponReloadEvent) {
        //reloadSpeedはリロードのはやさ。数値が大きいほど遅くなる
        //reloadDurationはリロード完了までの時間
        val lcdPlayer = manager.getLCDPlayer(e.player)
        val data = lcdPlayer.statusData
        e.reloadSpeed += lcdPlayer.statusData.reloadSpeedAcceleration
        if (GunCategory.ASSAULT_RIFLE.getWeaponList()
                .contains(e.weaponTitle) || GunCategory.SUB_MACHINE_GUN.getWeaponList().contains(e.weaponTitle)
        ) {
            e.reloadSpeed += data.fireRateReloadSpeedAcceleration
        }
        if (WeaponUtil.getGunCategory(e.weaponTitle) == GunCategory.SHOTGUN) {
            e.reloadSpeed += lcdPlayer.statusData.shotgunReloadSpeedAcceleration
        }
        if (data.specialSkillTypes.contains(SpecialSkillType.RUNNING_FROM_DEATH)){
            if (e.player.healthScale <= 10){
                e.reloadSpeed += -0.15
            }
        }
        val weapon: ItemStack?
        val weaponType = WeaponUtil.getWeaponType(CSUtility().generateWeapon(e.weaponTitle).type, e.player)
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
    fun onFireRate(e: WeaponFireRateEvent) {
        e.fireRate += manager.getLCDPlayer(e.player).statusData.rateAcceleration
        if (e.fireRate > 16) {
            e.fireRate = 16
        }
    }

    @EventHandler
    fun onPreShoot(e: WeaponPreShootEvent) {
        val lcdPlayer = manager.getLCDPlayer(e.player)
        var addSpread = 0.0
        addSpread += lcdPlayer.statusData.addBulletSpread
        val category = WeaponUtil.getGunCategory(e.weaponTitle)
        if (category == GunCategory.ASSAULT_RIFLE || category == GunCategory.SUB_MACHINE_GUN) {
            addSpread += lcdPlayer.statusData.fireRateAddBulletSpread
        }
        if (category == GunCategory.HANDGUN || category == GunCategory.AKIMBO){
            addSpread += lcdPlayer.statusData.handgunAddBulletSpread
        }
        if (lcdPlayer.statusData.specialSkillTypes.contains(SpecialSkillType.AKIMBO)){
            addSpread += -1.5
        }

        if (e.bulletSpread + addSpread < 0) {
            e.bulletSpread = 0.0
        } else {
            e.bulletSpread += addSpread
        }
        println("弾拡散率 ${e.bulletSpread}")
    }

    @EventHandler
    fun onReloadComplete(e: WeaponReloadCompleteEvent) {
    }

    @EventHandler
    fun onWeaponCapacity(e: WeaponCapacityEvent) {
        val lcdPlayer = manager.getLCDPlayer(e.player)
        e.capacity = (e.capacity * lcdPlayer.statusData.magazineAmountMultiplier).toInt()
        val category = WeaponUtil.getGunCategory(e.weaponTitle)
        if (category == GunCategory.HANDGUN || category == GunCategory.AKIMBO){
            e.capacity += lcdPlayer.statusData.addHandgunMagazine
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