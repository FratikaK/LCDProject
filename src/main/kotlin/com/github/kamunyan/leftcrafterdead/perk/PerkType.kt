package com.github.kamunyan.leftcrafterdead.perk

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
}