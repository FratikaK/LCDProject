package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

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
    }
}