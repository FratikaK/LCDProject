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
    },
    PAIN_KILLER{
        override fun getInstance(): SubGadget {
            return PainKiller
        }
    };

    abstract fun getInstance(): SubGadget

    companion object{
        val subGadgetLists = listOf(HEAL_POTION,TRIP_MINE,SENTRY_GUN,PAIN_KILLER)
    }
}