package com.github.kamunyan.leftcrafterdead.subgadget

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.skill.StatusData
import com.github.kamunyan.leftcrafterdead.util.SentryGunMinion
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object SentryGun : SubGadget() {
    override val subGadgetName: String
        get() = "${ChatColor.GREEN}セントリーガン"
    override val subGadgetType: SubGadgetType
        get() = SubGadgetType.SENTRY_GUN
    override val customData: Int
        get() = 72
    override val limitAmount: Int
        get() = 2
    override val material: Material
        get() = Material.PUMPKIN
    override val lore: List<Component>
        get() = listOf(Component.text("セントリーガンを設置する"), Component.text("攻撃力は設置したプレイヤーの攻撃力に依存する"))

    override fun rightInteract(player: Player) {
        SentryGunMinion.spawnSentry(MatchManager.getLCDPlayer(player))
    }

    override fun generateItemStack(data: StatusData): ItemStack {
        val item = super.generateItemStack(data)
        item.amount = data.sentryGunAmount
        return item
    }
}