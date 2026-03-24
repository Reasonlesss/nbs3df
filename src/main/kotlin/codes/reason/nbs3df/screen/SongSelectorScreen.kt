package codes.reason.nbs3df.screen

import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

class SongSelectorScreen(path: Path) : Screen(
    Component.literal("lol")
) {

    val layout = HeaderAndFooterLayout(this)
    val list = SongSelectorList(this)

    init {
        Files.list(path)
            .sorted(
                compareBy<Path> { Files.isRegularFile(it) }
                    .thenBy { it.fileName.toString().lowercase() }
            )
            .forEach {
                if (Files.isDirectory(it)) {
                    this.list.addDirectory(it)
                }
                if (it.extension == "nbs") {
                    this.list.addSong(it)
                }
            }
    }

    override fun init() {
        this.layout.addToContents(list)
        this.layout.visitWidgets {
            this.addRenderableWidget(it)
        }
        this.layout.arrangeElements()
        this.list.updateSize(this.width, this.layout)
    }

}