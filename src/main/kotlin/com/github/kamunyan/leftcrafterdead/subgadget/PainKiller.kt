package com.github.kamunyan.leftcrafterdead.subgadget

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.skill.StatusData
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

object PainKiller : SubGadget() {
    override val subGadgetName: String
        get() = "${ChatColor.GREEN}鎮痛剤"
    override val subGadgetType: SubGadgetType
        get() = SubGadgetType.PAIN_KILLER
    override val customData: Int
        get() = 73
    override val limitAmount: Int
        get() = 3
    override val material: Material
        get() = Material.PUMPKIN_SEEDS
    override val lore: List<Component>
        get() = listOf(Component.text("一時的に最大体力を増やす"))

    override fun rightInteract(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST, 600, 3, false, false, false))
        var amount = 12.0
        val defaultHealth = player.health
        player.health += amount
        object : BukkitRunnable() {
            override fun run() {
                try {
                    if (player.health <= defaultHealth) {
                        player.removePotionEffect(PotionEffectType.HEALTH_BOOST)
                        cancel()
                        return
                    }else if (amount <= 0){
                        player.health = defaultHealth
                    }
                    player.health--
                    amount--
                } catch (e: Exception) {
                    player.removePotionEffect(PotionEffectType.HEALTH_BOOST)
                    player.health = defaultHealth
                    cancel()
                    return
                }
            }
        }.runTaskTimer(LeftCrafterDead.instance, 0, 40)
    }

    override fun generateItemStack(data: StatusData): ItemStack {
        val item = super.generateItemStack(data)
        item.amount = data.painKillerAmount
        return item
    }
}