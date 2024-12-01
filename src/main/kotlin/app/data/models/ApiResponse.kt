package app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val uuid: String,
    val name: String,
    val version: String,
    val changelog: String,
    @SerialName("instance_type") val instanceType: String,
    val containers: List<Container>,
)

@Serializable
data class Container(
    @SerialName("content_type") val contentType: String,
    val content: List<Content>,
)

@Serializable
data class Content(
    val file: String,
    val url: String,
)