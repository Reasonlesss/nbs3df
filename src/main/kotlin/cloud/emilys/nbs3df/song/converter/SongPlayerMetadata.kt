package cloud.emilys.nbs3df.song.converter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Metadata used by the song player in-game
 * @param majorVersion The major version number, the player will
 *                     refuse to play songs made for versions
 *                     different to the major version it's designed
 *                     for.
 * @param chunks The number of chunks used throughout the song
 *               instantiate the music player.
 * @param notes The number of notes used in the song.
 * @param bytesPerNote The number of bytes used for each note
 *                     this allows us to make additions to the
 *                     format (such as additional fields) while
 *                     preserving backwards compatibility with
 *                     older versions of the player.
 */
@Serializable
data class SongPlayerMetadata(
    val fileName: String,
    val name: String,
    val author: String,
    @SerialName("original_author")
    val originalAuthor: String,
    @SerialName("major_version")
    val majorVersion: Int,
    val chunks: Int,
    val notes: Int,
    @SerialName("bytes_per_note")
    val bytesPerNote: Int
)