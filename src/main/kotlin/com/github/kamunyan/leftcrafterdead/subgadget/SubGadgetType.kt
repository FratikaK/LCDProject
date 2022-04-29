package com.github.kamunyan.leftcrafterdead.subgadget

import org.bukkit.ChatColor
import org.bukkit.Material

enum class SubGadgetType(val material: Material, val itemName: String, val lore: List<String>) {
    HEAL_POTION(Material.POTION, "回復のポーション", listOf("${ChatColor.AQUA}自身の体力を少量回復する")),
    TRIP_MINE(
        Material.TNT, "TRIP MINE", listOf(
            "${ChatColor.AQUA}自身の足元に地雷を設置する",
            "${ChatColor.AQUA}敵が地雷に触れるか、",
            "${ChatColor.AQUA}地雷を攻撃すると起爆する"
        )
    ),
    SENTRY_GUN(
        Material.PUMPKIN, "セントリーガン", listOf(
            "${ChatColor.AQUA}自身の足元にセントリーガンを設置する",
            "${ChatColor.AQUA}火力は設置したプレイヤーの",
            "${ChatColor.AQUA}攻撃力に依存する"
        )
    ),
    PAIN_KILLER(Material.PUMPKIN_SEEDS,"鎮痛剤", listOf("${ChatColor.AQUA}自身の体力を一時的に増やす"));
}