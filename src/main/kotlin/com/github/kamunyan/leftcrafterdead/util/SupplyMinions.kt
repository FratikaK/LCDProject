package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.subgadget.SubGadget
import com.github.kamunyan.leftcrafterdead.subgadget.SubGadgetType
import com.github.kamunyan.leftcrafterdead.weapons.WeaponUtil
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import java.util.*

class SupplyMinions {
    companion object {
        private val manager = MatchManager
        val supplies = HashMap<UUID, SupplyMinions>()
        fun spawnCampaignSupplies() {
            val locations = manager.campaign.supplyLocations
            supplies.clear()
            if (locations.isEmpty()) {
                return
            }
            locations.forEach {
                SupplyMinions().spawnSupply(it)
            }
        }
    }

    private val types = listOf(SupplyType.WEAPON_ENHANCEMENT, SupplyType.SUB_GADGET, SupplyType.GRENADE)
    private val retrievedPlayer = ArrayList<UUID>()
    lateinit var uuid: UUID
    private lateinit var supplyType: SupplyType

    fun spawnSupply(location: Location) {
        supplyType = types[Random().nextInt(types.size)]
        val cart = location.world.spawnEntity(location, EntityType.MINECART) as Minecart
        uuid = cart.uniqueId
        cart.setDisplayBlockData(supplyType.displayMaterial.createBlockData())
        MetadataUtil.setSupplyMetadata(cart, MetadataUtil.SUPPLY_CART)
        cart.maxSpeed = 0.0
        MetadataUtil.setSupplyMetadata(cart, MetadataUtil.SUPPLY_CART)
        cart.customName = supplyType.supplyName
        cart.isCustomNameVisible = true
        supplies[uuid] = this
    }

    fun retrievedSupply(player: Player) {
        if (retrievedPlayer.contains(player.uniqueId)) {
            player.sendMessage(Component.text("${ChatColor.RED}既にアイテムを受け取っています"))
            return
        }
        supplyType.rightInteract(player)
        retrievedPlayer.add(player.uniqueId)
    }

    enum class SupplyType {
        WEAPON_ENHANCEMENT {
            override val displayMaterial = Material.FURNACE
            override val supplyName: String = "${ChatColor.AQUA}武器強化"
            override fun rightInteract(player: Player) {
                val lcdPlayer = manager.getLCDPlayer(player)
                if (lcdPlayer.primary.levelUp()) {
                    player.sendMessage("${ChatColor.GOLD}${lcdPlayer.primary.weaponTitle}を強化！ Lv${lcdPlayer.primary.weaponLevel} ==>> Lv${lcdPlayer.primary.weaponLevel + 1}")
                } else {
                    if (lcdPlayer.secondaryWeapon.levelUp()) {
                        player.sendMessage("${ChatColor.GOLD}${lcdPlayer.secondaryWeapon.weaponTitle}を強化！ Lv${lcdPlayer.secondaryWeapon.weaponLevel} ==>> Lv${lcdPlayer.secondaryWeapon.weaponLevel + 1}")
                    } else {
                        player.sendMessage("${ChatColor.RED}武器のレベルが最大です")
                    }
                }
                player.playSound(player.location, Sound.ENTITY_BAT_TAKEOFF, 1f, 0f)
            }
        },
        SUB_GADGET {
            override val displayMaterial = Material.CHEST
            override val supplyName: String = "${ChatColor.YELLOW}サブガジェット強化"
            override fun rightInteract(player: Player) {
                val lcdPlayer = manager.getLCDPlayer(player)
                var subGadget: SubGadget? = null
                lcdPlayer.subGadget.forEach { (_, gadget) ->
                    if (gadget != null) {
                        if (subGadget == null) {
                            subGadget = gadget
                            return@forEach
                        }
                        if (gadget.subGadgetLevel < subGadget!!.subGadgetLevel) {
                            subGadget = gadget
                        }
                    }
                }
                if (subGadget == null) {
                    player.sendMessage("${ChatColor.RED}強化できるサブガジェットがありません")
                    return
                }
                if (subGadget!!.levelUp()) {
                    player.sendMessage("${ChatColor.GOLD}${subGadget!!.subGadgetName}を強化！ Lv${subGadget!!.subGadgetLevel} ==>> Lv${subGadget!!.subGadgetLevel + 1}")
                    subGadget!!.startCoolDown(lcdPlayer, 3)
                } else {
                    player.sendMessage("${ChatColor.RED}サブガジェットのレベルが最大です")
                }
                player.playSound(player.location, Sound.ENTITY_BAT_TAKEOFF, 1f, 0f)
            }
        },
        GRENADE {
            override val displayMaterial = Material.NETHER_QUARTZ_ORE
            override val supplyName: String = "${ChatColor.RED}グレネード補給"
            override fun rightInteract(player: Player) {
                val lcdPlayer = manager.getLCDPlayer(player)
                lcdPlayer.grenade.sendGrenade(player, 2)
                player.playSound(player.location, Sound.ENTITY_BAT_TAKEOFF, 1f, 0f)
                player.sendMessage("${ChatColor.AQUA}グレネードを補充しました")
            }
        }
        ;

        abstract val displayMaterial: Material
        abstract val supplyName: String
        abstract fun rightInteract(player: Player)
    }
}