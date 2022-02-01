package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.MatchManager
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

    private val types = listOf(SupplyType.AMMO, SupplyType.SUB_GADGET, SupplyType.GRENADE)
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
        AMMO {
            override val displayMaterial = Material.FURNACE
            override fun rightInteract(player: Player) {
                val lcdPlayer = manager.getLCDPlayer(player)
                lcdPlayer.statusData.ammoLimits.forEach { (category, limit) ->
                    val amount = (limit * 0.5).toInt()
                    lcdPlayer.addAmmo(category, amount)
                }
                player.playSound(player.location, Sound.ENTITY_BAT_TAKEOFF, 1f, 0f)
                player.sendMessage("${ChatColor.AQUA}弾薬を補充しました")
            }
        },
        SUB_GADGET {
            override val displayMaterial = Material.CHEST
            override fun rightInteract(player: Player) {

            }
        },
        GRENADE {
            override val displayMaterial = Material.TNT
            override fun rightInteract(player: Player) {
                val lcdPlayer = manager.getLCDPlayer(player)
                lcdPlayer.perk.getGrenade().sendGrenade(player,2)
                player.playSound(player.location, Sound.ENTITY_BAT_TAKEOFF, 1f, 0f)
                player.sendMessage("${ChatColor.AQUA}グレネードを2個補充しました")
            }
        }
        ;

        abstract val displayMaterial: Material
        abstract fun rightInteract(player: Player)
    }
}