package com.github.kamunyan.leftcrafterdead.player

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.perk.Gunslinger
import com.github.kamunyan.leftcrafterdead.perk.Perk
import com.github.kamunyan.leftcrafterdead.perk.PerkType
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.secondary.SecondaryWeapon
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class LCDPlayer(uuid: String) {
    private val manager = MatchManager

    val player: Player = Bukkit.getPlayer(UUID.fromString(uuid))!!

    var isMatchPlayer = false

    var isSurvivor = false

    var perk: Perk

    lateinit var primary: PrimaryWeapon

    lateinit var secondaryWeapon: SecondaryWeapon

    var kill: Int = 0

    val perkLevels = ConcurrentHashMap<PerkType, Int>()

    init {
        val perkIterator = listOf(
            PerkType.GUNSLINGER,
            PerkType.HELLRAIZER,
            PerkType.MEDIC,
            PerkType.FIXER,
            PerkType.SLASHER,
            PerkType.EXTERMINATOR
        )
        perkIterator.forEach { perkType -> perkLevels[perkType] = 0 }

        val perkItem = player.inventory.getItem(8)
        perk = if (perkItem == null) {
            Gunslinger(perkLevels.getValue(PerkType.GUNSLINGER))
        } else {
            when(perkItem.type){
                Material.CROSSBOW -> Gunslinger(perkLevels.getValue(PerkType.GUNSLINGER))
                else -> Gunslinger(perkLevels.getValue(PerkType.GUNSLINGER))
            }
        }
        perk.setSymbolItem(this)
    }

    override fun toString(): String {
        return "${ChatColor.AQUA}${player.displayName}\n" +
                "${ChatColor.WHITE}uuid: ${player.uniqueId}\n" +
                "isMatchPlayer: $isMatchPlayer\n" +
                "isSurvivor: $isSurvivor\n" +
                "perk: $perk\n" +
                "kill: $kill\n" +
                "perkLevels: $perkLevels\n"
    }
}