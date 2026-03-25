package cloud.emilys.nbs3df.screen.entry

import cloud.emilys.nbs3df.NBS3DF
import cloud.emilys.nbs3df.preview.SongPreviewHandler
import cloud.emilys.nbs3df.screen.FileNavigatorScreen
import cloud.emilys.nbs3df.screen.ImportConfirmScreen
import cloud.emilys.nbs3df.song.NBSSong
import cloud.emilys.nbs3df.song.converter.SongConverter
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import javax.tools.Tool
import kotlin.io.path.nameWithoutExtension

class SongNavigatorEntry(private val screen: FileNavigatorScreen, private val path: Path) : FileNavigatorEntry() {

    companion object {
        val PLAY_BUTTON_TEXT = Component.literal("▶").withColor(ChatFormatting.GREEN.color!!)
        val LOADING_BUTTON_TEXT = Component.literal("⌚").withColor(ChatFormatting.YELLOW.color!!)
        val STOP_BUTTON_TEXT = Component.literal("⏹").withColor(ChatFormatting.RED.color!!)
        val IMPORT_BUTTON_TEXT = Component.literal("↓").withColor(ChatFormatting.DARK_AQUA.color!!)

        val PLAY_BUTTON_TOOLTIP = Component.translatable("nbs3df.screen.import.startPreview")
        val STOP_BUTTON_TOOLTIP = Component.translatable("nbs3df.screen.import.stopPreview")
        val LOADING_BUTTON_TOOLTIP = Component.translatable("nbs3df.screen.import.loadingPreview")
        val IMPORT_BUTTON_TOOLTIP = Component.translatable("nbs3df.screen.import.selectSong")
    }

    val previewButton = Button
        .builder(PLAY_BUTTON_TEXT) {
            if (SongPreviewHandler.currentSongPath == path) {
                SongPreviewHandler.stopSong()
                return@builder
            }
            SongPreviewHandler.playSong(path)
        }
        .tooltip(Tooltip.create(PLAY_BUTTON_TOOLTIP))
        .bounds(0, 0, 16, 16)
        .build()

    val importButton = Button
        .builder(IMPORT_BUTTON_TEXT) {
            this.importItem()
        }
        .tooltip(Tooltip.create(IMPORT_BUTTON_TOOLTIP))
        .bounds(0, 0, 16, 16)
        .build()

    override val icon: Identifier = NBS3DF.id("song")
    override val name: Component = Component.literal(path.nameWithoutExtension)
    override val buttons: List<AbstractButton>
        get() = listOf(previewButton, importButton)

    override fun updateButtons() {
        val isPlaying = SongPreviewHandler.currentSongPath == this.path && SongPreviewHandler.isPlaying();
        this.previewButton.message = when {
            SongPreviewHandler.isLoading && isPlaying -> LOADING_BUTTON_TEXT
            isPlaying -> STOP_BUTTON_TEXT
            else -> PLAY_BUTTON_TEXT
        }
        this.previewButton.setTooltip(Tooltip.create(when {
            SongPreviewHandler.isLoading && isPlaying -> LOADING_BUTTON_TOOLTIP
            isPlaying -> STOP_BUTTON_TOOLTIP
            else -> PLAY_BUTTON_TOOLTIP
        }))
    }

    override fun clickBackground(
        event: MouseButtonEvent,
        isDouble: Boolean
    ): Boolean {
        if (isDouble) {
            importItem()
            return true
        }
        return false
    }

    fun importItem() {
        screen.onClose()

        CompletableFuture
            .supplyAsync {
                NBSSong.parseFile(this.path)
            }
            .thenAccept { file ->
                val items = SongConverter.convertSong(file)
                Minecraft.getInstance().execute {
                    ImportConfirmScreen.open(file, items)
                }
            }
            .exceptionally {
                it.printStackTrace()
                null
            }
    }

}
