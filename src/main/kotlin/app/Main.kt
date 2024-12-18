package app

import app.core.ContentHandler
import app.data.api.ApiClient
import app.data.models.ApiResponse
import app.data.models.Config
import app.data.storage.ConfigManager
import app.presentation.creditsOut
import app.presentation.requestFolderFromUser
import app.presentation.titleOut
import com.github.ajalt.mordant.rendering.TextAlign
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.table.GridBuilder
import com.github.ajalt.mordant.table.grid
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import java.nio.file.Paths
import kotlin.system.exitProcess


suspend fun main(args: Array<String>) {
    val parser = ArgParser("CubeRestart")
    val dir by parser.option(ArgType.String, shortName = "dir", description = "path to directory").default("")
    val dirRequest by parser.option(ArgType.Boolean, shortName = "dir-request",description = "request directory")

    parser.parse(args)

    val t = Terminal()
    t.println(titleOut())
    t.println(creditsOut())
    if (ConfigManager.config.instanceDirectory == "" || dirRequest == true) {
        while (true) {
            t.println(green("Выбрите папку со сборкой (там где находятся mods, resourcepack, config и тд.)"))
            val path = requestFolderFromUser()
            if (path != null) {
                ConfigManager.config.instanceDirectory = path.toString()
                ConfigManager.saveConfig()
                break
            }
            else {
                t.println(yellow("Такого пути не существует"))
                exitProcess(0)
            }
        }
    }

    if (dir != "") {
        ConfigManager.config.instanceDirectory = dir
        ConfigManager.saveConfig()
    }

    val apiClient = ApiClient()
    val apiResponse: ApiResponse
    try {
        t.println(green("Подключение к Cube-API..."))
        apiResponse = apiClient.fetchApiResponse(ConfigManager.config.apiUrl)
    }
    catch (_: Exception) {
        t.println(red("Не удалось подключиться к Cube-API..."))
        exitProcess(0)
    }

    val contentHandler = ContentHandler(apiClient, Paths.get(ConfigManager.config.instanceDirectory))
    contentHandler.handleContainers(apiResponse.containers)
    t.println(green("Весь контент был успешно собран..."))
    ConfigManager.saveConfig()
}

