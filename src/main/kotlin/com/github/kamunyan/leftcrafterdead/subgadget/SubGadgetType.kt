package com.github.kamunyan.leftcrafterdead.subgadget

enum class SubGadgetType {
    HEAL_POTION {
        override fun getInstance(): SubGadget {
            return HealPotion
        }
    },
    TRIP_MINE {
        override fun getInstance(): SubGadget {
            return TripMine
        }
    },
    SENTRY_GUN {
        override fun getInstance(): SubGadget {
            return SentryGun
        }
    };

    abstract fun getInstance(): SubGadget
}