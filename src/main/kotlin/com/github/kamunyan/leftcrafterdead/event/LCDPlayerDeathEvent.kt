package com.github.kamunyan.leftcrafterdead.event

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class LCDPlayerDeathEvent(val lcdPlayer: LCDPlayer) : Event() {
    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }
}