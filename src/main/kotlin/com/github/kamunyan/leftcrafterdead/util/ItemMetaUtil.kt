package com.github.kamunyan.leftcrafterdead.util

import com.github.kamunyan.leftcrafterdead.weapons.AmmoCategory
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ItemMetaUtil {

    fun generateMetaItem(material: Material, itemName: String): ItemStack {
        val item = ItemStack(material)
        val metaData = item.itemMeta
        metaData.setCustomModelData(1)
        metaData.setDisplayName(itemName)
        item.itemMeta = metaData
        return item
    }

    fun generateMetaItem(material: Material, itemName: String, data: Int): ItemStack {
        val item = generateMetaItem(material, itemName)
        val metadata = item.itemMeta
        metadata.setCustomModelData(data)
        item.itemMeta = metadata
        return item
    }

    fun generateMetaItem(material: Material, itemName: String, data: Int, lore: List<String>?): ItemStack {
        if (lore == null) {
            return generateMetaItem(material, itemName, data)
        }
        val item = generateMetaItem(material, itemName, data)
        val metadata = item.itemMeta
        metadata.lore = lore
        item.itemMeta = metadata
        return item
    }

    fun hasItemMetaCustomModelData(item: ItemStack?): Boolean {
        var flag = false
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.itemMeta.hasCustomModelData()) {
                    flag = true
                }
            }
        }
        return flag
    }

    fun createAmmunition(ammoType: AmmoCategory, amount: Int): ItemStack {
        val item = generateMetaItem(ammoType.material, "${ChatColor.AQUA}${ammoType.ammoCustomName}")
        item.amount = amount
        return item
    }
}