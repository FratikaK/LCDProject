package com.github.kamunyan.leftcrafterdead.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class BossParseStartEvent : Event() {
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