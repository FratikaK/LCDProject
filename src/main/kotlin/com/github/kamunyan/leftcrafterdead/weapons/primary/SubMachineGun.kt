package com.github.kamunyan.leftcrafterdead.weapons.primary

import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class SubMachineGun(weaponTitle: String) :PrimaryWeapon(weaponTitle){

    override fun getGunCategory(): GunCategory {
        return GunCategory.SUB_MACHINE_GUN
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
    }
}