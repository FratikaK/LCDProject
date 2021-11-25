package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object SentryGunMinion {
    private val plugin = LeftCrafterDead.instance
    private val random = Random()
    val sentryGunHashMap = HashMap<UUID, Player>()
    fun spawnSentry(lcdPlayer: LCDPlayer) {
        val sentryMob = lcdPlayer.player.world.spawnEntity(
            lcdPlayer.player.location,
            EntityType.SNOWMAN,
            CreatureSpawnEvent.SpawnReason.CUSTOM
        ) as LivingEntity
        val data = lcdPlayer.statusData
        sentryGunHashMap[sentryMob.uniqueId] = lcdPlayer.player
        MetadataUtil.setLivingEntityMetadata(sentryMob, MetadataUtil.SENTRY_GUN)
        sentryMob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.baseValue = 0.01
        sentryMob.isCustomNameVisible = true
        sentryMob.customName = "${ChatColor.AQUA}[${lcdPlayer.player.name}] セントリーガン"
        sentryMob.setAI(true)
        object : BukkitRunnable() {
            var timeLeft = data.sentryGunTime * 3
            override fun run() {
                if (sentryMob.isDead || !lcdPlayer.player.world.livingEntities.contains(sentryMob)) {
                    sentryGunHashMap.remove(sentryMob.uniqueId)
                    cancel()
                    return
                }

                if (timeLeft <= 0) {
                    sentryGunHashMap.remove(sentryMob.uniqueId)
                    sentryMob.health = 0.0
                    cancel()
                    return
                }

                if (sentryMob.location.getNearbyLivingEntities(10.0).isNotEmpty()) {
                    val yaw = Math.toRadians((-sentryMob.location.yaw - 90.0f).toDouble())
                    val pitch = Math.toRadians((-sentryMob.location.pitch).toDouble())
                    val spread = doubleArrayOf(1.0, 1.0, 1.0)

                    for (i in 0..3) {
                        spread[i] = (random.nextDouble() - random.nextDouble()) * 2.0 * 0.1
                    }
                    val x = cos(pitch) * cos(yaw) + spread[0]
                    val y = sin(pitch) + spread[1]
                    val z = -sin(yaw) * cos(pitch) + spread[2]
                    val dirVel = Vector(x, y, z)
                    val snowball = sentryMob.launchProjectile(Snowball::class.java)
                    snowball.setMetadata(
                        "projParentNode",
                        FixedMetadataValue(plugin.crackShot.handle, lcdPlayer.primary.weaponTitle)
                    )
                    snowball.velocity = dirVel.multiply(10)
                    snowball.shooter = lcdPlayer.player
                    sentryMob.world.playSound(sentryMob.location, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 0.5f, 1f)
                }
                sentryMob.customName = "${ChatColor.RED}${timeLeft}${ChatColor.AQUA}[${lcdPlayer.player.name}] セントリーガン"
                timeLeft--
            }
        }.runTaskTimer(plugin, 0, 3)
    }
}