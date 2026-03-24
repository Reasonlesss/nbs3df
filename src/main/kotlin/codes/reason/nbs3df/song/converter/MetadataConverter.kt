package codes.reason.nbs3df.song.converter

import codes.reason.nbs3df.template.Args
import codes.reason.nbs3df.template.CodeBlock
import codes.reason.nbs3df.template.SlotItem
import codes.reason.nbs3df.template.StringItem
import codes.reason.nbs3df.template.VariableItem
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