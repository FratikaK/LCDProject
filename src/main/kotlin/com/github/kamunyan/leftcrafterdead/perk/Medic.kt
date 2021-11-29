package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.HealGrenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.primary.SubMachineGun
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.color.RegularColor
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

class Medic() : Perk(PerkType.MEDIC) {
    override fun perkSymbolMaterial(): Material {
        return Material.REDSTONE
    }

    override fun getGrenade(): Grenade {
        return HealGrenade()
    }

    override fun firstPrimaryWeapon(): PrimaryWeapon {
        return SubMachineGun("MAC10")
    }

    override fun perkGadgetItem(): ItemStack {
        return ItemMetaUtil.generateMetaItem(
            Material.GLOWSTONE_DUST,
            "${ChatColor.GOLD}Heal Pulse",
            110,
            listOf(
                "自身の半径10mにいる周囲のプレイヤーの体力を",
                "40%回復する"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        startGadgetStartCoolDown(lcdPlayer)
        val location = lcdPlayer.player.location.clone()
        val players = location.getNearbyPlayers(10.0 * lcdPlayer.statusData.mainGadgetAddPerformance)
        object : BukkitRunnable() {
            var range = 0.2
            override fun run() {
                for (i in 0..360) {
                    val sin = sin(Math.toRadians(i.toDouble())) * range
                    val cos = cos(Math.toRadians(i.toDouble())) * range
                    ParticleBuilder(ParticleEffect.REDSTONE)
                        .setLocation(
                            location.clone().set(
                                location.x + sin,
                                location.y + 1.0,
                                location.z + cos
                            )
                        )
                        .setAmount(1)
                        .setParticleData(RegularColor(Color.GREEN))
                        .display()
                }
                if (range > 4) {
                    this.cancel()
                    return
                }
                range += 0.2
            }
        }.runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 1)

        players.forEach { player ->
            val healAmount =
                (player.healthScale * 0.4 * MatchManager.getLCDPlayer(player).statusData.mainGadgetAddPerformance).toInt()
            player.spawnParticle(Particle.HEART, location, 10, 0.5, 1.0, 0.5)
            player.sendMessage("${ChatColor.GOLD}${lcdPlayer.player.name}から${healAmount}回復してもらった！")
            if (player.health + healAmount > player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue) {
                player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
                return@forEach
            }
            player.health = player.health + healAmount
        }
    }

    override val gadgetCoolDown: Int = 5
}