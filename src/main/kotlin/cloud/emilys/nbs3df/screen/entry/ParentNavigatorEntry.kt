package cloud.emilys.nbs3df.screen.entry

import cloud.emilys.nbs3df.screen.FileNavigatorScreen
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import java.nio.file.Path

class ParentNavigatorEntry(val screen: FileNavigatorScreen, val path: Path) : FileNavigatorEntry() {

    override val icon: Identifier? = null
    override val name: Component = Component.translatable("nbs3df.screen.import.parentDirectory")
        .withStyle(ChatFormatting.GRAY)
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
