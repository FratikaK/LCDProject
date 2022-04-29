package com.github.kamunyan.leftcrafterdead.enemy

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.attribute.Attribute
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

    abstract val metadataKey: String
    abstract val entityType: EntityType
    abstract val enemyType: EnemyType
    abstract val nonHeadShotDamageResistance: Double
    abstract val explosionResistance: Double
    abstract val money: Int

    abstract fun getHealth(): Double
    abstract fun getPower(): Double

    open fun spawnEnemy(location: Location) {
        val enemy = location.world.spawnEntity(location, entityType, CreatureSpawnEvent.SpawnReason.CUSTOM)
        setMetadata(enemy)
        manager.enemyHashMap[enemy.uniqueId] = this
        setLivingEntitySettings(enemy as LivingEntity)
    }

    open fun setLivingEntitySettings(livingEntity: LivingEntity) {
        livingEntity.removeWhenFarAway = false
        livingEntity.noDamageTicks = 0
        livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = getHealth()
        livingEntity.health = getHealth()
        livingEntity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.baseValue = 2.0
    }

    open fun attackSpecialEffects(attacker: LivingEntity, target: LivingEntity) {}
    open fun enemyDeathEffects(enemy: LivingEntity) {}

    fun setMetadata(livingEntity: LivingEntity) {
        livingEntity.setMetadata(metadataKey, FixedMetadataValue(LeftCrafterDead.instance, metadataKey))
    }

    fun setMetadata(entity: Entity) {
        entity.setMetadata(metadataKey, FixedMetadataValue(LeftCrafterDead.instance, metadataKey))
    }

    fun enemyInfo(livingEntity: LivingEntity) {
        println("スピード ${livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.baseValue}")
        println("攻撃力 ${livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)!!.baseValue}")
        println("体力 ${livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue}")
        println("ノックバック率 ${livingEntity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)!!.baseValue}")
    }

    enum class EnemyType {
        NORMAL,
        SPECIAL,
        BOSS
    }
}