package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.enemy.specials.LCDSpecialEnemy
import com.github.kamunyan.leftcrafterdead.event.LCDPlayerDeathEvent
import com.github.kamunyan.leftcrafterdead.event.StartCheckPointEvent
import com.github.kamunyan.leftcrafterdead.subgadget.HealPotion
import com.github.kamunyan.leftcrafterdead.subgadget.SubGadget
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.MetadataUtil
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryCreativeEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.*
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class EntityControlListener : Listener {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    /**
     * スポーンする敵性Mobの初期設定を行う
     */
    @EventHandler
    fun onLivingEntitySpawn(e: CreatureSpawnEvent) {
        val entity = e.entity
        if (entity.type == EntityType.PLAYER) {
            return
        }

        //ラッシュ用mob設定
        if (e.spawnReason == CreatureSpawnEvent.SpawnReason.JOCKEY) {
            val random = Random().nextInt(manager.matchPlayer.size)
            val player = manager.matchPlayer[random - 1].player
            Bukkit.getPluginManager()
                .callEvent(EntityTargetLivingEntityEvent(entity, player, EntityTargetEvent.TargetReason.CLOSEST_PLAYER))
        }
    }

    @EventHandler
    fun onEntityTargetMob(e: EntityTargetLivingEntityEvent) {
        if (e.reason == EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
            e.isCancelled = true
        }
        if (e.entity.type == EntityType.SNOWMAN) {
            return
        }
        if (e.target != null) {
            if (e.target!!.type == EntityType.VILLAGER || e.target!!.type == EntityType.SNOWMAN) {
                e.isCancelled = true
            }
            if (manager.enemyHashMap.containsKey(e.entity.uniqueId)) {
                val enemy = manager.enemyHashMap[e.entity.uniqueId]!!
                if (enemy is LCDSpecialEnemy) {
                    enemy.specialEnemyRunnable(e.entity as LivingEntity)
                }
            }
        }
    }

    @EventHandler
    fun onEntityDeath(e: EntityDeathEvent) {
        if (e.entityType == EntityType.PLAYER) {
            return
        }
        object : BukkitRunnable() {
            override fun run() {
                e.entity.remove()
                cancel()
            }
        }.runTaskLater(plugin, 20)
    }

    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        val lcdPlayer = manager.getLCDPlayer(e.entity.uniqueId)
        e.deathMessage = ""
        e.keepInventory = false
        e.setShouldDropExperience(false)
        e.drops.clear()
        if (manager.isMatchPlayer(lcdPlayer) && manager.isMatch) {
            if (lcdPlayer.isSurvivor) {
                Bukkit.getPluginManager().callEvent(LCDPlayerDeathEvent(lcdPlayer))
            }
        }
    }

    @EventHandler
    fun onPlayerRespawn(e: PlayerRespawnEvent) {
        val lcdPlayer = manager.getLCDPlayer(e.player)
        if (manager.isMatch) {
            if (!lcdPlayer.isSurvivor) {
                if (manager.numberOfSurvivors() > 1) {
                    for (target in manager.matchPlayer) {
                        if (target.isSurvivor) {
                            e.respawnLocation = target.player.location
                            break
                        }
                    }
                } else {
                    e.respawnLocation = manager.campaign.startLocations[manager.gameProgress]
                }
                lcdPlayer.setSpectator()
            }
        } else if (!manager.isMatch) {
            e.respawnLocation = manager.lobbySpawnLocation
            lcdPlayer.setLobbyItem()
        }
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val lcdPlayer = manager.getLCDPlayer(e.player)
        if (e.player.gameMode != lcdPlayer.gameMode) {
            e.player.gameMode = lcdPlayer.gameMode
        }

        if (lcdPlayer.isSurvivor && manager.isMatch) {
            val inventory = e.player.inventory
            inventory.remove(Material.GLASS_BOTTLE)
            for (i in 5..8) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, SubGadget.nullItem())
                    lcdPlayer.subGadget[i] = null
                }
            }
        }

        if (e.player.location.clone().add(0.0, -0.1, 0.0).block.type == Material.DIAMOND_BLOCK) {
            if (!manager.isMatch || manager.isCheckPoint) {
                return
            }
            if (lcdPlayer.isMatchPlayer && lcdPlayer.isSurvivor) {
                Bukkit.getPluginManager().callEvent(StartCheckPointEvent())
            }
        }
    }

    @EventHandler
    fun onPlayerCreative(e: InventoryCreativeEvent) {
        if (e.whoClicked is Player) {
            val player = e.whoClicked as Player
            if (!player.isOp) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onSprintTrigger(e: PlayerToggleSprintEvent) {
        if (e.player.gameMode != GameMode.ADVENTURE) {
            return
        }
        val data = manager.getLCDPlayer(e.player).statusData
        if (e.isSprinting) {
            object : BukkitRunnable() {
                override fun run() {
                    if (!e.player.isSprinting) {
                        cancel()
                        return
                    }
                    e.player.foodLevel -= 1
                }
            }.runTaskTimerAsynchronously(plugin, 0, (5 * data.staminaDecreaseMultiplier).toLong())
        } else {
            object : BukkitRunnable() {
                var waitTime = 30
                override fun run() {
                    if (e.player.isSprinting) {
                        cancel()
                        return
                    }
                    if (waitTime <= 0) {
                        if (e.player.foodLevel >= 20) {
                            cancel()
                            return
                        }
                        e.player.foodLevel += 1
                        return
                    }
                    waitTime--
                }
            }.runTaskTimerAsynchronously(plugin, 0, 2)
        }
    }

    @EventHandler
    fun onVehicleEnter(e: VehicleEnterEvent){
        if (e.vehicle.hasMetadata(MetadataUtil.SUPPLY_CART) || e.vehicle.hasMetadata(MetadataUtil.TRADER_CART)){
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onVehicleDestroy(e: VehicleDestroyEvent){
        if (e.vehicle.hasMetadata(MetadataUtil.SUPPLY_CART) || e.vehicle.hasMetadata(MetadataUtil.TRADER_CART)){
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryMove(e: InventoryMoveItemEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        if (!e.player.isOp) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockDamage(e: BlockDamageEvent) {
        if (!e.player.isOp) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        if (!e.player.isOp) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onFoodLevelChange(e: FoodLevelChangeEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onRegainHealth(e: EntityRegainHealthEvent) {
        if (e.regainReason == EntityRegainHealthEvent.RegainReason.MAGIC) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDropItem(e: EntityDropItemEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onToolDamage(e: PlayerItemDamageEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onWorldLoad(e: WorldLoadEvent) {
        e.world.pvp = false
    }

    @EventHandler
    fun onLaunchHit(e: ProjectileHitEvent) {
        if (e.entity.type == EntityType.ARROW) {
            e.entity.remove()
        }
        if (e.hitEntity == null) return
        if (e.hitEntity!!.type == EntityType.PLAYER && e.entity.hasMetadata(MetadataUtil.SENTRY_GUN_BALL)) {
            e.isCancelled = true
        }
        if (e.entity.hasMetadata(MetadataUtil.ENEMY_ARROW)) {
            if (manager.enemyHashMap.containsKey(e.hitEntity!!.uniqueId)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onItemConsume(e: PlayerItemConsumeEvent) {
        e.replacement = null
    }

    @EventHandler
    fun onPotionEffect(e: EntityPotionEffectEvent) {
        if (e.cause == EntityPotionEffectEvent.Cause.POTION_DRINK) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onOffHand(e: PlayerSwapHandItemsEvent) {
        e.isCancelled = true
    }
}