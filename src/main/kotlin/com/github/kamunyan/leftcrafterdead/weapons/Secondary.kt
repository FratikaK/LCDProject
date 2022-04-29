package com.github.kamunyan.leftcrafterdead.weapons

class Secondary(val weapon:SecondaryType): LCDWeapon(weapon.weaponTitle,WeaponType.Secondary) {
    override val slot: Int = 1
}