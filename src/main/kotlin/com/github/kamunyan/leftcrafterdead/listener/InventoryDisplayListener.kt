package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.perk.PerkType
import com.github.kamunyan.leftcrafterdead.util.InventoryDisplayer
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

class InventoryDisplayListener : Listener {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager

    @EventHandler
    fun onPlayerPerkItemInteract(e: PlayerInteractEvent) {
        if (e.item == null || e.item?.type != Material.END_CRYSTAL) {
            return
        }
        val inventory = InventoryDisplayer.selectPerkDisplay()
        e.player.openInventory(inventory)
    }

    @EventHandler
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent){
        val entity = e.rightClicked
        if (entity.type == EntityType.VILLAGER){
            e.isCancelled = true
            e.player.openInventory(InventoryDisplayer.merchantWeaponSelectDisplay())
        }
    }

    @EventHandler
    fun onPlayerSelectPerk(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) {
            return
        }
        if (e.currentItem == null || !e.currentItem!!.hasItemMeta() || e.currentItem!!.itemMeta.customModelData != 100) {
            return
        }
        val lcdPlayer = manager.getLCDPlayer(e.whoClicked.uniqueId)
        if (lcdPlayer.isMatchPlayer && manager.isMatch) {
            return
        }
        val perkType = PerkType.getPerkType(e.currentItem!!.type)
        lcdPlayer.setPerk(perkType)
        e.isCancelled = true
    }

    @EventHandler
    fun onPlayerSelectWeaponType(e: InventoryClickEvent){
        if (e.whoClicked !is Player){
            return
        }
        val item = e.currentItem?:return
        if (item.itemMeta.hasCustomModelData()){
            when(item.itemMeta.customModelData){
                200 -> e.whoClicked.openInventory(InventoryDisplayer.primaryDisplay())
            }
        }
    }
}