package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.weapons.grenade.FlagGrenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.AssaultRifle
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Gunslinger : Perk(PerkType.GUNSLINGER) {
    override fun perkSymbolMaterial(): Material {
        return Material.CROSSBOW
    }

    override fun getGrenade(): Grenade {
        return FlagGrenade()
    }

    override fun firstPrimaryWeapon(): PrimaryWeapon {
        return AssaultRifle("AK12")
    }

    override fun perkGadgetItem(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun gadgetRightInteract(lcdPlayer: LCDPlayer) {

    }

    override val gadgetCoolDown: Int = 40
}