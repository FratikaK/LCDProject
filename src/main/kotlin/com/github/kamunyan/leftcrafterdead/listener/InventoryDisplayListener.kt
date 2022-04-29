package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.perk.PerkType
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.subgadget.SubGadget
import com.github.kamunyan.leftcrafterdead.trader.Trader
import com.github.kamunyan.leftcrafterdead.util.InventoryDisplayer
import com.github.kamunyan.leftcrafterdead.util.MetadataUtil
import com.github.kamunyan.leftcrafterdead.util.inventory.DisplayType
import com.github.kamunyan.leftcrafterdead.util.inventory.InventoryDisplay
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class InventoryDisplayListener : Listener {
    private val plugin = LeftCrafterDead.instance
    private val manager = MatchManager
    private val display = InventoryDisplayer

    @EventHandler
    fun onPlayerPerkItemInteract(e: PlayerInteractEvent) {
        if (e.item == null || e.item?.type != Material.END_CRYSTAL) {
            return
        }
        val inventory = InventoryDisplayer.selectPerkDisplay()
        e.player.openInventory(inventory)
    }

    @EventHandler
    fun onPlayerInteractAtTrader(e: PlayerInteractEntityEvent) {
        if (e.rightClicked.hasMetadata(MetadataUtil.TRADER_CART)) {
            Trader.showTraderDisplay(e.player)
        }
    }

    @EventHandler
    fun onExitItemClick(e: InventoryClickEvent) {
        if (e.currentItem != null) {
            if (e.currentItem!!.type == Material.OAK_DOOR) {
                e.isCancelled = true
                (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.BLOCK_WOODEN_DOOR_CLOSE, 1f, 1f)
            }
        }
    }

    @EventHandler
    fun onLCDDisplayItemClick(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        val lcdPlayer = manager.getLCDPlayer(player)
        if (lcdPlayer.displayView != null) {
            val handler = lcdPlayer.displayView!!.instance.buildHandlers()
            if (handler.containsKey(e.slot)) {
                handler[e.slot]!!(e)
            }
        }
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        println("クローズされたよ！")
        println(e.reason)
        if (e.reason == InventoryCloseEvent.Reason.PLAYER || e.reason == InventoryCloseEvent.Reason.DEATH || e.reason == InventoryCloseEvent.Reason.TELEPORT || e.reason == InventoryCloseEvent.Reason.UNKNOWN || e.reason == InventoryCloseEvent.Reason.UNLOADED) {
            val lcdPlayer = manager.getLCDPlayer(e.player as Player)
            lcdPlayer.displayView = null
        }
    }

    @EventHandler
    fun onMeinMenuClick(e: InventoryClickEvent) {
        if (!clickItemHasCustomModelData(e.currentItem)) return
        val inventory: Inventory?
        val lcdPlayer = manager.getLCDPlayer(e.whoClicked.uniqueId)
        if (e.currentItem!!.itemMeta.hasCustomModelData() && e.currentItem!!.itemMeta!!.customModelData == 62) {
            InventoryDisplay.switchDisplay(e.whoClicked as Player, DisplayType.SELECT_FIRST_SUB_GADGET)
            e.isCancelled = true
            lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f)
            return
        }
        if (e.currentItem!!.itemMeta.hasCustomModelData()) {
            inventory = when (e.currentItem!!.itemMeta!!.customModelData) {
                60 -> display.selectPerkDisplay()
                61 -> display.skillTreeTypeSelectDisplay(lcdPlayer)
                91 -> display.mainMenuDisplay()
                500 -> display.skillBuildDisplay(lcdPlayer, SkillType.MASTERMIND)
                501 -> display.skillBuildDisplay(lcdPlayer, SkillType.ENFORCER)
                502 -> display.skillBuildDisplay(lcdPlayer, SkillType.TECHNICIAN)
                503 -> display.skillBuildDisplay(lcdPlayer, SkillType.GHOST)
                504 -> display.skillBuildDisplay(lcdPlayer, SkillType.FUGITIVE)
                510 -> display.skillTreeTypeSelectDisplay(lcdPlayer)
                else -> return
            }
            e.isCancelled = true
            lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f)
            e.whoClicked.openInventory(inventory)
        }
    }

    @EventHandler
    fun onSkillTreeClick(e: InventoryClickEvent) {
        if (!clickItemHasCustomModelData(e.currentItem)) return
        e.isCancelled = true
        if (e.currentItem!!.type == Material.BEDROCK) {
            e.whoClicked.sendMessage(Component.text("${ChatColor.RED}アンロックに必要なポイントが足りません！"))
            e.whoClicked as Player
            (e.whoClicked as Player).playSound(e.whoClicked.location, Sound.BLOCK_LAVA_POP, 1f, 1f)
            return
        }
        val lcdPlayer = manager.getLCDPlayer(e.whoClicked.uniqueId)
        val skillTree = lcdPlayer.skillTree
        if (e.currentItem!!.itemMeta.hasCustomModelData()) {
            val skillType = when (e.currentItem!!.itemMeta.customModelData) {
                550 -> SkillType.MASTERMIND
                551 -> SkillType.ENFORCER
                552 -> SkillType.TECHNICIAN
                553 -> SkillType.GHOST
                554 -> SkillType.FUGITIVE
                else -> return
            }

            skillTree[skillType]!!.selectSkill(lcdPlayer, e.slot)
        }
    }

    @EventHandler
    fun onPlayerSelectPerk(e: InventoryClickEvent) {
        if (e.currentItem == null || !e.currentItem!!.hasItemMeta() || !e.currentItem!!.itemMeta.hasCustomModelData() || e.currentItem!!.itemMeta.customModelData != 100) {
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

    private fun clickItemHasCustomModelData(currentItem: ItemStack?): Boolean {
        var flag = false
        if (currentItem != null) {
            if (currentItem.hasItemMeta()) {
                currentItem.itemMeta.hasCustomModelData()
                flag = true
            }
        }
        return flag
    }
}