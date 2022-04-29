package com.github.kamunyan.leftcrafterdead.weapons

class Primary(val weapon: PrimaryType):LCDWeapon(weapon.weaponTitle,WeaponType.Primary) {
    override val slot: Int = 0
}