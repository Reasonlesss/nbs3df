package codes.reason.nbs3df.song

data class SongHeader(
    val version: Byte,
    val vanillaInstruments: Byte,
    val length: Short,
    val layerCount: Short,
    val meta: Metadata,
    val tempo: Short,
    val autoSaveConfig: AutoSaveConfig,
    val timeSignature: Byte,
    val stats: Statistics,
    val schematicName: String,
    val looping: Looping
)

data class AutoSaveConfig(
    val enabled: Boolean,
    val duration: Byte,
)

data class Metadata(
    val name: String,
    val author: String,
    val originalAuthor: String,
    val description: String
)

data class Statistics(
    val minutesSpent: Int,
    val leftClicks: Int,
    val rightClicks: Int,
    val noteBlocksAdded: Int,
    val noteBlocksRemoved: Int
)

data class Looping(
    val enabled: Boolean,
    val maxLoop: Byte,
    val startTick: Short
)
