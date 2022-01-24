package com.github.kamunyan.leftcrafterdead.weapons

import org.bukkit.Material

enum class AmmoCategory {
    RIFLE {
        override val material: Material = Material.STICK
        override val ammoCustomName: String = "ライフルの弾"
    },
    SUB {
        override val material: Material = Material.IRON_NUGGET
        override val ammoCustomName: String = "ハンドガンの弾"
    },
    SHELL {
        override val material: Material = Material.GOLD_NUGGET
        override val ammoCustomName: String = "ショットガンの弾"
    },
    MAGNUM {
        override val material: Material = Material.BLAZE_ROD
        override val ammoCustomName: String = "マグナムの弾"
    },
    UNKNOWN {
        override val material: Material = Material.AIR
        override val ammoCustomName: String = ""
    };

    abstract val material: Material
    abstract val ammoCustomName: String
}