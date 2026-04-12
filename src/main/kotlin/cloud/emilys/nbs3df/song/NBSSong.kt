package cloud.emilys.nbs3df.song

import cloud.emilys.nbs3df.util.ByteReader
import cloud.emilys.nbs3df.util.toByteReader
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

data class NBSSong(
    val fileName: String,
    val header: SongHeader,
    val notes: Map<Int, List<SongNote>>,
    val layers: List<SongLayer>,
    val instruments: List<CustomInstrument>,
    val metrics: SongMetrics,
    val layerNames: List<String>,
    val compressedLayerIndexs: List<Int>
) {
    companion object {
        fun parseFile(path: Path) =
            parse(path.name, path.toFile().toByteReader())

        // The documentation for the NBS format can be found at:
        //     https://noteblock.studio/nbs
        fun parse(fileName: String, reader: ByteReader): NBSSong {
            var version = 0.toByte()

            // In version 0 of the NBS format, the first 2 bytes encode
            // the length of the song, if the value is 0 then it is assumed
            // that the file is a versioned NBS file, in which the actual
            // version is read.
            val legacyLength = reader.readShort()
            if (legacyLength.toInt() == 0) {
                version = reader.readByte()
            }

            // Parse the header of the file.
            val header = SongHeader(
                version = version,
                vanillaInstruments = if (version > 0) reader.readByte() else 9,
                length = if (version >= 3) reader.readShort() else legacyLength,
                layerCount = reader.readShort(),
                meta = Metadata(
                    name = reader.readString(),
                    author = reader.readString(),
                    originalAuthor = reader.readString(),
                    description = reader.readString()
                ),
                tempo = reader.readShort(),
                autoSaveConfig = AutoSaveConfig(
                    enabled = reader.readBoolean(),
                    duration = reader.readByte()
                ),
                timeSignature = reader.readByte(),
                stats = Statistics(
                    minutesSpent = reader.readInt(),
                    leftClicks = reader.readInt(),
                    rightClicks = reader.readInt(),
                    noteBlocksAdded = reader.readInt(),
                    noteBlocksRemoved = reader.readInt()
                ),
                schematicName = reader.readString(),
                looping = if (version >= 4) {
                    Looping(
                        enabled = reader.readBoolean(),
                        maxLoop = reader.readByte(),
                        startTick = reader.readShort()
                    )
                } else {
                    Looping(false, 0, 0)
                }
            )

            // Parse the notes of the file
            var noteCount = 0
            val notes: Map<Int, List<SongNote>> = buildMap {
                var currentTick = -1

                generateSequence {
                    val tickOffset = reader.readShort()
                    if (tickOffset == 0.toShort()) null
                    else tickOffset
                }.forEach {
                    currentTick += it

                    put(currentTick, buildList {
                        var currentLayer = -1

                        generateSequence {
                            val layerOffset = reader.readShort()
                            if (layerOffset == 0.toShort()) null
                            else layerOffset
                        }.forEach { layerOffset ->
                            currentLayer += layerOffset

                            add(
                                SongNote(
                                    instrument = reader.readByte(),
                                    key = reader.readByte(),
                                    velocity = if (version >= 4) reader.readByte() else 100,
                                    panning = if (version >= 4) reader.readUByte() else 100,
                                    pitch = if (version >= 4) reader.readShort() else 0,
                                    layer = currentLayer
                                )
                            )
                            noteCount++
                        }
                    })
                }
            }

            // Parse layers
            val layers = List(header.layerCount.toInt()) {
                SongLayer(
                    name = reader.readString(),
                    locked = if (version >= 4) reader.readBoolean() else false,
                    volume = reader.readByte(),
                    stereo = if (version >= 2) reader.readUByte() else 100
                )
            }

            var layerNames = layers.map { it.name }.distinct()
            var compLayerIdx = layers.map { layerNames.indexOf(it.name) }

            // Parse custom instruments
            val customInstruments = List(reader.readUByte()) {
                CustomInstrument(
                    name = reader.readString(),
                    soundFile = reader.readString(),
                    soundKey = reader.readByte(),
                    pressPianoKey = reader.readBoolean()
                ).also { println(it) }
            }

            // Metrics
            val metrics = SongMetrics(
                noteCount = noteCount,
                layerCount = layers.size,
                customInstruments = customInstruments.size
            )

            return NBSSong(
                fileName,
                header,
                notes,
                layers,
                customInstruments,
                metrics,
                layerNames,
                compLayerIdx
            )
        }
    }
}
