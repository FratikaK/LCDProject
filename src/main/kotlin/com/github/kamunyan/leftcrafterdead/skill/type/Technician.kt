package com.github.kamunyan.leftcrafterdead.skill.type

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData

class Technician:SkillTree(){
    override val skillType: SkillType = SkillType.TECHNICIAN
    override fun setStatusData(data: StatusData) {
        skillMap.forEach { index, bool ->
            if (bool){
                when(index){

                    else -> {}
                }
            }
        }
    }
}