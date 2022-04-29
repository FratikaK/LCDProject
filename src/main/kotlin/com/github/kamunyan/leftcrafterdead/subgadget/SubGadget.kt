package com.github.kamunyan.leftcrafterdead.subgadget

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.skill.StatusData
import com.github.kamunyan.leftcrafterdead.util.InventoryDisplayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

abstract class SubGadget(val slot: Int) {
    abstract val subGadgetName: String
    abstract val subGadgetType: SubGadgetType
    abstract val customData: Int
    abstract val coolDown: Int
    abstract val material: Material
    abstract val lore: List<Component>

    open fun generateItemStack(data: StatusData): ItemStack {
        return ItemMetaUtil.generateMetaItem(material, "${ChatColor.AQUA}$subGadgetName", customData)
    }

    var subGadgetLevel = 0
    fun levelUp(): Boolean {
        if (subGadgetLevel >= 4) {
            return false
        }
        subGadgetLevel += 1
        return true
    }

    protected var isCooldown = false

    fun startCoolDown(lcdPlayer: LCDPlayer, time: Int = coolDown) {
        val coolDownItem =
            ItemMetaUtil.generateMetaItem(Material.BLACK_DYE, "${ChatColor.GREEN}${subGadgetName} クールダウン中")
        coolDownItem.amount = time
        lcdPlayer.player.inventory.setItem(slot, coolDownItem)
        val item = lcdPlayer.player.inventory.getItem(slot) ?: return
        isCooldown = true
        object : BukkitRunnable() {
            override fun run() {
                if (lcdPlayer.player.isDead || !MatchManager.isMatchPlayer(lcdPlayer)) {
                    isCooldown = false
                    cancel()
                    return
                }

                if (item.amount <= 1) {
                    lcdPlayer.player.inventory.setItem(slot, generateItemStack(lcdPlayer.statusData))
                    isCooldown = false
                    cancel()
                    return
                }

                try {
                    item.amount--
                    item.itemMeta.displayName(Component.text("${ChatColor.RED}再使用可能になるまで${ChatColor.AQUA}${item.amount}${ChatColor.RED}秒"))
                } catch (_: Exception) {

                }
            }
        }.runTaskTimerAsynchronously(LeftCrafterDead.instance, 0, 20)
    }

    abstract fun rightInteract(player: Player)

    companion object {
        fun nullItem(): ItemStack {
            return ItemMetaUtil.generateMetaItem(Material.BLACK_STAINED_GLASS_PANE, "No Gadget")
        }

        fun createSubGadgetInstance(slot: Int, type: SubGadgetType): SubGadget {
            return when (type) {
                SubGadgetType.HEAL_POTION -> HealPotion(slot)
                SubGadgetType.TRIP_MINE -> TripMine(slot)
                SubGadgetType.SENTRY_GUN -> SentryGun(slot)
                SubGadgetType.PAIN_KILLER -> PainKiller(slot)
            }
        }

        val selectGadgetDisplayItemMap =
            hashMapOf(
                1 to SubGadgetType.HEAL_POTION,
                2 to SubGadgetType.TRIP_MINE,
                3 to SubGadgetType.SENTRY_GUN,
                4 to SubGadgetType.PAIN_KILLER
            )
    }
}