package com.github.kamunyan.leftcrafterdead.skill.type

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData

class Fugitive : SkillTree() {
    override val skillType: SkillType = SkillType.FUGITIVE
    override fun setStatusData(data: StatusData) {
        skillMap.forEach { (index, flag) ->
            if (!flag) return@forEach
            when (index) {
                49 -> data.secondaryDamageMultiplier += 0.1
                38 -> data.secondaryAddBulletSpread -= 2.0
                40 -> data.specialSkillTypes.add(SpecialSkillType.COMMITMENT_TO_SURVIVAL)
                42 -> data.specialWeaponDamageMultiplier += 0.3
                29 -> data.secondaryMagazineMultiplier += 0.3
                31 -> data.specialSkillTypes.add(SpecialSkillType.RUNNING_FROM_DEATH)
                33 -> data.specialSkillTypes.add(SpecialSkillType.BERSERKER)
                20 -> data.secondaryDamageMultiplier += 0.2
                22 -> data.specialSkillTypes.add(SpecialSkillType.UP_YOU_GO)
                24 -> data.healthScaleAmount -= 4.0
                11 -> {
                    data.secondaryAddBulletSpread -= 2.0
                    data.secondaryDamageMultiplier += 0.2
                }
                13 -> data.specialSkillTypes.add(SpecialSkillType.SWAN_SONG)
                15 -> data.healthScaleAmount -= 6.0
                2 -> data.secondaryMagazineMultiplier += 0.5
                4 -> {
                    data.healthScaleAmount -= 8.0
                    data.damageResistMultiplier -= 0.3
                }
                6 -> data.specialSkillTypes.add(SpecialSkillType.MESSIAH)
            }
        }
    }
}