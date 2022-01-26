package com.github.kamunyan.leftcrafterdead.weapons

enum class GunCategory {
    ASSAULT_RIFLE {
        override fun getWeaponList(): List<String> {
            return listOf("AK12","Mini21")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.RIFLE
    },
    SUB_MACHINE_GUN {
        override fun getWeaponList(): List<String> {
            return listOf("MAC10")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.SUB
    },
    SHOTGUN {
        override fun getWeaponList(): List<String> {
            return listOf("M31")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.SHELL
    },
    HANDGUN {
        override fun getWeaponList(): List<String> {
            return listOf("P226")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.SUB
    },
    AKIMBO{
        override fun getWeaponList(): List<String> {
            return listOf()
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.SUB
    },
    GRENADE {
        override fun getWeaponList(): List<String> {
            return listOf("Grenade","Heal Grenade","Concussion","Cluster Bomb")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.UNKNOWN
    },
    UNKNOWN{
        override fun getWeaponList(): List<String> {
            return listOf()
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.UNKNOWN
    };

    abstract fun getWeaponList(): List<String>
    abstract val ammoType:AmmoCategory
}