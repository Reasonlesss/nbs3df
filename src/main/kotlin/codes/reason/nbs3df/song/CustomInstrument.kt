package codes.reason.nbs3df.song

data class CustomInstrument(
    val name: String,
    val soundFile: String,
    val soundKey: Byte,
    val pressPianoKey: Boolean
)