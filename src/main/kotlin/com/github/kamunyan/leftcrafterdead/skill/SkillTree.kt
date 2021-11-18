package com.github.kamunyan.leftcrafterdead.skill

abstract class SkillTree {
    val skillMap: HashMap<Int, Boolean> = hashMapOf(
        49 to false,
        38 to false,
        40 to false,
        42 to false,
        29 to false,
        31 to false,
        33 to false,
        20 to false,
        22 to false,
        24 to false,
        11 to false,
        13 to false,
        15 to false,
        2 to false,
        4 to false,
        6 to false
    )
    abstract val skillType: SkillType
    abstract fun setStatusData(data: StatusData)
    var useSkillPoint = 0
}
