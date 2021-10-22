package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.HealGrenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.primary.SubMachineGun
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
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
        return SubMachineGun("Mac10")
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
        val players = location.getNearbyPlayers(10.0)
        for (i in 0..360) {
            ParticleBuilder(ParticleEffect.REDSTONE)
                .setLocation(
                    location.clone().set(
                        location.x + sin(Math.toRadians(i.toDouble())) * 4,
                        location.y + 1.0,
                        location.z + cos(Math.toRadians(i.toDouble())) * 4
                    )
                )
                .setAmount(1)
                .setParticleData(RegularColor(Color.GREEN))
                .display()
        }
        players.forEach { player ->
            val healAmount = (player.healthScale * 0.4).toInt()
            if (player.health + healAmount > player.healthScale) {
                player.health = player.healthScale
                return@forEach
            }
            player.health = player.health + healAmount
            player.spawnParticle(Particle.HEART, location, 10, 0.5, 1.0, 0.5)
        }
    }

    override val gadgetCoolDown: Int = 5
}