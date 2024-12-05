package app.data.storage

import app.data.models.Config
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

object ConfigManager {
    var config: Config = loadConfig()

    fun saveConfig() {
        val json = Json.encodeToString(config)
        val file = File(getConfigLocation().resolve("cube_restart.json").toString())
        file.createNewFile()
        file.writeText(json)
    }

    fun loadConfig(): Config {
        val file = File(getConfigLocation().resolve("cube_restart.json").toString())
        return if (file.exists()) {
            Json.decodeFromString(file.readText())
        } else {
            Config(
                apiUrl = "http://api.cubeshield.ru:8000/api/v1/instances",
                instanceDirectory = "",
                installedContainers = listOf()
            )
        }
    }

    fun getConfigLocation(): Path {
        val currentFolder = Paths.get(System.getProperty("user.dir"))
        if (currentFolder.resolve("cube_restart.json").exists()) {
            return currentFolder
        }
        val userHome = System.getProperty("user.home")
        return when {
            System.getProperty("os.name").lowercase().contains("win") -> {
                Paths.get(userHome, "AppData", "Roaming", "cuberestart").createDirectories()
            }
            System.getProperty("os.name").lowercase().contains("nix") ||
                    System.getProperty("os.name").lowercase().contains("nux") ||
                    System.getProperty("os.name").lowercase().contains("mac") -> {
                Paths.get(userHome, ".config", "cuberestart").createDirectories()
            }
            else -> {
                Paths.get(userHome, "cuberestart").createDirectories()
            }
        }
    }
}