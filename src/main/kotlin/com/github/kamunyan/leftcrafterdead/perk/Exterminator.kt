package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.Primary
import com.github.kamunyan.leftcrafterdead.weapons.PrimaryType
import com.github.kamunyan.leftcrafterdead.weapons.grenade.ClusterBomb
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.color.RegularColor
import java.awt.Color

class Exterminator : Perk(PerkType.EXTERMINATOR) {
    override fun perkSymbolMaterial(): Material {
        return Material.NETHERITE_INGOT
    }

    override fun getGrenade(): Grenade {
        return ClusterBomb
    }

    override fun firstPrimaryWeapon(): Primary {
        return Primary(PrimaryType.SUPER_90)
    }

    override fun perkGadgetItem(): ItemStack {
        return ItemMetaUtil.generateMetaItem(
            Material.PHANTOM_MEMBRANE,
            "${ChatColor.GOLD}Shock wave",
            110,
            listOf(
                "自身の半径8mにいる周囲の敵性mobを吹っ飛ばし、",
                "動きを3秒封じる"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        super.gadgetRightInteract(lcdPlayer)
        val location = lcdPlayer.player.location.clone()
        val entities = location.getNearbyLivingEntities(8.0)

        ParticleBuilder(ParticleEffect.REDSTONE)
            .setLocation(location.add(0.0, 1.0, 0.0))
            .setOffset(3f, 2f, 3f)
            .setAmount(800)
            .setParticleData(RegularColor(Color.BLACK))
            .display()
        ParticleBuilder(ParticleEffect.EXPLOSION_HUGE)
            .setLocation(location.add(0.0, 1.0, 0.0))
            .setAmount(1)
            .display()
        location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 3f, 1f)

        entities.forEach { livingEntity ->
            if (livingEntity.type == EntityType.PLAYER || livingEntity.type == EntityType.VILLAGER) {
                return@forEach
            }
            livingEntity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 0.0
            livingEntity.damage(0.0, lcdPlayer.player)
            livingEntity.velocity = Vector(
                livingEntity.velocity.x * 20.0,
                livingEntity.velocity.y * 3.0,
                livingEntity.velocity.z * 20.0
            )
            livingEntity.damage(0.0, lcdPlayer.player)
            livingEntity.addPotionEffect(
                PotionEffect(
                    PotionEffectType.SLOW,
                    (60 * lcdPlayer.statusData.mainGadgetAddPerformance).toInt(),
                    5,
                    false,
                    false
                )
            )
        }
    }

    override val gadgetCoolDown: Int = 30
}