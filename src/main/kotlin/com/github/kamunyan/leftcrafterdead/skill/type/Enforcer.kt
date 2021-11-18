package com.github.kamunyan.leftcrafterdead.skill.type

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData

class Enforcer() : SkillTree() {
    override val skillType: SkillType = SkillType.ENFORCER
    override fun setStatusData(data: StatusData) {
    }
}