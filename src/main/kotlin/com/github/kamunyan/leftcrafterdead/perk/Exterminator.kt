package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.ClusterBomb
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.primary.Shotgun
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.color.RegularColor
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

class Exterminator : Perk(PerkType.EXTERMINATOR) {
    override fun perkSymbolMaterial(): Material {
        return Material.NETHERITE_INGOT
    }

    override fun getGrenade(): Grenade {
        return ClusterBomb()
    }

    override fun firstPrimaryWeapon(): PrimaryWeapon {
        return Shotgun("M31")
    }

    override fun perkGadgetItem(): ItemStack {
        return ItemMetaUtil.generateMetaItem(
            Material.PHANTOM_MEMBRANE,
            "${ChatColor.GOLD}Shock wave",
            110,
            listOf(
                "自身の半径5mにいる周囲の敵性mobを吹っ飛ばし、",
                "動きを3秒封じる"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        startGadgetStartCoolDown(lcdPlayer)
        val location = lcdPlayer.player.location.clone()
        val entities = location.getNearbyLivingEntities(5.0)
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
                        .setParticleData(RegularColor(Color.BLACK))
                        .display()
                }
                if (range > 5) {
                    cancel()
                    return
                }
                range += 0.5
            }
        }.runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 1)
        location.world.createExplosion(location, 4.0f, false, false)
        entities.forEach { livingEntity ->
            if (livingEntity.type == EntityType.PLAYER || livingEntity.type == EntityType.VILLAGER) {
                return@forEach
            }
            val defaultVector = livingEntity.velocity.clone()
            livingEntity.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 60, 5, false, false))
        }
    }

    override val gadgetCoolDown: Int = 5
}