package com.github.kamunyan.leftcrafterdead.weapons.secondary

import com.github.kamunyan.leftcrafterdead.weapons.AmmoCategory
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class HandGun(weaponTitle: String, weaponType: WeaponType) :SecondaryWeapon(weaponTitle, weaponType) {
    override fun getGunCategory(): GunCategory {
        return GunCategory.HANDGUN
    }

    override fun specialEffects(attacker: Player, entity: LivingEntity) {
    }

    override fun weaponDataList(): List<String> {
        return listOf()
    }

    override fun getAmmoType(): AmmoCategory {
        return AmmoCategory.SUB
    }
}