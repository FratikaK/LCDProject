package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Minecart
import org.bukkit.entity.Projectile
import org.bukkit.metadata.FixedMetadataValue

object MetadataUtil {
    const val ENEMY_ARROW = "ENEMY_ARROW"
    const val BOSS_ARROW = "BOSS_ARROW"
    const val EXPLODE_ARROW = "EXPLODE_ARROW"
    const val MERCHANT = "MERCHANT"
    const val CAMPAIGN_JOIN_MOB = "CAMPAIGN_JOIN_MOB"
    const val SENTRY_GUN = "SENTRY_GUN"
    const val SENTRY_GUN_BALL = "SENTRY_GUN_BALL"
    const val SUPPLY_CART = "SUPPLY_CART"
    const val TRADER_CART = "TRADER_CART"

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

    fun setSupplyMetadata(cart: Minecart, metadata: String) {
        if (!cart.hasMetadata(metadata)) {
            cart.setMetadata(metadata, FixedMetadataValue(LeftCrafterDead.instance, null))
        }
    }
}