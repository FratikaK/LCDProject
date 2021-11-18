package com.github.kamunyan.leftcrafterdead.skill.type

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType
import com.github.kamunyan.leftcrafterdead.skill.StatusData

class Ghost:SkillTree() {
    override val skillType: SkillType = SkillType.GHOST
    override fun setStatusData(data: StatusData) {
    }
}