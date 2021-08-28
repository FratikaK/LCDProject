package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent
import com.shampaggon.crackshot.events.WeaponExplodeEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class DamageListener : Listener {
    private val plugin = LeftCrafterDead.instance

    @EventHandler
    fun onCrackShotExplosion(e: WeaponExplodeEvent) {
    }

    @EventHandler
    fun onCrackShotDamage(e: WeaponDamageEntityEvent){
    }
}