package com.github.kamunyan.leftcrafterdead.skill

import org.bukkit.Material

enum class SkillType {
    MASTERMIND {
        override val material: Material = Material.GLOWSTONE_DUST
        override val skillItemData: Int = 550
    },
    ENFORCER {
        override val material: Material = Material.DIAMOND_CHESTPLATE
        override val skillItemData: Int = 551
    },
    TECHNICIAN {
        override val material: Material = Material.ANVIL
        override val skillItemData: Int = 552
    },
    GHOST {
        override val material: Material = Material.WITHER_SKELETON_SKULL
        override val skillItemData: Int = 553
    },
    FUGITIVE {
        override val material: Material = Material.FEATHER
        override val skillItemData: Int = 554
    };

    abstract val material: Material
    abstract val skillItemData: Int
}