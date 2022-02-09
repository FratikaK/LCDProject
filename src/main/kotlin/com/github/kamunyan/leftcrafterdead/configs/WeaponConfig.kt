package com.github.kamunyan.leftcrafterdead.configs

import com.github.kamunyan.leftcrafterdead.util.Trader
import com.github.kamunyan.leftcrafterdead.weapons.GunCategory
import org.bukkit.configuration.ConfigurationSection

object WeaponConfig : Config("weapon", "weapon.yml") {
    override fun loadConfig() {
        if (yml.contains("Weapon")) {
            val section = yml.getConfigurationSection("Weapon")!!
            section.getKeys(false).forEach {
                val category: GunCategory = when (it) {
                    "Assault" -> GunCategory.ASSAULT_RIFLE
                    "Sub" -> GunCategory.SUB_MACHINE_GUN
                    "ShotGun" -> GunCategory.SHOTGUN
                    "HandGun" -> GunCategory.HANDGUN
                    else -> return@forEach
                }
                if (section.getConfigurationSection(it) != null) {
                    loadWeapons(section.getConfigurationSection(it)!!, category)
                }
            }
        }

        if (yml.contains("Grenade") && yml.getInt("Grenade.money") != 0) {
            Trader.weaponShopList[GunCategory.GRENADE]!!["Grenade"] = yml.getInt("Grenade.money")
        }
    }

    private fun loadWeapons(section: ConfigurationSection, category: GunCategory) {
        section.getKeys(false).forEach {
            if (!category.getWeaponList().contains(it)) {
                category.getWeaponList().add(it)
            }
            Trader.weaponShopList[category]!![it] = section.getInt("${it}.money")
        }
    }
}