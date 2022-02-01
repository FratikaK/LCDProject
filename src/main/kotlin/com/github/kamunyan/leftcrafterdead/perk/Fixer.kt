package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
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
import org.bukkit.scheduler.BukkitRunnable
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
                "半径10mにいるプレイヤーの",
                "アーマー回復速度を100%上昇させる"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        super.gadgetRightInteract(lcdPlayer)
        lcdPlayer.player.location.getNearbyPlayers(10.0 * lcdPlayer.statusData.mainGadgetAddPerformance)
            .forEach { player ->
                player.playSound(player.location, Sound.ITEM_FIRECHARGE_USE, 2f, 1f)
                ParticleBuilder(ParticleEffect.REDSTONE, player.location.clone())
                    .setColor(Color.YELLOW)
                    .setOffset(3f, 3f, 3f)
                    .setAmount(200)
                    .display()
                val targetLCDPlayer = MatchManager.getLCDPlayer(player)
                val limitArmor = targetLCDPlayer.statusData.armorLimit
                var timeLeft = (10 * lcdPlayer.statusData.mainGadgetAddPerformance).toInt()
                object : BukkitRunnable() {
                    override fun run() {
                        if (targetLCDPlayer.isSurvivor || !MatchManager.isMatch || timeLeft <= 0) {
                            cancel()
                            return
                        }
                        if (player.absorptionAmount < limitArmor) {
                            player.absorptionAmount++
                        }
                        timeLeft--
                    }
                }.runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 10)
            }
    }

    override val gadgetCoolDown: Int = 5
}