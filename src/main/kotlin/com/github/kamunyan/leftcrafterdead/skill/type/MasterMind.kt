package com.github.kamunyan.leftcrafterdead.skill.type

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.SpecialSkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData

class MasterMind: SkillTree() {
    override val skillType: SkillType = SkillType.MASTERMIND
    override fun setStatusData(data: StatusData) {
        skillMap.forEach{ (index, flag) ->
            if (!flag) return@forEach
            when(index){
                49 -> data.reloadSpeedAcceleration -= 0.05
                38 -> data.rateAcceleration += 1
                40 -> data.fireRateReloadSpeedAcceleration -= 0.1
                42 -> data.addBulletSpread -= 0.5
                29 -> data.healPotionMultiplier += 0.5
                31 -> data.fireRateReloadSpeedAcceleration -= 0.15
                33 -> data.fireRateAddBulletSpread -= 1.0
                20 -> data.healPotionAmount += 2
                22 -> data.specialSkillTypes.add(SpecialSkillType.COMBAT_MEDIC)
                24 -> data.painKillerAmount = 8
                11 -> data.healPotionMultiplier += 1.0
                13 -> data.dodgeMultiplier += 25
                15 -> data.fireRateAddBulletSpread -= 2.5
                2 -> data.specialSkillTypes.add(SpecialSkillType.INSPIRE)
                4 -> data.specialSkillTypes.add(SpecialSkillType.GRAZE)
                6 -> data.rateAcceleration += 3
            }
        }
    }
}