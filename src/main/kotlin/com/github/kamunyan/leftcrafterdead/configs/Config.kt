package com.github.kamunyan.leftcrafterdead.configs

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.jetbrains.annotations.NotNull
import java.io.File

abstract class Config(@NotNull val fileDir: String, @NotNull val targetFile: String) {
    protected val plugin = LeftCrafterDead.instance
    protected val manager = MatchManager

    init {
        createFile()
    }

    /**
     * コンフィグファイルをロードする処理
     */
    abstract fun loadConfig()

    lateinit var yml: YamlConfiguration

    /**
     * 対象のファイルが存在していなければ作成
     */
    private fun createFile() {
        val dir = File("plugins/LeftCrafterDead", "$fileDir/$targetFile")
        dir.parentFile.mkdirs()
        if (!dir.exists()) {
            plugin.saveResource("$fileDir/$targetFile", false)
            plugin.logger.info("${ChatColor.YELLOW}[$targetFile] The file was not found. A new file was created.")
        }
        yml = YamlConfiguration.loadConfiguration(dir)
    }

    open fun loadCampaignConfig() {}

    fun getInt(nodes: String): Int? {
        if (yml.contains(nodes)) return yml.getInt(nodes)
        return null
    }

    fun getDouble(nodes: String): Double? {
        if (yml.contains(nodes)) return yml.getDouble(nodes)
        return null
    }

    fun getString(nodes: String): String? {
        return yml.getString(nodes)
    }

    fun getLocation(section: ConfigurationSection): Location {
        return Location(
            section.getString("world")?.let { plugin.server.getWorld(it) },
            section.getDouble("x"),
            section.getDouble("y"),
            section.getDouble("z"),
            section.getDouble("yaw").toFloat(),
            section.getDouble("pitch").toFloat()
        )
    }
}