package com.github.kamunyan.leftcrafterdead.subgadget

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.skill.StatusData
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TripMine(slot:Int) : SubGadget(slot) {
    override val subGadgetName: String
        get() = "${ChatColor.GREEN}TRIP MINE"
    override val subGadgetType: SubGadgetType
        get() = SubGadgetType.TRIP_MINE
    override val customData: Int
        get() = 71
    override val coolDown: Int
        get() = 60
    override val material: Material
        get() = Material.TNT
    override val lore: List<Component>
        get() = listOf(Component.text("自身の足元に地雷を設置する"))

    override fun rightInteract(player: Player) {
        if (isCooldown){
            return
        }
        startCoolDown(MatchManager.getLCDPlayer(player),coolDown - (10 * subGadgetLevel))
        val cs = LeftCrafterDead.instance.crackShot
        cs.spawnMine(player, player.location, "TRIP MINE")
    }

    override fun generateItemStack(data: StatusData): ItemStack {
        val item = super.generateItemStack(data)
        item.amount = data.sentryGunAmount
        return item
    }
}