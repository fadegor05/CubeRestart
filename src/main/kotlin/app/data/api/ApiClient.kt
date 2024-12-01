package app.data.api

import app.data.models.ApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path


class ApiClient {
    private val client = HttpClient()

    suspend fun fetchApiResponse(endpoint: String): ApiResponse {
        val response = client.get(endpoint).bodyAsText()
        return Json.decodeFromString(response)
    }

    suspend fun downloadFile(endpoint: String, path: Path, fileName: String) {
        val response = client.get(endpoint)

        if (response.status.value in 200..299) {
            withContext(Dispatchers.IO) {
                Files.newOutputStream(path.resolve(fileName)).buffered().use { outputStream ->
                    response.bodyAsBytes().inputStream().copyTo(outputStream)
                }
            }
        }
    }
}