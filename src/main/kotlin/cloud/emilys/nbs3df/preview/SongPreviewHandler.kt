package cloud.emilys.nbs3df.preview

import cloud.emilys.nbs3df.song.NBSSong
import cloud.emilys.nbs3df.song.plugin.PluginChain
import cloud.emilys.nbs3df.song.plugin.ResamplePlugin
import cloud.emilys.nbs3df.song.plugin.TransposeNotesPlugin
import cloud.emilys.nbs3df.song.plugin.apply
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

object SongPreviewHandler {

    private val pluginsToApply: PluginChain = listOf(
        ResamplePlugin(targetTempo = 20.0),
        TransposeNotesPlugin
    )

    var currentSongPath: Path? = null
        private set

    var isLoading = false
        private set

    private var cancelLoading = false

    private var currentPreview: SongPreview? = null

    fun setup() {
        ClientTickEvents.END_CLIENT_TICK.register { mc ->
            if (this.isLoading) {
                return@register
            }
            val preview = this.currentPreview ?: return@register
            if (!preview.playing) {
                return@register
            }
            preview.tick()
        }
    }

    fun stopSong() {
        cancelLoading = true
        currentSongPath = null
        currentPreview = null
    }

    fun isPlaying() : Boolean {
        return !this.isLoading && this.currentPreview != null && this.currentPreview!!.playing
    }

    fun playSong(path: Path) {
        if (isLoading) {
            return
        }
        currentSongPath = path
        currentPreview = null
        cancelLoading = false
        isLoading = true

        CompletableFuture
            .supplyAsync {
                pluginsToApply.apply(NBSSong.parseFile(path))
            }
            .thenAccept { song ->
                val sounds = PreviewSound.buildPreviewSounds(song)
                Minecraft.getInstance().execute {
                    if (!cancelLoading) {
                        currentPreview = SongPreview(song, sounds)
                    }
                    isLoading = false
                }
            }
            .exceptionally { throwable ->
                throwable.printStackTrace()
                Minecraft.getInstance().execute {
                    isLoading = false
                }
                null
            }
    }
}