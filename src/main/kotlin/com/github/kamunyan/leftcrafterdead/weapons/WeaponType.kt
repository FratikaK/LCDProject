package com.github.kamunyan.leftcrafterdead.weapons

enum class WeaponType {
    Primary {
        override fun getWeaponSlot(): Int{
            return 0
        }
    },
    Secondary {
        override fun getWeaponSlot(): Int {
            return 1
        }
    },
    Grenade {
        override fun getWeaponSlot(): Int {
            return 2
        }
    },
    UNKNOWN {
        override fun getWeaponSlot(): Int {
            return -1
        }
    };

    abstract fun getWeaponSlot():Int
}