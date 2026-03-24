package codes.reason.nbs3df.preview

import codes.reason.nbs3df.song.NBSSong
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import java.nio.file.Path

object NBSPreview {
    var currentSongPath: Path? = null
    var currentSongFile: NBSSong? = null
    var currentSongTick = 0
    var playing: Boolean = false

    fun setup() {
        ClientTickEvents.END_CLIENT_TICK.register { mc ->
            if (!playing) {
                return@register
            }
            if (currentSongTick >= currentSongFile!!.header.length) {
                playing = false
                return@register
            }

        }
    }
}