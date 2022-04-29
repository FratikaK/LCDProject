package com.github.kamunyan.leftcrafterdead.weapons.grenade

enum class GrenadeType(val weaponTitle: String, val instance: Grenade) {
    FLAG_GRENADE("Flag Grenade", FlagGrenade),
    HEAL_GRENADE("Heal Grenade", HealGrenade),
    CONCUSSION("Concussion", Concussion),
    CLUSTER_BOMB("Cluster Bomb", ClusterBomb);
}