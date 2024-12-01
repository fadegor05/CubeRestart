package app.presentation

import app.core.ContentHandler
import app.core.PackageContent
import app.data.api.ApiClient
import app.data.storage.ConfigManager
import java.nio.file.Path
import java.nio.file.Paths


suspend fun main() {
    val apiClient = ApiClient()
    val apiResponse = apiClient.fetchApiResponse(ConfigManager.config.apiUrl)
    val path = Paths.get("/Users/fadegor05/Library/Application Support/PrismLauncher/instances/test/.minecraft")
    val contentHandler = ContentHandler(apiClient, path)
    contentHandler.handleContainers(apiResponse.containers)
    ConfigManager.saveConfig()
}