package com.github.kamunyan.leftcrafterdead.skill.type

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData

class Enforcer() : SkillTree() {
    override val skillType: SkillType = SkillType.ENFORCER
    override fun setStatusData(data: StatusData) {
        skillMap.forEach{ (index, flag) ->
            if (!flag) return@forEach
            when(index){
                49 -> data.weaponDamageMultiplier += 0.05
                38 -> data.specialSkillTypes.add(SpecialSkillType.UNDERDOG)
                40 -> data.armorRecovery -= 0.15
                42 -> data.addMoneyMultiplier += 0.2
                29 -> data.shotgunReloadSpeedAcceleration -= 0.15
                31 -> data.armorLimit += 6.0
                33 -> data.magazineAmountMultiplier += 0.15
                20 -> data.shotgunDamageMultiplier += 0.15
                22 -> data.specialSkillTypes.add(SpecialSkillType.BULLSEYE)
                24 -> data.ammunitionAmountMultiplier += 0.5
                11 -> data.specialSkillTypes.add(SpecialSkillType.CLOSE_BY)
                13 -> data.armorRecovery -= 0.3
                15 -> data.magazineAmountMultiplier += 0.3
                2 -> data.armorLimit += 10.0
                4 -> data.weaponDamageMultiplier += 0.3
                6 -> data.specialSkillTypes.add(SpecialSkillType.FULLY_LOADED)
            }
        }

    }
}