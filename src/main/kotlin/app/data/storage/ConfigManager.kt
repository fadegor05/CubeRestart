package app.data.storage

import app.data.models.Config
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object ConfigManager {
    var config: Config = loadConfig()

    fun saveConfig() {
        val json = Json.encodeToString(config)
        File("cube_restart.json").writeText(json)
    }

    fun loadConfig(): Config {
        val file = File("cube_restart.json")
        return if (file.exists()) {
            Json.decodeFromString(file.readText())
        } else Config(apiUrl = "http://api.cubeshield.ru:8000/api/v1/instances", instanceDirectory = "", installedContainers = listOf())
    }
}