package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.HealGrenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.primary.SubMachineGun
import org.bukkit.Material

class Medic(level: Int) : Perk(level, PerkType.MEDIC) {
    override fun perkSymbolMaterial(): Material {
        return Material.REDSTONE
    }

    override fun getGrenade(): Grenade {
        return HealGrenade()
    }

    override fun firstPrimaryWeapon(): PrimaryWeapon {
        return SubMachineGun("Mac10")
    }
}