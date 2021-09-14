package com.github.kamunyan.leftcrafterdead.util

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
}