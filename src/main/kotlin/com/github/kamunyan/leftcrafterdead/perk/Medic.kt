package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.HealGrenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.primary.SubMachineGun
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Medic() : Perk(PerkType.MEDIC) {
    override fun perkSymbolMaterial(): Material {
        return Material.REDSTONE
    }

    override fun getGrenade(): Grenade {
        return HealGrenade()
    }

    override fun firstPrimaryWeapon(): PrimaryWeapon {
        return SubMachineGun("Mac10")
    }

    override fun perkGadgetItem(): ItemStack {
        return ItemMetaUtil.generateMetaItem(
            Material.GLOWSTONE_DUST,
            "${ChatColor.GOLD}Heal Pulse",
            110,
            listOf(
                "自身の半径10mにいる周囲のプレイヤーの体力を",
                "40%回復する"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        val location = lcdPlayer.player.location.clone()
        val players = location.getNearbyPlayers(10.0)
        players.forEach { player ->
            val healAmount = (player.healthScale * 0.4).toInt()
            if (player.health + healAmount > player.healthScale) {
                player.health = player.healthScale
                return@forEach
            }
            player.health = player.health + healAmount
        }
    }

    override val gadgetCoolDown: Int = 40
}