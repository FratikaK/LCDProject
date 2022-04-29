package com.github.kamunyan.leftcrafterdead.weapons.grenade

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.weapons.LCDWeapon
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

abstract class Grenade(weaponTitle: String) : LCDWeapon(weaponTitle, WeaponType.Grenade) {

    abstract fun explosionEffects(location: Location)
    abstract fun specialEffects(attacker: Player, entity: LivingEntity)
    abstract val grenadeType: GrenadeType

    override val slot: Int = 2

    fun sendGrenade(player: Player, amount: Int) {
        if (weaponType != WeaponType.Grenade) {
            return
        }
        val grenade = crackShot.generateWeapon(weaponTitle)
        var add = 0
        val item = player.inventory.getItem(2)
        if (item != null) {
            add = item.amount
        }
        grenade.amount = (amount + add)
        if (MatchManager.getLCDPlayer(player).statusData.firstGrenadeAmount < grenade.amount) {
            grenade.amount = MatchManager.getLCDPlayer(player).statusData.firstGrenadeAmount
        }
        player.inventory.setItem(2, grenade)
    }

    override fun weaponDataList(): List<String> {
        return listOf()
    }

    companion object{
        const val money = 300
    }
}