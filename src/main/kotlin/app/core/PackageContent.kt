package app.core

import app.data.api.ApiClient
import app.data.models.Container
import app.data.models.Content
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import com.github.ajalt.mordant.rendering.TextColors.*
import kotlin.io.path.createDirectories
import kotlin.reflect.KSuspendFunction1


class PackageContent(contentType: String, apiContent: List<Content>, packageContent: List<Content>, apiClient: ApiClient, basePath: Path) : BaseContent(contentType, apiContent, packageContent, apiClient, basePath) {
    private val contentPath = basePath.resolve(contentType)
    private val t = Terminal()

    override fun handleDirectories() {
        contentPath.createDirectories()
    }

    fun getContentSetFromPath(path: Path): Set<String> {
        return File(path.toString()).listFiles()?.mapNotNull { it.name.replace("[CubeRestart] ", "") }?.toSet() ?: emptySet()
    }

    fun getCurrentMainContentSet(): Set<String> {
        return getContentSetFromPath(contentPath)
    }

    fun fromContentToSet(contentList: List<Content>): Set<String> {
        return contentList.map { it.file }.toSet()
    }

    fun getPackageContentSet(): Set<String> {
        return fromContentToSet(packageContent)
    }

    fun getApiContentSet(): Set<String> {
        return fromContentToSet(apiContent)
    }

    suspend fun actPackageContentSet(contentList: List<Content>, contentSet: Set<String>, action: KSuspendFunction1<Content, Unit>) {
        for (content in contentList) {
            if (content.file in contentSet) {
                action(content)
            }
        }
    }

    suspend fun deletePackageContent(content: Content) {
        t.println(brightWhite("· Удаление ${content.file}"))
        withContext(Dispatchers.IO) {
            Files.deleteIfExists(contentPath.resolve("[CubeRestart] ${content.file}"))
        }
    }

    suspend fun installPackageContent(content: Content) {
        t.println(brightWhite("· Скачивание ${content.file}"))
        apiClient.downloadFile(content.url, contentPath, "[CubeRestart] ${content.file}")
    }

    override suspend fun handleContent(): Container {
        handleDirectories()
        val packageContentSet = getPackageContentSet()
        val apiContentSet = getApiContentSet()
        val deletePackageContentSet = packageContentSet - apiContentSet
        val installPackageContentSet = apiContentSet - packageContentSet

        actPackageContentSet(packageContent, deletePackageContentSet, ::deletePackageContent)
        actPackageContentSet(apiContent, installPackageContentSet, ::installPackageContent)

        return Container(contentType=contentType, content=apiContent)
    }

    override fun applyContent() {
    }
}