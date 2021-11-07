package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Concussion
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.AssaultRifle
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Fixer : Perk(PerkType.FIXER) {
    override fun perkSymbolMaterial(): Material {
        return Material.HONEYCOMB
    }

    override fun getGrenade(): Grenade {
        return Concussion()
    }

    override fun firstPrimaryWeapon(): PrimaryWeapon {
        return AssaultRifle("Mini21")
    }

    override fun perkGadgetItem(): ItemStack {
        return ItemMetaUtil.generateMetaItem(
            Material.HEART_OF_THE_SEA,
            "${ChatColor.GOLD}Armor Boost",
            110,
            listOf(
                "半径10mにいるプレイヤーに、",
                "最大体力の60%のアーマーを付与する"
            )
        )
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {
        startGadgetStartCoolDown(lcdPlayer)
        lcdPlayer.player.location.getNearbyPlayers(10.0).forEach { player ->
            val armorAmount = player.healthScale * 0.6
            player.absorptionAmount = player.absorptionAmount + armorAmount
        }
    }

    override val gadgetCoolDown: Int = 5
}