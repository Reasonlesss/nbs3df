package cloud.emilys.nbs3df.song

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

data class CustomInstrument(
    val name: String,
    val soundFile: String,
    val soundKey: Byte,
    val pressPianoKey: Boolean
)

@OptIn(ExperimentalSerializationApi::class)
object CustomInstrumentData {

    val names: Map<String, String>
    val files: Map<String, SoundFileInfo>

    init {
        val json = Json { ignoreUnknownKeys = true }
        this.names = json.decodeResource("/sound_names.json")
        this.files = json.decodeResource("/sound_files.json")
    }

    private inline fun <reified T> Json.decodeResource(name: String) : T {
        return this.decodeFromStream<T>(CustomInstrumentData.javaClass.getResourceAsStream(name)!!)
    }

    fun isSoundName(name: String) = name in names

    fun findSound(path: String): SoundFileInfo? {
        val pathWithoutExt = path.removeSuffix(".ogg")
        val components = pathWithoutExt.split('/', '\\').filter { it.isNotEmpty() }
        if (components.isEmpty()) return null
        for (i in components.indices) {
            val suffix = components.subList(i, components.size).joinToString("/")
            if (suffix in files) {
                return files[suffix]
            }
        }
        return null
    }

}

@Serializable
data class SoundFileInfo(
    val key: String,
    val seed: Long?,
    val name: String?,
    @SerialName("seed_name")
    val variantName: String?
)
