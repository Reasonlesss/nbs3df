package cloud.emilys.nbs3df.song.converter

import cloud.emilys.nbs3df.util.template.setCodeTemplate
import cloud.emilys.nbs3df.song.NBSSong
import cloud.emilys.nbs3df.song.plugin.PluginChain
import cloud.emilys.nbs3df.song.plugin.ResamplePlugin
import cloud.emilys.nbs3df.song.plugin.TransposeNotesPlugin
import cloud.emilys.nbs3df.song.plugin.apply
import cloud.emilys.nbs3df.util.template.CodeTemplateData
import cloud.emilys.nbs3df.util.chunked
import net.minecraft.world.item.ItemStack

object SongConverter {
    private const val MAJOR_VERSION = 1
    private const val BYTES_PER_CHUNK = 10_000
    private const val TARGET_TPS = 20.0
    private const val JUMPS_PER_NOTE = 8

    private val pluginsToApply: PluginChain = listOf(
        ResamplePlugin(targetTempo = TARGET_TPS),
        TransposeNotesPlugin
    )

    fun convertSong(song: NBSSong): List<ItemStack> {
        val modifiedSong = pluginsToApply.apply(song)
        val chunks = SongByteEncoder.encodeSongBytes(modifiedSong)
            .chunked(BYTES_PER_CHUNK)

        val function = LineStarterGenerator.createLineStarter(modifiedSong)
        val data = SongDataConverter.convertNotes(chunks)
        val instruments = InstrumentConverter.convertInstruments(modifiedSong)

        val metadata = SongPlayerMetadata(
            name = song.header.meta.name.ifEmpty { song.fileName },
            fileName = song.fileName,
            author = song.header.meta.author,
            originalAuthor = song.header.meta.originalAuthor,
            chunks = chunks.size,
            notes = modifiedSong.notes.values.sumOf { it.size },
            bytesPerNote = JUMPS_PER_NOTE,
            majorVersion = MAJOR_VERSION,
            layers = song.layerNames
        )

        val convertedMetadata =
            MetadataConverter.convertMetadata(metadata)

        return data.mapIndexed { index, currentBlock ->
            val blocks = buildList {
                if (index == 0) add(function)
                add(currentBlock)
                if (index == data.lastIndex) {
                    addAll(instruments)
                    add(convertedMetadata)
                }
            }

            val item = SongIconGenerator.makeSongIcon(song, index + 1, data.size)
            item.setCodeTemplate(song.header.meta.author, CodeTemplateData(blocks).encode())
            item
        }
    }
}