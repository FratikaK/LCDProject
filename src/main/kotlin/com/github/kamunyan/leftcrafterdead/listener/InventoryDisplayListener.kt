package com.github.kamunyan.leftcrafterdead.listener

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.perk.PerkType
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.subgadget.SubGadget
import com.github.kamunyan.leftcrafterdead.util.InventoryDisplayer
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
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
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
        val entity = e.rightClicked
        if (entity.type == EntityType.VILLAGER) {
            e.isCancelled = true
            e.player.openInventory(display.merchantWeaponSelectDisplay())
        }
    }

    @EventHandler
    fun onMeinMenuClick(e: InventoryClickEvent) {
        if (!clickItemHasCustomModelData(e.currentItem)) return
        var inventory: Inventory? = null
        val lcdPlayer = manager.getLCDPlayer(e.whoClicked.uniqueId)
        inventory = when (e.currentItem!!.itemMeta!!.customModelData) {
            60 -> display.selectPerkDisplay()
            61 -> display.skillTreeTypeSelectDisplay(lcdPlayer)
            62 -> display.selectFirstSubGadgetDisplay()
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
        e.whoClicked.openInventory(inventory)
    }

    @EventHandler
    fun onSelectSubGadget(e: InventoryClickEvent) {
        if (!clickItemHasCustomModelData(e.currentItem)) return
        e.isCancelled = true
        if (e.currentItem!!.itemMeta.customModelData == 92) {
            val lcdPlayer = manager.getLCDPlayer(e.whoClicked.uniqueId)
            lcdPlayer.player.openInventory(display.subGadgetSlotSelectDisplay(lcdPlayer, e.currentItem!!))
        }
    }

    @EventHandler
    fun onSelectSubGadgetSlot(e: InventoryClickEvent) {
        if (!clickItemHasCustomModelData(e.currentItem)) return
        if (e.inventory.getItem(8) == null) return
        e.isCancelled = true
        val lcdPlayer = manager.getLCDPlayer(e.whoClicked.uniqueId)
        val type = SubGadget.getSubGadget(e.inventory.getItem(8)!!.type)
        SubGadget.setFirstSubGadget(lcdPlayer,type,e.currentItem!!.itemMeta.customModelData)
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

    @EventHandler
    fun onPlayerSelectWeaponType(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) {
            return
        }
        val item = e.currentItem ?: return
        if (item.hasItemMeta() && item.itemMeta.hasCustomModelData()) {
            when (item.itemMeta.customModelData) {
                200 -> e.whoClicked.openInventory(InventoryDisplayer.primaryDisplay())
            }
        }
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