package com.github.kamunyan.leftcrafterdead.perk

import org.bukkit.Material

enum class PerkType {
    GUNSLINGER {
        override val perkName: String
            get() = "Gunslinger"
    },
    HELLRAIZER {
        override val perkName: String
            get() = "Hellraizer"
    },
    MEDIC {
        override val perkName: String
            get() = "Medic"
    },
    FIXER {
        override val perkName: String
            get() = "Fixer"
    },
    SLASHER {
        override val perkName: String
            get() = "Slasher"
    },
    EXTERMINATOR {
        override val perkName: String
            get() = "Exterminator"
    };

    abstract val perkName: String

    companion object {
        val perkTypeHashMap = hashMapOf(
            "Gunslinger" to GUNSLINGER,
            "Hellraizer" to HELLRAIZER,
            "Medic" to MEDIC,
            "Fixer" to FIXER,
            "Slasher" to SLASHER,
            "Exterminator" to EXTERMINATOR
        )

        fun getPerkType(material: Material): PerkType {
            return when (material) {
                Material.CROSSBOW -> GUNSLINGER
                Material.REDSTONE -> MEDIC
                else -> GUNSLINGER
            }
        }

        fun getPerkType(perkName: String): PerkType? {
            if (perkTypeHashMap.containsKey(perkName)) {
                return perkTypeHashMap[perkName]
            }
            return null
        }

        fun getPerk(level: Int, perkType: PerkType): Perk {
            return when (perkType) {
                GUNSLINGER -> Gunslinger(level)
                MEDIC -> Medic(level)
                else -> Gunslinger(level)
            }
        }
    }
}