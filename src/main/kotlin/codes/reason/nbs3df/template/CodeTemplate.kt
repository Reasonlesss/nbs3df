package codes.reason.nbs3df.template

import codes.reason.nbs3df.util.compress
import codes.reason.nbs3df.util.toBase64
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class CodeTemplateData(val blocks: List<BlockOrBracket>) {
    fun encode(): String {
        val json = Json {
            classDiscriminator = "id"
            prettyPrint = false
        }
        return json.encodeToJsonElement(this).toString()
            .encodeToByteArray().compress().toBase64()
    }
}