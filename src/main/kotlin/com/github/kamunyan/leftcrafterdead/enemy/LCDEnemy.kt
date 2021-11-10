package com.github.kamunyan.leftcrafterdead.enemy

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.metadata.FixedMetadataValue
import java.util.*

abstract class LCDEnemy {
    companion object {
        const val NORMAL_ENEMY_KEY = "NORMAL_ENEMY"
        const val SPECIAL_ENEMY_KEY = "SPECIAL_ENEMY"
        const val BOSS_ENEMY_KEY = "BOSS_ENEMY"
    }

    val manager = MatchManager

    abstract var uuid: UUID
    abstract val metadataKey: String
    abstract val entityType: EntityType
    abstract val enemyType: EnemyType
    abstract val nonHeadShotDamageResistance: Double
    abstract val explosionResistance: Double

    abstract fun getHealth(): Double
    abstract fun getPower(): Double

    open fun spawnEnemy(location: Location): Entity {
        val enemy = manager.world.spawnEntity(location, entityType, CreatureSpawnEvent.SpawnReason.CUSTOM)
        setMetadata(enemy)
        uuid = enemy.uniqueId
        manager.enemyHashMap[enemy.uniqueId] = this
        return enemy
    }

    open fun attackSpecialEffects(target: LivingEntity) {}
    open fun enemyDeathEffects() {}

    fun setMetadata(livingEntity: LivingEntity) {
        livingEntity.setMetadata(metadataKey, FixedMetadataValue(LeftCrafterDead.instance, metadataKey))
    }

    fun setMetadata(entity: Entity) {
        entity.setMetadata(metadataKey, FixedMetadataValue(LeftCrafterDead.instance, metadataKey))
    }

    enum class EnemyType {
        NORMAL,
        SPECIAL,
        BOSS
    }
}