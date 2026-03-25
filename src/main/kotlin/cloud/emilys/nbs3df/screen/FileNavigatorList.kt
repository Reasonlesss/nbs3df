package cloud.emilys.nbs3df.screen

import cloud.emilys.nbs3df.screen.entry.FileNavigatorEntry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.ContainerObjectSelectionList

class FileNavigatorList(screen: FileNavigatorScreen) : ContainerObjectSelectionList<FileNavigatorEntry>(
    Minecraft.getInstance(),
    screen.width,
    screen.layout.contentHeight,
    screen.layout.headerHeight,
    FileNavigatorEntry.ENTRY_HEIGHT
) {

    fun add(entry: FileNavigatorEntry) {
        this.addEntry(entry)
    }

    fun clear() {
        this.clearEntries()
        this.setScrollAmount(0.0)
    }

}