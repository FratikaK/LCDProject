package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

object SentryGunMinion {
    private val plugin = LeftCrafterDead.instance
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
            var timeLeft = data.sentryGunTime * 17
            override fun run() {
                if (sentryMob.isDead || !lcdPlayer.player.world.livingEntities.contains(sentryMob)) {
                    sentryGunHashMap.remove(sentryMob.uniqueId)
                    cancel()
                    return
                }

                if (timeLeft <= 0) {
                    sentryGunHashMap.remove(sentryMob.uniqueId)
                    sentryMob.remove()
                    cancel()
                    return
                }

                if (sentryMob.location.getNearbyLivingEntities(10.0).isNotEmpty()) {
                    val snowball = sentryMob.launchProjectile(Snowball::class.java)
                    snowball.setMetadata(
                        "projParentNode",
                        FixedMetadataValue(plugin.crackShot.handle, lcdPlayer.primary.weaponTitle)
                    )
                    snowball.shooter = lcdPlayer.player
                    sentryMob.world.playSound(sentryMob.location, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f, 1f)
                }
                sentryMob.customName = "${ChatColor.RED}${timeLeft}${ChatColor.AQUA}[${lcdPlayer.player.name}] セントリーガン"
                timeLeft--
            }
        }.runTaskTimer(plugin, 0, 2)
    }
}