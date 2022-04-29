package com.github.kamunyan.leftcrafterdead.trader

import com.github.kamunyan.leftcrafterdead.MatchManager
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.util.ItemMetaUtil
import com.github.kamunyan.leftcrafterdead.util.MetadataUtil
import com.github.kamunyan.leftcrafterdead.util.inventory.DisplayType
import com.github.kamunyan.leftcrafterdead.util.inventory.InventoryDisplay
import com.github.kamunyan.leftcrafterdead.weapons.*
import com.github.kamunyan.leftcrafterdead.weapons.grenade.Grenade
import com.github.kamunyan.leftcrafterdead.weapons.grenade.GrenadeType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList

object Trader {
    private val manager = MatchManager

    private val u = ItemMetaUtil

    private val traderUUIDs = ArrayList<UUID>()

    fun showTraderDisplay(player: Player) {
        InventoryDisplay.switchDisplay(player, DisplayType.TRADER_MENU)
    }

    fun spawnTrader() {
        if (traderUUIDs.isNotEmpty()) {
            deleteTrader()
            traderUUIDs.clear()
        }
        if (manager.campaign.traderLocation != null) {
            val cart = manager.campaign.traderLocation!!.world.spawnEntity(
                manager.campaign.traderLocation!!,
                EntityType.MINECART
            ) as Minecart
            val uuid = cart.uniqueId
            cart.setDisplayBlockData(Material.GOLD_BLOCK.createBlockData())
            MetadataUtil.setSupplyMetadata(cart, MetadataUtil.TRADER_CART)
            cart.maxSpeed = 0.0
            cart.customName = "${ChatColor.GREEN}トレーダー"
            cart.isCustomNameVisible = true
            traderUUIDs.add(uuid)
        }
    }

    fun deleteTrader() {
        if (Bukkit.getWorld(manager.campaign.mapName) != null) {
            Bukkit.getWorld(manager.campaign.mapName)!!.entities.forEach {
                if (it.hasMetadata(MetadataUtil.TRADER_CART)) {
                    it.remove()
                }
            }
        }
        traderUUIDs.forEach {
            Bukkit.getEntity(it)?.remove()
        }
        traderUUIDs.clear()
    }

    fun tradePrimaryWeapon(player: Player, primary: PrimaryType) {
        val lcdPlayer = MatchManager.getLCDPlayer(player)
        if (!isTradeAble(lcdPlayer, primary.coin)) return


        lcdPlayer.campaignData.money -= primary.coin
        val level = lcdPlayer.primary.weaponLevel
        lcdPlayer.primary = Primary(primary)
        lcdPlayer.primary.weaponLevel = level - 1
        if (lcdPlayer.primary.weaponLevel < 0) {
            lcdPlayer.primary.weaponLevel = 0
        }
        lcdPlayer.primary.sendWeapon(player)

        sendTradeSuccessMessage(player, primary.weaponTitle)
    }

    fun tradeSecondaryWeapon(player: Player, secondary: SecondaryType) {
        val lcdPlayer = MatchManager.getLCDPlayer(player)
        if (!isTradeAble(lcdPlayer, secondary.coin)) return

        var coin = lcdPlayer.campaignData.money
        coin -= secondary.coin
        val level = lcdPlayer.secondaryWeapon.weaponLevel
        lcdPlayer.secondaryWeapon = Secondary(secondary)
        lcdPlayer.secondaryWeapon.weaponLevel = level - 1
        if (lcdPlayer.secondaryWeapon.weaponLevel < 0) {
            lcdPlayer.secondaryWeapon.weaponLevel = 0
        }
        lcdPlayer.secondaryWeapon.sendWeapon(player)

        sendTradeSuccessMessage(player, secondary.weaponTitle)
    }

