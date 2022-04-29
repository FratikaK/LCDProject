package com.github.kamunyan.leftcrafterdead.weapons

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class LCDWeapon(val weaponTitle: String, val weaponType: WeaponType) {

    companion object {
        const val DAMAGE = ".Shooting.Projectile_Damage"
        const val SPEED = ".Shooting.Projectile_Speed"
        const val SPREAD = ".Shooting.Bullet_Spread"
        const val AMOUNT = ".Shooting.Projectile_Amount"
        const val FIRE_RATE = ".Fully_Automatic.Fire_Rate"
        const val RELOAD_AMOUNT = ".Reload.Reload_Amount"
        const val RELOAD_DURATION = ".Reload.Reload_Duration"

        private val fireRate =
            listOf(300, 360, 420, 480, 540, 600, 660, 720, 780, 840, 900, 960, 1020, 1080, 1140, 1200)

        fun weaponInformation(weaponTitle: String): ArrayList<Component> {
            val cs = LeftCrafterDead.instance.crackShot.handle
            val spread = 100 - (cs.getDouble("${weaponTitle}${SPREAD}") * 10)
            val rate = if (cs.getInt("${weaponTitle}${FIRE_RATE}") != 0) {
                fireRate[cs.getInt("${weaponTitle}${FIRE_RATE}")]
            } else {
                0
            }
            val list = ArrayList<Component>()
            val y = ChatColor.YELLOW
            val a = ChatColor.AQUA
            list.add(Component.text("${y}威力: ${a}${cs.getInt("${weaponTitle}${DAMAGE}")}"))
            list.add(Component.text("${y}精度: ${a}${spread}%"))
            list.add(Component.text("${y}レート: ${a}${rate}"))
            list.add(Component.text("${y}マガジン: ${a}${cs.getInt("${weaponTitle}${RELOAD_AMOUNT}")}"))
            list.add(Component.text("${y}リロード時間: ${a}${cs.getInt("${weaponTitle}${RELOAD_DURATION}")}"))
            return list
        }
    }

    protected val crackShot = LeftCrafterDead.instance.crackShot

    private fun weaponDataInfo(infoName: String, node: String): String {
        return "${ChatColor.YELLOW}${infoName}${ChatColor.WHITE}: " +
                "${ChatColor.RED}${crackShot.handle.getInt(weaponTitle + node)}"
    }

    abstract val slot: Int

    var weaponLevel = 0

    fun levelUp(): Boolean {
        if (weaponLevel >= 4) {
            return false
        }
        weaponLevel += 1
        return true
    }

    fun sendWeapon(player: Player) {
        val weapon = getWeaponItemStack() ?: return
        if (weaponType == WeaponType.UNKNOWN) return
        player.inventory.setItem(weaponType.getWeaponSlot(), weapon)
    }

    private fun getWeaponItemStack(): ItemStack? {
        return crackShot.generateWeapon(weaponTitle)
    }

    open fun weaponDataList(): List<String> {
        return listOf(
            weaponDataInfo("威力", DAMAGE),
            weaponDataInfo("弾速", SPEED),
            weaponDataInfo("精度", SPREAD),
            weaponDataInfo("弾数", AMOUNT),
            weaponDataInfo("レートLv", FIRE_RATE),
            weaponDataInfo("装弾数", RELOAD_AMOUNT),
            weaponDataInfo("リロード速度", RELOAD_DURATION),
        )
    }
}