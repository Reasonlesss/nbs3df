package codes.reason.nbs3df.song

data class SongNote(
    val instrument: Byte,
    val key: Byte,
    val velocity: Byte,
    val panning: Int,
    val pitch: Short,
    val layer: Int
)