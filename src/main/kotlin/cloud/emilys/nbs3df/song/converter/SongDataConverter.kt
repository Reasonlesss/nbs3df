package cloud.emilys.nbs3df.song.converter

import cloud.emilys.nbs3df.util.template.CodeBlock
import cloud.emilys.nbs3df.util.template.StringItem
import cloud.emilys.nbs3df.util.template.VariableItem
import cloud.emilys.nbs3df.util.template.createListCodeBlocks
import cloud.emilys.nbs3df.util.compress
import cloud.emilys.nbs3df.util.toBase64

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