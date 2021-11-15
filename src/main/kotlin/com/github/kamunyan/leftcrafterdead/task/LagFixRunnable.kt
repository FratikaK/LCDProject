package com.github.kamunyan.leftcrafterdead.task

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Explosive
import org.bukkit.entity.Projectile
import org.bukkit.scheduler.BukkitRunnable


object LagFixRunnable : BukkitRunnable() {
    override fun run() {
        val removed = Bukkit.getWorlds().stream()
            .flatMap { world: World ->
                world.getEntitiesByClasses(
                    Projectile::class.java,
                    Explosive::class.java
                ).stream()
            }
            .filter { entity: Entity -> entity.ticksLived > 20 * 20 }
            .peek(Entity::remove)
            .count()
        if (removed > 0) {
            Bukkit.getLogger().info("[LagFixRunnable] 不要な $removed 体のエンティティが削除されました。")
        }
    }

    fun runTask() {
        runTaskTimer(LeftCrafterDead.instance, 0, 20)
    }
}