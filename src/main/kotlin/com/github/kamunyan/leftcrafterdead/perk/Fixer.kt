package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Concussion
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.AssaultRifle
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import java.awt.Color

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
                "最大体力の60%のアーマーを付与する"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        startGadgetStartCoolDown(lcdPlayer)
        lcdPlayer.player.location.getNearbyPlayers(10.0 * lcdPlayer.statusData.mainGadgetAddPerformance)
            .forEach { player ->
                player.playSound(player.location, Sound.ITEM_FIRECHARGE_USE, 2f, 1f)
                ParticleBuilder(ParticleEffect.REDSTONE, player.location.clone())
                    .setColor(Color.YELLOW)
                    .setOffset(3f, 3f, 3f)
                    .setAmount(200)
                    .display()
                val armorAmount = (player.healthScale * 0.6) * lcdPlayer.statusData.mainGadgetAddPerformance
                player.absorptionAmount = player.absorptionAmount + armorAmount
                if (MatchManager.getLCDPlayer(player).statusData.armorLimit < player.absorptionAmount){
                    player.absorptionAmount = MatchManager.getLCDPlayer(player).statusData.armorLimit
                }
                player.sendMessage("${ChatColor.GOLD}${lcdPlayer.player.name}からアーマーを${armorAmount}ポイント付与された！")
            }
    }

    override val gadgetCoolDown: Int = 5
}