package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.FlagGrenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.AssaultRifle
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class Gunslinger : Perk(PerkType.GUNSLINGER) {
    override fun perkSymbolMaterial(): Material {
        return Material.CROSSBOW
    }

    override fun getGrenade(): Grenade {
        return FlagGrenade()
    }

    override fun firstPrimaryWeapon(): PrimaryWeapon {
        return AssaultRifle("AK12")
    }

    override fun perkGadgetItem(): ItemStack {
        return ItemMetaUtil.generateMetaItem(
            Material.TRIPWIRE_HOOK,
            "${ChatColor.GOLD}Weapon Boost",
            110,
            listOf(
                "20秒間フルオート武器の", "発射レート倍率を3段階",
                "上昇させ、リロードのスピードを50%上昇させる"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        startGadgetStartCoolDown(lcdPlayer)
        val rateQuantity = (3 * lcdPlayer.statusData.mainGadgetAddPerformance).toInt()
        val reloadSpeed = 0.5 * lcdPlayer.statusData.mainGadgetAddPerformance
        lcdPlayer.statusData.rateAcceleration += rateQuantity
        lcdPlayer.statusData.reloadSpeedAcceleration -= reloadSpeed
        val bossBar = Bukkit.createBossBar("レート上昇中", BarColor.PURPLE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG)
        bossBar.addPlayer(lcdPlayer.player)
        bossBar.isVisible = true
        var timeLeft = 1.0
        object : BukkitRunnable() {
            override fun run() {
                if (timeLeft <= 0.0) {
                    bossBar.removeAll()
                    bossBar.isVisible = false
                    lcdPlayer.statusData.rateAcceleration -= rateQuantity
                    lcdPlayer.statusData.reloadSpeedAcceleration += reloadSpeed
                    cancel()
                    return
                }
                bossBar.progress = timeLeft
                timeLeft -= 0.05
            }
        }.runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 20)
        lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
        lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f)
        lcdPlayer.player.sendMessage("${ChatColor.GOLD}レートとリロード速度が上昇！")
    }

    override val gadgetCoolDown: Int = 40
}