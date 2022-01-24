package com.github.kamunyan.leftcrafterdead.weapons.primary

import com.github.kamunyan.leftcrafterdead.weapons.AmmoCategory
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class Shotgun(weaponTitle: String) : PrimaryWeapon(weaponTitle) {

    override fun getGunCategory(): GunCategory {
       return GunCategory.SHOTGUN
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
    }

    override fun getAmmoType(): AmmoCategory {
        return AmmoCategory.SHELL
    }
}