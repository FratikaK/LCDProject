package com.github.kamunyan.leftcrafterdead.skill.type

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData

class Ghost : SkillTree() {
    override val skillType: SkillType = SkillType.GHOST
    override fun setStatusData(data: StatusData) {
        skillMap.forEach { (index, flag) ->
            if (!flag) return@forEach
            when (index) {
                49 -> data.criticalMultiplier += 5
                38 -> data.dodgeMultiplier += 5
                40 -> data.staminaDecreaseMultiplier -= 0.25
                42 -> data.criticalMultiplier += 8
                29 -> data.walkSpeed *= 1.25f
                31 -> data.maxGrenade += 2
                33 -> data.magazineAmountMultiplier += 0.15
                20 -> data.shotgunDamageMultiplier += 0.15
                22 -> data.specialSkillTypes.add(SpecialSkillType.BULLSEYE)
                24 -> data.ammunitionAmountMultiplier += 0.5
                11 -> data.specialSkillTypes.add(SpecialSkillType.CLOSE_BY)
                13 -> data.armorLimit += 7
                15 -> data.magazineAmountMultiplier += 0.3
                2 -> data.armorLimit += 15
                4 -> data.weaponDamageMultiplier += 0.3
                6 -> data.specialSkillTypes.add(SpecialSkillType.FULLY_LOADED)
            }
        }
    }
}