package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.weapons.grenade.FlagGrenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.AssaultRifle
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.primary.SubMachineGun
import org.bukkit.Material

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
}