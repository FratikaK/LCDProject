package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.MatchManager
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import java.util.*

class SupplyMinions {
    companion object{
        private val manager = MatchManager
        val supplies = HashMap<UUID,SupplyMinions>()
        fun setCampaignSpawnSupplies(){
            val locations = manager.campaign.supplyLocations
            supplies.clear()
            if (locations[manager.gameProgress] == null || locations[manager.gameProgress]!!.isEmpty()){
                return
            }
            locations[manager.gameProgress]!!.forEach {
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

    fun retrievedSupply(player: Player){
        if (retrievedPlayer.contains(player.uniqueId)){
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

            }
        }
        ;

        abstract val displayMaterial: Material
        abstract fun rightInteract(player:Player)
    }
}