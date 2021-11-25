package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.metadata.FixedMetadataValue

object MetadataUtil {
    const val ENEMY_ARROW = "ENEMY_ARROW"
    const val EXPLODE_ARROW = "EXPLODE_ARROW"
    const val MERCHANT = "MERCHANT"
    const val CAMPAIGN_JOIN_MOB = "CAMPAIGN_JOIN_MOB"
    const val SENTRY_GUN = "SENTRY_GUN"
    const val SENTRY_GUN_BALL = "SENTRY_GUN_BALL"

    fun setProjectileMetadata(projectile: Projectile, metadata: String) {
        if (!projectile.hasMetadata(metadata)) {
            projectile.setMetadata(metadata, FixedMetadataValue(LeftCrafterDead.instance, metadata))
        }
    }

    fun setLivingEntityMetadata(livingEntity: LivingEntity, metadata: String) {
        if (!livingEntity.hasMetadata(metadata)) {
            livingEntity.setMetadata(metadata, FixedMetadataValue(LeftCrafterDead.instance, metadata))
        }
    }
}