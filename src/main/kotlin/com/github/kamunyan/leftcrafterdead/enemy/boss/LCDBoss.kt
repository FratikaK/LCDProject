package com.github.kamunyan.leftcrafterdead.enemy.boss

import com.github.kamunyan.leftcrafterdead.enemy.LCDEnemy

abstract class LCDBoss : LCDEnemy() {
    override val metadataKey: String = BOSS_ENEMY_KEY
    override val enemyType: EnemyType = EnemyType.BOSS

    abstract val bossSkillCoolDown: Int

    abstract fun bossSkillRunnable()

    abstract fun bossSkill()

    abstract fun bossDamagedRunnable()
}