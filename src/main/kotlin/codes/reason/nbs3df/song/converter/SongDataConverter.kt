package codes.reason.nbs3df.song.converter

import codes.reason.nbs3df.util.template.CodeBlock
import codes.reason.nbs3df.util.template.StringItem
import codes.reason.nbs3df.util.template.VariableItem
import codes.reason.nbs3df.util.template.createListCodeBlocks
import codes.reason.nbs3df.util.compress
import codes.reason.nbs3df.util.toBase64

object SongDataConverter {
    private const val DATA_VARIABLE_NAME = "nbs:data"

    private val dataVariable = VariableItem(
        VariableItem.VariableData(
            name = DATA_VARIABLE_NAME,
            scope = VariableItem.Scope.LOCAL
        )
    )

    fun convertNotes(chunks: List<ByteArray>): List<CodeBlock> {
        val mappedChunks = chunks.map {
            StringItem(
                StringItem.StringData(
                    name = it.compress().toBase64()
                )
            )
        }
        return createListCodeBlocks(
            variableItem = dataVariable,
            chunkSize = 15,
            codeItems = mappedChunks
        )
    }
}