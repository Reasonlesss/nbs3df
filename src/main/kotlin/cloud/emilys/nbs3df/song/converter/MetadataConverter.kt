package cloud.emilys.nbs3df.song.converter

import cloud.emilys.nbs3df.util.template.Args
import cloud.emilys.nbs3df.util.template.CodeBlock
import cloud.emilys.nbs3df.util.template.SlotItem
import cloud.emilys.nbs3df.util.template.StringItem
import cloud.emilys.nbs3df.util.template.VariableItem
import kotlinx.serialization.json.Json

object MetadataConverter {
    private const val META_VARIABLE_NAME = "nbs:meta"

    private val metaVariable = VariableItem(
        VariableItem.VariableData(
            name = META_VARIABLE_NAME,
            scope = VariableItem.Scope.LOCAL
        )
    )

    fun convertMetadata(meta: SongPlayerMetadata): CodeBlock {
        val jsonItem = StringItem(
            StringItem.StringData(
                name = Json.encodeToString(meta)
            )
        )
        return CodeBlock(
            block = "set_var",
            action = "JsonFromValue",
            args = Args(
                items = listOf(
                    SlotItem(item = metaVariable, slot = 0),
                    SlotItem(item = jsonItem, slot = 1)
                )
            )
        )
    }
}