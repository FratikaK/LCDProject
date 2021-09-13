package com.github.kamunyan.leftcrafterdead.perk

import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon

abstract class Perk(var level: Int,val perkType: PerkType) {
    abstract fun getGrenade(): Grenade
    abstract fun firstPrimaryWeapon():PrimaryWeapon
}