package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.Primary
import com.github.kamunyan.leftcrafterdead.weapons.PrimaryType
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Concussion
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import java.awt.Color

class Fixer : Perk(PerkType.FIXER) {
    override fun perkSymbolMaterial(): Material {
        return Material.HONEYCOMB
    }

    override fun getGrenade(): Grenade {
        return Concussion
    }

    override fun firstPrimaryWeapon(): Primary {
        return Primary(PrimaryType.M16)
    }

    override fun perkGadgetItem(): ItemStack {
        return ItemMetaUtil.generateMetaItem(
            Material.HEART_OF_THE_SEA,
            "${ChatColor.GOLD}Armor Boost",
            110,
            listOf(
                "半径10mにいるプレイヤーに",
                "一時的な体力を10付与する"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        super.gadgetRightInteract(lcdPlayer)
        val amount = 7 + (3 * lcdPlayer.statusData.mainGadgetAddPerformance)
        lcdPlayer.player.location.getNearbyPlayers(10.0 * lcdPlayer.statusData.mainGadgetAddPerformance)
            .forEach { player ->
                player.playSound(player.location, Sound.ITEM_FIRECHARGE_USE, 2f, 1f)
                ParticleBuilder(ParticleEffect.REDSTONE, player.location.clone())
                    .setColor(Color.YELLOW)
                    .setOffset(3f, 3f, 3f)
                    .setAmount(200)
                    .display()
                player.absorptionAmount += amount
            }
    }

    override val gadgetCoolDown: Int = 50
}