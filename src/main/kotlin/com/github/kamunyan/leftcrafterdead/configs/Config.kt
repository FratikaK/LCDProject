package com.github.kamunyan.leftcrafterdead.configs

import com.github.kamunyan.leftcrafterdead.LeftCrafterDead
import com.github.kamunyan.leftcrafterdead.MatchManager
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

    protected val config: YamlConfiguration = YamlConfiguration()

    private fun createFile() {
        if (existsResource()) {
            return
        }

        val file = File(fileDir)
        val resourceFile = File("$fileDir/$targetFile")
        file.parentFile.mkdir()
        file.mkdir()

        val files = file.listFiles()
        if (!files.contains(resourceFile)) {
            resourceFile.createNewFile()
            plugin.saveResource("$fileDir/$targetFile", false)
            plugin.logger.info("[$targetFile]File not found. A new file has been created.")
        }
    }

    /**
     * ファイルが存在するか
     */
    private fun existsResource(): Boolean {
        return File("$fileDir/$targetFile").exists()
    }

}