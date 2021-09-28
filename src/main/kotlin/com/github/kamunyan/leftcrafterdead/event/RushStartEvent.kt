package com.github.kamunyan.leftcrafterdead.event

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class RushStartEvent : Event(), Cancellable {
    private var cancel = false

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

    override fun isCancelled(): Boolean {
        return this.cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }
}