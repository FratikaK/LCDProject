package com.github.kamunyan.leftcrafterdead.subgadget

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class PainKiller(slot: Int) : SubGadget(slot) {
    override val subGadgetName: String
        get() = "${ChatColor.GREEN}鎮痛剤"
    override val subGadgetType: SubGadgetType
        get() = SubGadgetType.PAIN_KILLER
    override val customData: Int
        get() = 73
    override val coolDown: Int = 90
    override val material: Material
        get() = Material.PUMPKIN_SEEDS
    override val lore: List<Component>
        get() = listOf(Component.text("一時的な体力を増やす"))

    override fun rightInteract(player: Player) {
        if (isCooldown) return
        startCoolDown(MatchManager.getLCDPlayer(player), coolDown - (subGadgetLevel * 10))
        val lcdPlayer = MatchManager.getLCDPlayer(player)
        val data = lcdPlayer.statusData
        val amount = 10 * data.painKillerMultiplier
        player.absorptionAmount += amount
        if (data.specialSkillTypes.contains(SpecialSkillType.INSPIRE)) {
            MatchManager.matchPlayer.forEach {
                if (player.uniqueId == UUID.fromString(it.uuid)) {
                    return@forEach
                }

                if (it.isSurvivor) {
                    it.player.absorptionAmount += amount
                }
            }
        }
    }
}