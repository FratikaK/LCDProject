package com.github.kamunyan.leftcrafterdead.subgadget

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

object TripMine:SubGadget() {
    override val subGadgetName: String
        get() = "${ChatColor.GREEN}TRIP MINE"
    override val subGadgetType: SubGadgetType
        get() = SubGadgetType.TRIP_MINE
    override val customData: Int
        get() = 71
    override val limitAmount: Int
        get() = 4
    override val material: Material
        get() = Material.TNT
    override val lore: List<Component>
        get() = listOf(Component.text("自身の足元に地雷を設置する"))

    override fun rightInteract(player: Player) {
        val cs = LeftCrafterDead.instance.crackShot
        cs.spawnMine(player,player.location,"TRIP MINE")
    }
}