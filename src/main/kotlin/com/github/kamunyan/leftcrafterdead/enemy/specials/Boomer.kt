package com.github.kamunyan.leftcrafterdead.enemy.specials

import com.github.kamunyan.leftcrafterdead.event.RushStartEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect

object Boomer : LCDSpecialEnemy() {
    override val entityType: EntityType = EntityType.CREEPER
    override fun specialEnemyRunnable(livingEntity: LivingEntity) {}

    override val nonHeadShotDamageResistance: Double = 0.0
    override val explosionResistance: Double = 0.0

    override fun getHealth(): Double {
        return 5.0
    }

    override fun getPower(): Double {
        return 1.0
    }

    override fun enemyDeathEffects(enemy: LivingEntity) {
        val location = enemy.location.clone()
        val players = location.getNearbyPlayers(4.0)
        if (players.isNotEmpty()) {
            Bukkit.getPluginManager().callEvent(RushStartEvent())
            players.forEach { player ->
                player.addPotionEffects(
                    listOf(
                        PotionEffect(PotionEffectType.SLOW, 200, 2, false, false),
                        PotionEffect(PotionEffectType.BLINDNESS, 200, 5, false, false)
                    )
                )
                Bukkit.broadcast(Component.text("${ChatColor.AQUA}${player.name}${ChatColor.YELLOW}が敵をおびき寄せてしまった！"))
            }
        }
        location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 2f, 3f)
        ParticleBuilder(ParticleEffect.EXPLOSION_LARGE, location).display()
        ParticleBuilder(ParticleEffect.SLIME, location)
            .setAmount(600)
            .setOffset(5f, 3f, 5f)
            .display()
    }
}