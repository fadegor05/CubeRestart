package app.data.api

import app.data.models.ApiResponse
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.progress.progressBar
import com.github.ajalt.mordant.widgets.progress.progressBarLayout
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.Math.pow
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.pow


class ApiClient {
    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 0
        }
    }

    suspend fun fetchApiResponse(endpoint: String): ApiResponse {
        val response = client.get(endpoint).bodyAsText()
        return Json.decodeFromString(response)
    }

    suspend fun downloadFile(endpoint: String, path: Path, fileName: String) {
        val filePath = path.resolve(fileName)
        if (!Files.exists(filePath)) {
            val response = client.get(endpoint)

            if (response.status.value in 200..299) {
                withContext(Dispatchers.IO) {
                    Files.newOutputStream(filePath).buffered().use { outputStream ->
                        response.bodyAsBytes().inputStream().copyTo(outputStream)
                    }
                }
            }
        }
    }
}