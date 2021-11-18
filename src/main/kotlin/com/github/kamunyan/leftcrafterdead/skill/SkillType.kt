package com.github.kamunyan.leftcrafterdead.skill

import org.bukkit.Material

enum class SkillType {
    MASTERMIND {
        override val material: Material = Material.GLOWSTONE_DUST
    },
    ENFORCER {
        override val material: Material = Material.DIAMOND_CHESTPLATE
    },
    TECHNICIAN{
        override val material: Material = Material.ANVIL
              },
    GHOST{
        override val material: Material = Material.WITHER_SKELETON_SKULL
         },
    FUGITIVE{
        override val material: Material = Material.FEATHER
    };

    abstract val material: Material
}