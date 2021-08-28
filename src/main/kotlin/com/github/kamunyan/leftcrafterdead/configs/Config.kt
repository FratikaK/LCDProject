package com.github.kamunyan.leftcrafterdead.configs

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
import org.bukkit.ChatColor
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

    protected lateinit var yml: YamlConfiguration

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
}