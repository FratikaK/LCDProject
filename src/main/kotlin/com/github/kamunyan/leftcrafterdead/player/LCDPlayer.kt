package com.github.kamunyan.leftcrafterdead.player

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.perk.Gunslinger
import com.github.kamunyan.leftcrafterdead.perk.Perk
import com.github.kamunyan.leftcrafterdead.perk.PerkType
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.secondary.SecondaryWeapon
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
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

    var walkSpeed: Float = 0.2f

    var healthScale = 20.0

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
            when (perkItem.type) {
                Material.CROSSBOW -> Gunslinger(perkLevels.getValue(PerkType.GUNSLINGER))
                else -> Gunslinger(perkLevels.getValue(PerkType.GUNSLINGER))
            }
        }
        perk.setSymbolItem(this)
    }

    fun setLobbyItem() {
        val util = ItemMetaUtil
        player.inventory.clear()
        val diamond = util.generateMetaItem(Material.DIAMOND, "${ChatColor.AQUA}Join Game")
        val endCrystal = util.generateMetaItem(Material.END_CRYSTAL, "${ChatColor.DARK_PURPLE}Select Perk")
        player.inventory.setItem(0, diamond)
        player.inventory.setItem(1, endCrystal)
        perk.setSymbolItem(this)
    }

    fun setSpectator() {
        clearInventory()
        player.gameMode = GameMode.SPECTATOR
    }

    /**
     * Perkシンボル以外のアイテムを空にする
     */
    fun clearInventory() {
        val symbolItem = player.inventory.getItem(8)
        if (symbolItem == null) {
            player.inventory.clear()
            return
        }
        player.inventory.clear()
        player.inventory.setItem(8, symbolItem)
    }

    /**
     * 処理しているPerkのアイテムから
     * Perkのインスタンスをセットする
     */
    fun setPerk() {
        val perkItem = player.inventory.getItem(8)
        if (perkItem == null) {
            perk = Gunslinger(perkLevels.getValue(PerkType.GUNSLINGER))
            return
        }
        val perkType = PerkType.getPerkType(perkItem.type)
        var perkLevel = perkLevels[perkType]
        if (perkLevel == null) perkLevel = 0
        perk = PerkType.getPerk(perkLevel, perkType)
    }

    fun changePerk(perkType: PerkType) {
    }

    fun setSpeed(speed: Float) {
        this.walkSpeed = speed
        player.walkSpeed = walkSpeed
    }

    fun initialize() {
        isMatchPlayer = false
        isSurvivor = false
        player.gameMode = GameMode.ADVENTURE
        setPerk()
        setSpeed(0.2f)
        setLobbyItem()
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