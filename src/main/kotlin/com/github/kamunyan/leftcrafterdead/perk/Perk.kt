package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

abstract class Perk(val perkType: PerkType) {
    abstract fun perkSymbolMaterial(): Material
    abstract fun getGrenade(): Grenade
    abstract fun firstPrimaryWeapon(): PrimaryWeapon
    abstract fun perkGadgetItem(): ItemStack
    abstract val gadgetCoolDown: Int

    fun setFirstWeapon(lcdPlayer: LCDPlayer) {
        lcdPlayer.primary = firstPrimaryWeapon()
        lcdPlayer.primary.sendWeapon(lcdPlayer.player)
        lcdPlayer.secondaryWeapon.sendWeapon(lcdPlayer.player)
        getGrenade().sendGrenade(lcdPlayer.player, lcdPlayer.statusData.maxGrenade)
        setGadget(lcdPlayer)
    }

    fun setSymbolItem(lcdPlayer: LCDPlayer) {
        val inventory = lcdPlayer.player.inventory
        val material = perkSymbolMaterial()
        inventory.setItem(8, ItemMetaUtil.generateMetaItem(material, "${ChatColor.AQUA}${perkType.perkName}"))
    }

    fun setGadget(lcdPlayer: LCDPlayer) {
        val inventory = lcdPlayer.player.inventory
        inventory.setItem(3, perkGadgetItem())
    }

    open fun gadgetRightInteract(lcdPlayer: LCDPlayer){
        startGadgetStartCoolDown(lcdPlayer)
    }

    fun startGadgetStartCoolDown(lcdPlayer: LCDPlayer) {
        object : BukkitRunnable() {
            var timeLeft = (gadgetCoolDown * lcdPlayer.statusData.addMainGadgetCoolDown).toInt()
            override fun run() {
                if (!lcdPlayer.isSurvivor || !lcdPlayer.isMatchPlayer){
                    this.cancel()
                    return
                }
                if (timeLeft <= 0) {
                    setGadget(lcdPlayer)
                    this.cancel()
                    return
                }
                sendCoolDownItem(lcdPlayer, timeLeft)
                timeLeft--
            }
        }.runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 20)
    }

    fun sendCoolDownItem(lcdPlayer: LCDPlayer, timeLeft: Int) {
        val inventory = lcdPlayer.player.inventory
        val coolDownItem = ItemMetaUtil.generateMetaItem(
            Material.WHITE_DYE,
            "${ChatColor.GREEN}再使用可能まで${ChatColor.RED}${timeLeft}${ChatColor.GREEN}秒",
            111
        )
        inventory.setItem(3, coolDownItem)
    }
}