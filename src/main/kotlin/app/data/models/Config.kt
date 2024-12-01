package app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    @SerialName("api_url") var apiUrl: String,
    @SerialName("instance_directory") var instanceDirectory: String,
    @SerialName("installed_containers") var installedContainers: List<Container>,
)
