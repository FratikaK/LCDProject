package com.github.kamunyan.leftcrafterdead.weapons.grenade

import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ClusterBomb : Grenade("Cluster Bomb") {
    override val grenadeType: GrenadeType
        get() = GrenadeType.CLUSTER_BOMB

    override fun explosionEffects(location: Location) {
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
        entity.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 100, 5, false, false))
    }
}