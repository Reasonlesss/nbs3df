package codes.reason.nbs3df.screen

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

class SongSelectorList(screen: SongSelectorScreen) : ContainerObjectSelectionList<SongSelectorEntry>(
    Minecraft.getInstance(),
    screen.width,
    screen.layout.contentHeight,
    screen.layout.headerHeight,
    20
) {

    fun addDirectory(path: Path) {
        this.addEntry(DirectorySelectorEntry(path.name))
    }

    fun addSong(path: Path) {
        this.addEntry(NBSSelectorEntry(path.nameWithoutExtension))
    }

}