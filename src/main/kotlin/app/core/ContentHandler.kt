package app.core

import app.data.api.ApiClient
import app.data.models.Container
import app.data.storage.ConfigManager
import java.nio.file.Path

class ContentHandler(val apiClient: ApiClient, val path: Path) {
    suspend fun handleContainers(containers: List<Container>) {
        val updatedContainers = mutableListOf<Container>()
        containers.forEach {container ->
            updatedContainers.add(handleContainer(container))
        }
        ConfigManager.config.installedContainers = updatedContainers
        ConfigManager.saveConfig()
    }

    suspend fun handleContainer(container: Container): Container {
        val installedContainer = ConfigManager.config.installedContainers.find { it.contentType == container.contentType } ?: Container(container.contentType, listOf())
        val packageContent = PackageContent(container.contentType, container.content, installedContainer.content, apiClient, path)
        return packageContent.handleContent()
    }
}