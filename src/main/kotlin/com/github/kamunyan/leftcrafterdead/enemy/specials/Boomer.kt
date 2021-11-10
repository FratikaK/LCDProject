package com.github.kamunyan.leftcrafterdead.enemy.specials

import com.github.kamunyan.leftcrafterdead.enemy.LCDEnemy
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import java.util.*

class Boomer : LCDEnemy() {
    override lateinit var uuid: UUID
    override val metadataKey: String = SPECIAL_ENEMY_KEY
    override val entityType: EntityType = EntityType.CREEPER
    override val enemyType: EnemyType = EnemyType.SPECIAL
    override val nonHeadShotDamageResistance: Double = 0.0
    override val explosionResistance: Double = 0.0

    override fun getHealth(): Double {
        return 5.0
    }

    override fun getPower(): Double {
        return 1.0
    }

    override fun enemyDeathEffects() {
        val entity = Bukkit.getEntity(uuid) ?: return
        val location = entity.location.clone()
        location.getNearbyPlayers(4.0).forEach { player ->
            player.addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.SLOW, 200, 2, false, false),
                    PotionEffect(PotionEffectType.BLINDNESS, 200, 5, false, false)
                )
            )
        }
        location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 2f, 3f)
        ParticleBuilder(ParticleEffect.EXPLOSION_LARGE, location).display()
        ParticleBuilder(ParticleEffect.SLIME, location)
            .setAmount(600)
            .setOffset(5f, 3f, 5f)
            .display()
    }
}