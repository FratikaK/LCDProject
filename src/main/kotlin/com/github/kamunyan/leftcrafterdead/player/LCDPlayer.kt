package com.github.kamunyan.leftcrafterdead.player

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.perk.Gunslinger
import com.github.kamunyan.leftcrafterdead.perk.Perk
import com.github.kamunyan.leftcrafterdead.perk.PerkType
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import com.github.kamunyan.leftcrafterdead.weapons.primary.PrimaryWeapon
import com.github.kamunyan.leftcrafterdead.weapons.secondary.HandGun
import com.github.kamunyan.leftcrafterdead.weapons.secondary.SecondaryWeapon
import org.bukkit.*
import org.bukkit.entity.Player
import java.util.*

class LCDPlayer(uuid: String) {
    private val manager = MatchManager

    val player: Player = Bukkit.getPlayer(UUID.fromString(uuid))!!

    var isMatchPlayer = false

    var isSurvivor = false

    var perk: Perk

    lateinit var primary: PrimaryWeapon

    var secondaryWeapon: SecondaryWeapon = HandGun("P226", WeaponType.Secondary)

    var kill: Int = 0

    var walkSpeed: Float = 0.2f

    var healthScale = 20.0

    var baseDamage = 0.0

    lateinit var playerData: PlayerData

    init {
        val perkItem = player.inventory.getItem(8)
        if (perkItem == null) {
            perk = Gunslinger()
        } else {
            perk = PerkType.getPerk(PerkType.getPerkType(perkItem.type))
        }
        perk.setSymbolItem(this)
        player.gameMode = GameMode.SURVIVAL
    }

    fun setPlayerStatus() {
        player.walkSpeed = walkSpeed
        player.healthScale = healthScale
        player.health = player.healthScale
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
    @Synchronized
    fun setPerk() {
        perk = PerkType.getPerk(perk.perkType)
        perk.setSymbolItem(this)
    }

    @Synchronized
    fun setPerk(perkType: PerkType) {
        perk = PerkType.getPerk(perkType)
        perk.setSymbolItem(this)
        player.sendMessage(
            "${ChatColor.AQUA}Perkを${ChatColor.LIGHT_PURPLE}" +
                    "${perkType.perkName}${ChatColor.AQUA}に変更しました！"
        )
        player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 0f)
    }

    fun setSpeed(speed: Float) {
        this.walkSpeed = speed
        player.walkSpeed = walkSpeed
    }

    fun loadPlayerData() {}

    fun updatePlayerData() {}

    fun initialize() {
        isMatchPlayer = false
        isSurvivor = false
        player.gameMode = GameMode.ADVENTURE
        setPerk()
        setSpeed(0.2f)
        setPlayerStatus()
        setLobbyItem()
    }

    override fun toString(): String {
        return "${ChatColor.AQUA}${player.displayName}\n" +
                "${ChatColor.WHITE}uuid: ${player.uniqueId}\n" +
                "isMatchPlayer: $isMatchPlayer\n" +
                "isSurvivor: $isSurvivor\n" +
                "perk: $perk\n" +
                "kill: $kill\n"
    }
}