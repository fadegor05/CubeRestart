package app.core

import app.data.api.ApiClient
import app.data.models.Container
import app.data.models.Content

abstract class BaseContent(val contentType: String, val apiContent: List<Content>, val packageContent: List<Content>, val apiClient: ApiClient, val basePath: java.nio.file.Path) {
    abstract fun handleDirectories()
    abstract fun applyContent()
    abstract suspend fun handleContent(): Container
}