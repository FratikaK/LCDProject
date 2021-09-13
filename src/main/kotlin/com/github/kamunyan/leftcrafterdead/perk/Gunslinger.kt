package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.weapons.grenade.FlagGrenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.primary.SubMachineGun

class Gunslinger(level: Int) :Perk(level, PerkType.GUNSLINGER) {
    override fun getGrenade(): Grenade {
        return FlagGrenade()
    }

    override fun firstPrimaryWeapon(): PrimaryWeapon {
        return SubMachineGun("MAC10")
    }
}