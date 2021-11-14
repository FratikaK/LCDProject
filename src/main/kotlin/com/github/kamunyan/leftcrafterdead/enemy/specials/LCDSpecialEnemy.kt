package com.github.kamunyan.leftcrafterdead.enemy.specials

import com.github.kamunyan.leftcrafterdead.enemy.LCDEnemy
import org.bukkit.entity.LivingEntity

abstract class LCDSpecialEnemy:LCDEnemy() {
    override val metadataKey: String = SPECIAL_ENEMY_KEY
    override val enemyType: EnemyType = EnemyType.SPECIAL

    abstract fun specialEnemyRunnable(livingEntity: LivingEntity)

}