package com.github.kamunyan.leftcrafterdead.perk

import org.bukkit.Material

enum class PerkType {
    GUNSLINGER {
        override val perkName: String
            get() = "Gunslinger"
    },
    MEDIC {
        override val perkName: String
            get() = "Medic"
    },
    FIXER {
        override val perkName: String
            get() = "Fixer"
    },
    EXTERMINATOR {
        override val perkName: String
            get() = "Exterminator"
    };

    abstract val perkName: String

    companion object {
        val perkTypeHashMap = hashMapOf(
            "Gunslinger" to GUNSLINGER,
            "Medic" to MEDIC,
            "Fixer" to FIXER,
            "Exterminator" to EXTERMINATOR
        )

        fun getPerkType(material: Material): PerkType {
            return when (material) {
                Material.CROSSBOW -> GUNSLINGER
                Material.REDSTONE -> MEDIC
                Material.HONEYCOMB -> FIXER
                Material.NETHERITE_INGOT -> EXTERMINATOR
                else -> GUNSLINGER
            }
        }

        fun getPerkType(perkName: String): PerkType? {
            if (perkTypeHashMap.containsKey(perkName)) {
                return perkTypeHashMap[perkName]
            }
            return null
        }

        fun getPerkInstance(perkType: PerkType): Perk {
            return when (perkType) {
                GUNSLINGER -> Gunslinger()
                MEDIC -> Medic()
                FIXER -> Fixer()
                EXTERMINATOR -> Exterminator()
                else -> Gunslinger()
            }
        }
    }
}