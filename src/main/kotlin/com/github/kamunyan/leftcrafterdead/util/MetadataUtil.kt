package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.metadata.FixedMetadataValue

object MetadataUtil {
    const val EXPLODE_ARROW = "EXPLODE_ARROW"
    const val MERCHANT = "MERCHANT"

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