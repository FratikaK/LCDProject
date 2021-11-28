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
                40 -> data.staminaDecreaseMultiplier += 0.25
                42 -> data.criticalMultiplier += 8
                29 -> data.walkSpeed *= 1.25f
                31 -> data.maxGrenade += 2
                33 -> {
                    data.specialSkillTypes.add(SpecialSkillType.SILENT_KILLER)
                    data.weaponDamageMultiplier -= 0.2
                    data.criticalMultiplier += 15
                }
                20 -> data.criticalDamageMultiplier += 0.15
                22 -> data.staminaDecreaseMultiplier += 0.3
                24 -> data.suppressorCriticalDamageMultiplier += 0.3
                11 -> data.dodgeMultiplier += 10
                13 -> data.walkSpeed *= 1.3f
                15 -> data.specialSkillTypes.add(SpecialSkillType.LOW_BLOW)
                2 -> data.maxGrenade += 5
                4 -> data.specialSkillTypes.add(SpecialSkillType.SNEAKY_BASTARD)
                6 -> data.suppressorCriticalDamageMultiplier += 0.5
            }
        }
    }
}