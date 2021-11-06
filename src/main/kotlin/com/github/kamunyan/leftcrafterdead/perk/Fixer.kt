package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Concussion
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.AssaultRifle
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class Fixer : Perk(PerkType.FIXER) {
    override fun perkSymbolMaterial(): Material {
        return Material.HONEYCOMB
    }

    override fun getGrenade(): Grenade {
        return Concussion()
    }

    override fun firstPrimaryWeapon(): PrimaryWeapon {
        return AssaultRifle("Mini21")
    }

    override fun perkGadgetItem(): ItemStack {
        return ItemMetaUtil.generateMetaItem(
            Material.HEART_OF_THE_SEA,
            "${ChatColor.GOLD}Armor Boost",
            110,
            listOf(
                "半径10mにいるプレイヤーに、",
                "一時的にアーマーを付与する"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        startGadgetStartCoolDown(lcdPlayer)
        val armorAmount = 6
        val bossBar = Bukkit.createBossBar("Armor Boost", BarColor.YELLOW, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG)
        lcdPlayer.player.location.getNearbyPlayers(10.0).forEach { player ->
            bossBar.addPlayer(player)
            bossBar.isVisible = true
            player.absorptionAmount = player.absorptionAmount + armorAmount
            object : BukkitRunnable() {
                var timeLeft = 1.0
                override fun run() {
                    if (timeLeft <= 0) {
                        player.absorptionAmount = player.absorptionAmount - armorAmount
                        bossBar.isVisible = false
                        bossBar.removePlayer(player)
                        cancel()
                        return
                    }
                    timeLeft -= 0.02
                }
            }.runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 20)
        }
    }

    override val gadgetCoolDown: Int = 5
}