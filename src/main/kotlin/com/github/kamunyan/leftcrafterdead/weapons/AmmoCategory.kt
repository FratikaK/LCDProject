package com.github.kamunyan.leftcrafterdead.weapons

import org.bukkit.Material

enum class AmmoCategory {
    RIFLE {
        override val material: Material = Material.STICK
        override val ammoCustomName: String = "ライフルの弾"
        override val itemSlot: ArrayList<Int> = arrayListOf(9,10,11,12,13,14,15,16,17)
    },
    SUB {
        override val material: Material = Material.IRON_NUGGET
        override val ammoCustomName: String = "ハンドガンの弾"
        override val itemSlot: ArrayList<Int> = arrayListOf(18,19,20,21,22,23,24,25,26)
    },
    SHELL {
        override val material: Material = Material.GOLD_NUGGET
        override val ammoCustomName: String = "ショットガンの弾"
        override val itemSlot: ArrayList<Int> = arrayListOf(27,28,29,30,31,32)
    },
    MAGNUM {
        override val material: Material = Material.BLAZE_ROD
        override val ammoCustomName: String = "マグナムの弾"
        override val itemSlot: ArrayList<Int> = arrayListOf(33,34,35)
    },
    UNKNOWN {
        override val material: Material = Material.AIR
        override val ammoCustomName: String = ""
        override val itemSlot: ArrayList<Int> = arrayListOf()
    };

    abstract val material: Material
    abstract val ammoCustomName: String
    abstract val itemSlot: ArrayList<Int>
}