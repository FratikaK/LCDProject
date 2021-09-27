package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import java.util.*

class EntityControlListener :Listener{
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    /**
     * スポーンする敵性Mobの初期設定を行う
     */
    @EventHandler
    fun onLivingEntitySpawn(e: CreatureSpawnEvent){
        val entity = e.entity
        entity.noDamageTicks = 0
        entity.removeWhenFarAway = false

        //ラッシュ用mob設定
        if (e.spawnReason == CreatureSpawnEvent.SpawnReason.JOCKEY){
            val random = Random().nextInt(manager.matchPlayer.size)
            val player = manager.matchPlayer[random - 1].player
            Bukkit.getPluginManager().callEvent(EntityTargetLivingEntityEvent(entity,player,EntityTargetEvent.TargetReason.CLOSEST_PLAYER))
        }
    }

    @EventHandler
    fun onFoodLevelChange(e:FoodLevelChangeEvent){
        e.isCancelled = true
    }
}