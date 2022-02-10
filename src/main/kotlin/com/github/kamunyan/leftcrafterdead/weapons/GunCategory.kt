package com.github.kamunyan.leftcrafterdead.weapons

enum class GunCategory {
    ASSAULT_RIFLE {
        override fun getWeaponList(): ArrayList<String> {
            return arrayListOf("AK12","Mini21")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.RIFLE
    },
    SUB_MACHINE_GUN {
        override fun getWeaponList(): ArrayList<String> {
            return arrayListOf("MAC10")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.SUB
    },
    SHOTGUN {
        override fun getWeaponList(): ArrayList<String> {
            return arrayListOf("M31")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.SHELL
    },
    HANDGUN {
        override fun getWeaponList(): ArrayList<String> {
            return arrayListOf("P226")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.SUB
    },
    AKIMBO{
        override fun getWeaponList(): ArrayList<String> {
            return arrayListOf()
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.SUB
    },
    GRENADE {
        override fun getWeaponList(): ArrayList<String> {
            return arrayListOf("Grenade","Heal Grenade","Concussion","Cluster Bomb")
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.UNKNOWN
    },
    UNKNOWN{
        override fun getWeaponList(): ArrayList<String> {
            return arrayListOf()
        }

        override val ammoType: AmmoCategory
            get() = AmmoCategory.UNKNOWN
    };

    abstract fun getWeaponList(): ArrayList<String>
    abstract val ammoType:AmmoCategory
}