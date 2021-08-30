package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.shampaggon.crackshot.events.WeaponExplodeEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class DamageControlListener : Listener {
    private val plugin = LeftCrafterDead.instance

    @EventHandler
    fun onCrackShotExplosion(e: WeaponExplodeEvent) {
        val entities = e.location.getNearbyLivingEntities(5.0)
        for (entity in entities){
            //TODO ダメージ処理
        }
    }
}