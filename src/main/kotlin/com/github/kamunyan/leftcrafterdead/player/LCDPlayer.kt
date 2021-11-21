package com.github.kamunyan.leftcrafterdead.player

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.data.SQLDriver
import com.github.kamunyan.leftcrafterdead.perk.Gunslinger
import com.github.kamunyan.leftcrafterdead.perk.Perk
import com.github.kamunyan.leftcrafterdead.perk.PerkType
import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData
import com.github.kamunyan.leftcrafterdead.skill.type.*
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

    var gameMode: GameMode = GameMode.ADVENTURE

    var perk: Perk

    lateinit var primary: PrimaryWeapon

    var secondaryWeapon: SecondaryWeapon = HandGun("P226", WeaponType.Secondary)

    var campaignData: CampaignPlayerData = CampaignPlayerData(0, 0, 0)

    val playerData: PlayerData = PlayerData(uuid, 0, 0, 0, 0)

    val skillTree: LinkedHashMap<SkillType, SkillTree> = linkedMapOf(
        SkillType.MASTERMIND to MasterMind(),
        SkillType.ENFORCER to Enforcer(),
        SkillType.TECHNICIAN to Technician(),
        SkillType.GHOST to Ghost(),
        SkillType.FUGITIVE to Fugitive()
    )

    var skillPointLimit: Int = 0

    var skillPoint: Int = 0

    var statusData: StatusData = StatusData()

    init {
        loadPlayerData()
        val perkItem = player.inventory.getItem(8)
        perk = if (perkItem == null) {
            Gunslinger()
        } else {
            PerkType.getPerk(PerkType.getPerkType(perkItem.type))
        }
        perk.setSymbolItem(this)
        player.level = playerData.level
        player.totalExperience = playerData.exp
        setSkillPoint()
    }

    fun setPlayerStatus() {
        player.walkSpeed = statusData.walkSpeed
        player.healthScale = statusData.healthScaleAmount
        player.health = player.healthScale
    }

    private fun setSkillPoint() {
        skillPointLimit = 5 + player.level * 3
        var point = skillPointLimit
        skillTree.forEach { (_, u) ->
            point -= u.useSkillPoint
        }
        skillPoint = point
    }

    fun setStatusAllData() {
        statusData = StatusData()
        skillTree.forEach { (_, value) ->
            value.setStatusData(statusData)
        }
    }

    fun setLobbyItem() {
        val util = ItemMetaUtil
        player.inventory.clear()
        val diamond = util.generateMetaItem(Material.ENCHANTED_BOOK, "${ChatColor.AQUA}Main Menu")
        val endCrystal = util.generateMetaItem(Material.END_CRYSTAL, "${ChatColor.DARK_PURPLE}Select Perk")
        player.inventory.setItem(0, diamond)
        player.inventory.setItem(1, endCrystal)
        perk.setSymbolItem(this)
    }

    fun setSpectator() {
        clearInventory()
        gameMode = GameMode.SPECTATOR
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

    fun loadPlayerData() {
        SQLDriver.loadPlayerData(playerData)
    }

    fun updatePlayerData() {
        SQLDriver.savePlayerData(playerData)
    }

    fun initialize() {
        isMatchPlayer = false
        isSurvivor = false
        player.giveExp(campaignData.exp, false)
        playerData.totalKill += campaignData.kill
        playerData.exp += campaignData.exp
        playerData.level = player.level
        updatePlayerData()
        campaignData = CampaignPlayerData(0, 0, 0)
        statusData = StatusData()
        gameMode = GameMode.ADVENTURE
        setPerk()
        setPlayerStatus()
        setSkillPoint()
        setLobbyItem()
    }

    override fun toString(): String {
        return "${ChatColor.AQUA}${player.displayName}\n" +
                "${ChatColor.WHITE}uuid: ${player.uniqueId}\n" +
                "isMatchPlayer: $isMatchPlayer\n" +
                "isSurvivor: $isSurvivor\n" +
                "perk: ${perk.perkType}\n" +
                "exp: ${player.totalExperience}"
    }
}