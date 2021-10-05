package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.event.LCDPlayerDeathEvent
import com.github.kamunyan.leftcrafterdead.event.StartCheckPointEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryCreativeEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerRespawnEvent
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
        entity.noDamageTicks = 0
        entity.removeWhenFarAway = false

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
        if (e.target != null) {
            if (e.target!!.type == EntityType.VILLAGER) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        val lcdPlayer = manager.getLCDPlayer(e.entity.uniqueId)
        e.deathMessage = ""
        e.keepInventory = false

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
                    e.respawnLocation = manager.campaign.startLocation
                }
                lcdPlayer.setSpectator()
            }
        } else if (!manager.isMatch) {
            e.respawnLocation = manager.lobbySpawnLocation
            lcdPlayer.setLobbyItem()
        }
    }

    @EventHandler
    fun onPlayerMove(e:PlayerMoveEvent){
        if (e.player.location.clone().add(0.0,-0.1,0.0).block.type == Material.DIAMOND_BLOCK){
            if (!manager.isMatch || manager.isCheckPoint){
                return
            }
            val lcdPlayer = manager.getLCDPlayer(e.player)
            if (lcdPlayer.isMatchPlayer && lcdPlayer.isSurvivor){
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
    fun onInventoryMove(e:InventoryMoveItemEvent){
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
    fun onPlayerDropItem(e: EntityDropItemEvent) {
        e.isCancelled = true
    }
}