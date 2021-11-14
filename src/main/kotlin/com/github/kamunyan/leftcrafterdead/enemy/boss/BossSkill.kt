package com.github.kamunyan.leftcrafterdead.enemy.boss

import org.bukkit.entity.LivingEntity

abstract class BossSkill {
    abstract fun activationSkill(livingEntity: LivingEntity)
}