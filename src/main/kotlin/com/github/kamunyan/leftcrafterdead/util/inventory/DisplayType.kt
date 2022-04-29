package com.github.kamunyan.leftcrafterdead.util.inventory

import com.github.kamunyan.leftcrafterdead.util.inventory.subGadget.FirstSubGadgetSelectSlot
import com.github.kamunyan.leftcrafterdead.util.inventory.subGadget.SubGadgetDisplay
import com.github.kamunyan.leftcrafterdead.util.inventory.trader.*

enum class DisplayType(val inventorySlot:Int,val instance: InventoryDisplay) {
    TRADER_ENHANCEMENT(9,TraderEnhancement),
    TRADER_MENU(9,TraderMenu),
    TRADER_PRIMARY_WEAPON(54,TraderPrimary),
    TRADER_SECONDARY_WEAPON(54,TraderSecondary),
    SELECT_FIRST_SUB_GADGET(9, SubGadgetDisplay),
    SELECT_FIRST_SUB_GADGET_SLOT_SELECT(9, FirstSubGadgetSelectSlot),
}