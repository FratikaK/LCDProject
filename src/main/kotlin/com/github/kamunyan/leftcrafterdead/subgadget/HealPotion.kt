package com.github.kamunyan.leftcrafterdead.subgadget

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData
import com.github.kamunyan.leftcrafterdead.task.BuffRunnable
import com.github.kamunyan.leftcrafterdead.util.Buff
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class HealPotion(slot: Int) : SubGadget(slot) {
    override val subGadgetName: String
        get() = "${ChatColor.GREEN}回復のポーション"
    override val subGadgetType: SubGadgetType
        get() = SubGadgetType.HEAL_POTION
    override val customData: Int
        get() = 70
    override val coolDown: Int = 90
    override val material: Material
        get() = Material.POTION
    override val lore: List<Component>
        get() = listOf(Component.text("自身の体力を5ポイント回復する"))

    override fun generateItemStack(data: StatusData): ItemStack {
        val item = super.generateItemStack(data)
        val meta = item.itemMeta as PotionMeta
        meta.addCustomEffect(PotionEffect(PotionEffectType.HEAL, 1, 1), false)
        item.itemMeta = meta
        return item
    }

    override fun rightInteract(player: Player) {
        if (isCooldown){
            return
        }
        startCoolDown(MatchManager.getLCDPlayer(player), coolDown - (10 * subGadgetLevel))
        val data = MatchManager.getLCDPlayer(player).statusData
        val healAmount = 6 * data.healPotionMultiplier
        if (player.health + healAmount > player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue) {
            player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
        } else {
            player.health += healAmount
        }
        if (data.specialSkillTypes.contains(SpecialSkillType.COMBAT_MEDIC)) {
            BuffRunnable(MatchManager.getLCDPlayer(player),30,Buff.COMBAT_MEDIC).addBuff()
        }
        if (data.specialSkillTypes.contains(SpecialSkillType.COMBAT_DOCTOR)){
            BuffRunnable(MatchManager.getLCDPlayer(player),30,Buff.COMBAT_DOCTOR).addBuff()
        }
        if (data.specialSkillTypes.contains(SpecialSkillType.INSPIRE)) {
            MatchManager.matchPlayer.forEach {
                if (!it.isSurvivor) return@forEach
                if (it.player.health + healAmount > it.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue) {
                    it.player.health = it.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
                } else {
                    it.player.health += healAmount
                }
            }
        }
    }
}