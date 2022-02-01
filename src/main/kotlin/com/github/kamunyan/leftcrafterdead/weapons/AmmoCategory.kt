package com.github.kamunyan.leftcrafterdead.weapons

import org.bukkit.Material

enum class AmmoCategory {
    RIFLE {
        override val material: Material = Material.STICK
        override val ammoCustomName: String = "ライフルの弾"
        override val itemSlot = 9
    },
    SUB {
        override val material: Material = Material.IRON_NUGGET
        override val ammoCustomName: String = "ハンドガンの弾"
        override val itemSlot = 10
    },
    SHELL {
        override val material: Material = Material.GOLD_NUGGET
        override val ammoCustomName: String = "ショットガンの弾"
        override val itemSlot = 11
    },
    MAGNUM {
        override val material: Material = Material.BLAZE_ROD
        override val ammoCustomName: String = "マグナムの弾"
        override val itemSlot = 12
    },
    UNKNOWN {
        override val material: Material = Material.AIR
        override val ammoCustomName: String = ""
        override val itemSlot = 35
    };

    abstract val material: Material
    abstract val ammoCustomName: String
    abstract val itemSlot: Int
}