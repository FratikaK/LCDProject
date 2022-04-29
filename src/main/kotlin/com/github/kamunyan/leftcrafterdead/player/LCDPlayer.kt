package com.github.kamunyan.leftcrafterdead.player

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.data.SQLDriver
import com.github.kamunyan.leftcrafterdead.data.SkillTreeData
import com.github.kamunyan.leftcrafterdead.perk.Gunslinger
import com.github.kamunyan.leftcrafterdead.perk.Perk
import com.github.kamunyan.leftcrafterdead.perk.PerkType
import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData
import com.github.kamunyan.leftcrafterdead.skill.type.*
import com.github.kamunyan.leftcrafterdead.subgadget.*
import com.github.kamunyan.leftcrafterdead.util.Buff
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.inventory.DisplayType
import com.github.kamunyan.leftcrafterdead.weapons.Primary
import com.github.kamunyan.leftcrafterdead.weapons.Secondary
import com.github.kamunyan.leftcrafterdead.weapons.SecondaryType
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class LCDPlayer(val uuid: String) {
    private val manager = MatchManager

    val player: Player = Bukkit.getPlayer(UUID.fromString(uuid))!!

    var isMatchPlayer = false

    var isSurvivor = false

    var gameMode: GameMode = GameMode.ADVENTURE

    var perk: Perk

    lateinit var primary: Primary

    var firstPrimaryWeapon: Primary? = null

    var secondaryWeapon: Secondary = Secondary(SecondaryType.BERETTA_M9)

    var firstSecondaryWeapon: Secondary? = null

    lateinit var grenade: Grenade

    var firstGrenade: Grenade? = null

    val firstSubGadget = hashMapOf<Int, SubGadgetType?>(5 to null, 6 to null, 7 to null)

    val subGadget = hashMapOf<Int, SubGadget?>(5 to null, 6 to null, 7 to null)

    val buff = ArrayList<Buff>()

    var campaignData: CampaignPlayerData = CampaignPlayerData(0, 10000, 0)

    val playerData: PlayerData = PlayerData(uuid, 0, 0, 0, 0)

    val skillTree: LinkedHashMap<SkillType, SkillTree> = linkedMapOf(
        SkillType.MASTERMIND to MasterMind(),
        SkillType.ENFORCER to Enforcer(),
        SkillType.TECHNICIAN to Technician(),
        SkillType.GHOST to Ghost(),
        SkillType.FUGITIVE to Fugitive()
    )

    var displayView: DisplayType? = null

    var skillPoint: Int = 0

    var statusData: StatusData = StatusData()

    init {
        loadPlayerData()
        setPlayerStatus()
        val perkItem = player.inventory.getItem(8)
        perk = if (perkItem == null) {
            Gunslinger()
        } else {
            PerkType.getPerkInstance(PerkType.getPerkType(perkItem.type))
        }
        perk.setSymbolItem(this)
        setSkillPoint()
    }

    fun setPlayerStatus() {
        setSkillBuildStatus()
        player.walkSpeed = statusData.walkSpeed
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = statusData.healthScaleAmount
        player.absorptionAmount = statusData.armorLimit
        player.foodLevel = 20
        gameMode = GameMode.ADVENTURE
    }

    fun setSkillPoint() {
        val total = playerData.totalSkillPoint
        var used = 0
        skillTree.forEach { (_, u) ->
            u.skillMap.forEach { (index, isGet) ->
                if (isGet) {
                    used += SkillTree.requireSkillPoint[index]!!
                }
            }
        }
        skillPoint = total - used
    }

    private fun setSkillBuildStatus() {
        statusData = StatusData()
        skillTree.forEach { (_, value) ->
            value.setStatusData(statusData)
        }
    }

    fun sendFirstEquipment() {
        clearInventory()
        perk.setFirstWeapon(this)
        giveFirstSubGadget()
    }

    fun setLobbyItem() {
        val util = ItemMetaUtil
        player.inventory.clear()
        val diamond = util.generateMetaItem(Material.ENCHANTED_BOOK, "${ChatColor.AQUA}Main Menu")
        player.inventory.setItem(0, diamond)
        perk.setSymbolItem(this)
    }

    fun giveFirstSubGadget() {
        firstSubGadget.forEach { (t, u) ->
            val item: ItemStack
            if (u != null) {
                subGadget[t] = SubGadget.createSubGadgetInstance(t, u)
                item = subGadget[t]!!.generateItemStack(statusData)
            } else {
                item = SubGadget.nullItem()
            }
            player.inventory.setItem(t, item)
        }
    }

    fun setSpectator() {
        clearInventory()
        gameMode = GameMode.SPECTATOR
    }

    /**
     * Perkシンボル以外のアイテムを空にする
     */
    private fun clearInventory() {
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
        perk = PerkType.getPerkInstance(perk.perkType)
        perk.setSymbolItem(this)
    }

    @Synchronized
    fun setPerk(perkType: PerkType) {
        perk = PerkType.getPerkInstance(perkType)
        perk.setSymbolItem(this)
        player.sendMessage(
            "${ChatColor.AQUA}Perkを${ChatColor.LIGHT_PURPLE}" +
                    "${perkType.perkName}${ChatColor.AQUA}に変更しました！"
        )
        player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 0f)
    }

    private fun loadPlayerData() {
        SQLDriver.loadPlayerData(playerData)
        SkillTreeData.loadSkillTree(this)
    }

    fun updatePlayerData() {
        SQLDriver.savePlayerData(playerData)
        SkillTreeData.saveSkillTree(this)
    }

    fun initialize() {
        isMatchPlayer = false
        isSurvivor = false
        buff.clear()
        playerData.totalKill += campaignData.kill
        PlayerData.addExp(campaignData.exp, playerData)
        updatePlayerData()
        campaignData = CampaignPlayerData(0, 0, 0)
        setPlayerStatus()
        setPerk()
        setLobbyItem()
    }

    override fun toString(): String {
        return "${ChatColor.AQUA}${player.name}\n" +
                "${ChatColor.WHITE}uuid: ${player.uniqueId}\n" +
                "isMatchPlayer: $isMatchPlayer\n" +
                "isSurvivor: $isSurvivor\n" +
                "perk: ${perk.perkType}\n" +
                "exp: ${player.totalExperience}"
    }
}