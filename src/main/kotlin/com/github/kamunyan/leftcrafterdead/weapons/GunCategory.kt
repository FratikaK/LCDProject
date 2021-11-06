package com.github.kamunyan.leftcrafterdead.weapons

enum class GunCategory {
    ASSAULT_RIFLE {
        override fun getWeaponList(): List<String> {
            return listOf("AK12","Mini21")
        }
    },
    SUB_MACHINE_GUN {
        override fun getWeaponList(): List<String> {
            return listOf("MAC10")
        }
    },
    SHOTGUN {
        override fun getWeaponList(): List<String> {
            return listOf()
        }
    },
    HANDGUN {
        override fun getWeaponList(): List<String> {
            return listOf("P226")
        }
    },
    GRENADE {
        override fun getWeaponList(): List<String> {
            return listOf("Grenade","Heal Grenade","Concussion")
        }
    };

    abstract fun getWeaponList(): List<String>
}