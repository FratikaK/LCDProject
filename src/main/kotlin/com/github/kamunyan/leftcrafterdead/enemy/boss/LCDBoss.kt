package com.github.kamunyan.leftcrafterdead.enemy.boss

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.enemy.LCDEnemy
import com.github.kamunyan.leftcrafterdead.event.DefeatBossEvent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

abstract class LCDBoss : LCDEnemy() {
    override val metadataKey: String = BOSS_ENEMY_KEY
    override val enemyType: EnemyType = EnemyType.BOSS
    lateinit var livingEntity: LivingEntity
    abstract val bossSkills: List<BossSkill>

    override fun spawnEnemy(location: Location) {
        val enemy = manager.world.spawnEntity(location, entityType, CreatureSpawnEvent.SpawnReason.CUSTOM)
        setMetadata(enemy)
        manager.enemyHashMap[enemy.uniqueId] = this
        livingEntity = enemy as LivingEntity
        setLivingEntitySettings(livingEntity)
        bossNormalAttackRunnable()
        bossSkillRunnable()
    }

    abstract fun bossNormalAttackRunnable()

    private fun bossSkillRunnable() {
        var timeLeft = bossSkillCoolDown
        val random = Random()
        object : BukkitRunnable() {
            override fun run() {
                if (livingEntity.isDead) {
                    cancel()
                    return
                }
                if (timeLeft <= 0) {
                    timeLeft = bossSkillCoolDown
                    Bukkit.getLogger().info("ボススキル発動！")
                    bossSkills[random.nextInt(bossSkills.size)].activationSkill(livingEntity)
                    return
                }
                timeLeft--
            }
        }.runTaskTimer(LeftCrafterDead.instance, 0, 20)
    }

    override fun enemyDeathEffects(enemy: LivingEntity) {
        Bukkit.getPluginManager().callEvent(DefeatBossEvent())
    }

    abstract val bossSkillCoolDown: Int
}