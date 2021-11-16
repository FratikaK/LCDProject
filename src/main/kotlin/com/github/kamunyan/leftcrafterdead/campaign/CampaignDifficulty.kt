package com.github.kamunyan.leftcrafterdead.campaign

enum class CampaignDifficulty {
    NORMAL {
        override val normalMobSpawnAmount: Int = 3
        override val expRate: Int = 1
    },
    ADVANCED {
        override val normalMobSpawnAmount: Int = 5
        override val expRate: Int = 2
    },
    EXPERT {
        override val normalMobSpawnAmount: Int = 8
        override val expRate: Int = 3
    };

    abstract val normalMobSpawnAmount: Int
    abstract val expRate: Int
}