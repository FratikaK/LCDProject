package com.github.kamunyan.leftcrafterdead.configs

import com.github.kamunyan.leftcrafterdead.skill.SkillTree
import com.github.kamunyan.leftcrafterdead.skill.SkillType

object SkillTreeConfig : Config("data", "skill.yml") {
    val skillDataHashMap = HashMap<SkillType, HashMap<Int, HashMap<String, String>>>()
    private val skillTypeList =
        listOf(SkillType.MASTERMIND, SkillType.ENFORCER, SkillType.TECHNICIAN, SkillType.GHOST, SkillType.FUGITIVE)

    override fun loadConfig() {
        skillTypeList.forEach { skill ->
            val indexHashMap = HashMap<Int, HashMap<String, String>>()
            SkillTree.requireSkillPoint.forEach { (index, _) ->
                val infoHashMap = HashMap<String, String>()
                val typeSection = yml.getConfigurationSection(skill.name) ?: return@forEach
                val name: String? = typeSection.getString("${index}.name")
                val info: String? = typeSection.getString("${index}.info")
                val type: String? = typeSection.getString("${index}.type")
                infoHashMap["name"] = name ?: "non name"
                infoHashMap["info"] = info ?: "non info"
                infoHashMap["type"] = type ?: "non type"
                indexHashMap[index] = infoHashMap
            }
            skillDataHashMap[skill] = indexHashMap
        }
    }
}