    fun tradeGrenade(player: Player, grenade: GrenadeType) {
        val lcdPlayer = MatchManager.getLCDPlayer(player)
        val amount = player.inventory.getItem(2)?.amount
        if (amount != null) {
            if (amount >= lcdPlayer.statusData.firstGrenadeAmount && grenade == lcdPlayer.grenade.grenadeType) {
                player.sendMessage("${ChatColor.RED}所持上限です！")
                player.playSound(lcdPlayer.player.location, Sound.BLOCK_LAVA_POP, 1f, 0f)
                return
            }
        }
        if (!isTradeAble(lcdPlayer, Grenade.money)) return

        lcdPlayer.campaignData.money -= Grenade.money

        if (amount == null) {
            lcdPlayer.grenade = grenade.instance
            lcdPlayer.grenade.sendGrenade(player, 1)
            sendTradeSuccessMessage(player, grenade.weaponTitle)
            return
        }

        if (grenade == lcdPlayer.grenade.grenadeType) {
            lcdPlayer.grenade.sendGrenade(player, 1)
            player.sendMessage("${ChatColor.GOLD}${grenade.weaponTitle}${ChatColor.AQUA}を補充しました！")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
        } else {
            player.inventory.setItem(2, null)
            lcdPlayer.grenade = grenade.instance
            lcdPlayer.grenade.sendGrenade(player, 1)
            sendTradeSuccessMessage(player, grenade.weaponTitle)
        }
    }

    fun levelUpPrimary(player: Player) {
        val lcdPlayer = MatchManager.getLCDPlayer(player)
        val primary = lcdPlayer.primary
        val requireCoin = primary.weapon.coin * 2
        if (lcdPlayer.campaignData.money < requireCoin) {
            player.sendMessage("${ChatColor.RED}所持金が足りません！")
            player.playSound(lcdPlayer.player.location, Sound.BLOCK_LAVA_POP, 1f, 0f)
            return
        }
        if (!primary.levelUp()) {
            player.sendMessage("${ChatColor.RED}レベル上限です！")
            player.playSound(lcdPlayer.player.location, Sound.BLOCK_LAVA_POP, 1f, 0f)
        } else {
            lcdPlayer.campaignData.money -= requireCoin
            player.sendMessage("${ChatColor.GOLD}${primary.weaponTitle} Lv${primary.weaponLevel} ==>> Lv${primary.weaponLevel + 1}")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
            InventoryDisplay.switchDisplay(player, DisplayType.TRADER_ENHANCEMENT)
        }
    }

    fun levelUpSubGadget(player: Player, slot: Int) {
        val lcdPlayer = MatchManager.getLCDPlayer(player)
        val subGadget = lcdPlayer.subGadget[slot] ?: return
        if (lcdPlayer.campaignData.money < 500) {
            lcdPlayer.player.sendMessage("${ChatColor.RED}所持金が足りません！")
            lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.BLOCK_LAVA_POP, 1f, 0f)
            return
        }
        if (!subGadget.levelUp()) {
            player.sendMessage("${ChatColor.RED}レベル上限です！")
            player.playSound(lcdPlayer.player.location, Sound.BLOCK_LAVA_POP, 1f, 0f)
        } else {
            lcdPlayer.campaignData.money -= 500
            player.sendMessage("${ChatColor.GOLD}${subGadget.subGadgetName} Lv${subGadget.subGadgetLevel} ==>> Lv${subGadget.subGadgetLevel + 1}")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
            InventoryDisplay.switchDisplay(player, DisplayType.TRADER_ENHANCEMENT)
        }
    }

    private fun isTradeAble(lcdPlayer: LCDPlayer, weaponCoin: Int): Boolean {
        val flag = lcdPlayer.campaignData.money >= weaponCoin
        if (!flag) {
            lcdPlayer.player.sendMessage("${ChatColor.RED}所持金が足りません！")
            lcdPlayer.player.playSound(lcdPlayer.player.location, Sound.BLOCK_LAVA_POP, 1f, 0f)
        }
        return flag
    }

    private fun sendTradeSuccessMessage(player: Player, weaponTitle: String) {
        player.sendMessage("${ChatColor.GOLD}${weaponTitle}${ChatColor.AQUA}を購入しました！")
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
    }
}

