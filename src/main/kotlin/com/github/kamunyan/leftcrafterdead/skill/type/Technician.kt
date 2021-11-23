package com.github.kamunyan.leftcrafterdead.skill.type

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData

class Technician : SkillTree() {
    override val skillType: SkillType = SkillType.TECHNICIAN
    override fun setStatusData(data: StatusData) {
        skillMap.forEach { (index, bool) ->
            if (bool) {
                when (index) {
                    49 -> data.mainGadgetAddPerformance += 0.05
                    38 -> data.sentryGunTime += 15
                    40 -> data.addMainGadgetCoolDown -= 0.15
                    42 -> data.addBulletSpread -= 0.5
                    29 -> data.sentryGunPowerMultiplier += 0.15
                    31 -> data.tripMineRangeMultiplier += 0.3
                    33 -> data.fireRateAddBulletSpread -= 1.0
                    20 -> data.mainGadgetAddPerformance += 0.3
                    22 -> data.explosionDamageMultiplier += 0.3
                    24 -> data.rateAcceleration += 2
                    11 -> data.sentryGunTime += 30
                    13 -> {
                        data.tripMineRangeMultiplier += 0.5
                        data.specialSkillTypes.add(SpecialSkillType.FIRE_TRAP)
                    }
                    15 -> data.tripMineAmount += 6
                    2 -> data.sentryGunAmount += 4
                    4 -> data.mainGadgetAddPerformance += 0.5
                    6 -> data.specialSkillTypes.add(SpecialSkillType.BODY_EXPERTISE)
                    else -> {
                    }
                }
            }
        }
    }
}