package com.github.kamunyan.leftcrafterdead.skill.type

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData

class Enforcer() : SkillTree() {
    override val skillType: SkillType = SkillType.ENFORCER
    override fun setStatusData(data: StatusData) {
        skillMap.forEach { (index, flag) ->
            if (!flag) return@forEach
            when (index) {
                49 -> data.weaponDamageMultiplier += 0.05
                38 -> data.specialSkillTypes.add(SpecialSkillType.UNDERDOG)
                40 -> data.damageResistMultiplier -= 0.1
                42 -> data.addMoneyMultiplier += 0.2
                29 -> data.shotgunReloadSpeedAcceleration -= 0.15
                31 -> data.specialSkillTypes.add(SpecialSkillType.DIE_HARD)
                33 -> data.magazineAmountMultiplier += 0.15
                20 -> data.shotgunDamageMultiplier += 0.15
                22 -> data.specialSkillTypes.add(SpecialSkillType.BULLSEYE)
                24 -> data.addShotgunMagazine += 8
                11 -> data.specialSkillTypes.add(SpecialSkillType.CLOSE_BY)
                13 -> data.specialSkillTypes.add(SpecialSkillType.MONEY_GRUBBER)
                15 -> data.magazineAmountMultiplier += 0.3
                2 -> {
                    data.damageResistMultiplier -= 0.3
                    data.walkSpeed *= 0.9f
                }
                4 -> data.weaponDamageMultiplier += 0.3
                6 -> data.specialSkillTypes.add(SpecialSkillType.FULLY_LOADED)
            }
        }

    }
}