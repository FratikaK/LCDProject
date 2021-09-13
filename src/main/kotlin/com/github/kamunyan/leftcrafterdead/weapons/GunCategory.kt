package com.github.kamunyan.leftcrafterdead.weapons

enum class GunCategory {
    ASSAULT_RIFLE {
        override fun getWeaponList(): List<String> {
            return listOf("AK12")
        }
    },
    SUB_MACHINE_GUN {
        override fun getWeaponList(): List<String> {
            return listOf()
        }
    },
    SHOTGUN {
        override fun getWeaponList(): List<String> {
            return listOf()
        }
    },
    HANDGUN {
        override fun getWeaponList(): List<String> {
            return listOf()
        }
    },
    GRENADE {
        override fun getWeaponList(): List<String> {
            return listOf("Grenade")
        }
    };

    abstract fun getWeaponList(): List<String>
}