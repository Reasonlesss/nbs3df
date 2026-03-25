package cloud.emilys.nbs3df.screen.entry

import cloud.emilys.nbs3df.NBS3DF
import cloud.emilys.nbs3df.screen.FileNavigatorScreen
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import java.nio.file.Path
import kotlin.io.path.name

class DirectoryNavigatorEntry(val screen: FileNavigatorScreen, val path: Path) : FileNavigatorEntry() {

    override val icon: Identifier = NBS3DF.id("directory")
    override val name: Component = Component.literal("${path.name}/")
    override val buttons: List<AbstractButton> = listOf()

    override fun updateButtons() {}

    override fun clickBackground(
        event: MouseButtonEvent,
        isDouble: Boolean
    ): Boolean {
        this.screen.navigateTo(this.path)
        return true
    }

}
