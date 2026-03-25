package cloud.emilys.nbs3df.screen.entry

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.ARGB

abstract class FileNavigatorEntry : ContainerObjectSelectionList.Entry<FileNavigatorEntry>() {

    companion object {
        private const val CONTENT_HEIGHT = 8
        private const val ICON_SIZE = 8
        private const val BUTTON_SIZE = 16
        private const val SPACING = 4
        private val HOVER_COLOR = ARGB.white(0.2f)
        const val ENTRY_HEIGHT = 20
    }

    abstract val icon: Identifier?
    abstract val name: Component
    abstract val buttons: List<AbstractButton>

    private val textWidget = StringWidget(Component.empty(), Minecraft.getInstance().font)

    override fun renderContent(
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        hovered: Boolean,
        delta: Float
    ) {
        if (hovered) {
            graphics.fill(this.x, this.y, this.x + this.width, this.y + this.height, HOVER_COLOR)
        }

        val iconWidth = if (icon != null) { ICON_SIZE + SPACING } else 0
        val buttonWidth = buttons.size * BUTTON_SIZE + (buttons.size - 1) * SPACING
        val remainingWidth = this.width - (2 * SPACING) - iconWidth - buttonWidth

        val contentY = this.y + (this.height - CONTENT_HEIGHT) / 2

        this.textWidget.setMaxWidth(remainingWidth, StringWidget.TextOverflow.SCROLLING)
        this.textWidget.x = this.x + iconWidth + SPACING
        this.textWidget.y = contentY
        this.textWidget.message = this.name
        this.textWidget.render(graphics, mouseX, mouseY, delta)

        val icon = this.icon
        if (icon != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon, SPACING + this.x, contentY, ICON_SIZE, ICON_SIZE)
        }

        this.updateButtons()

        var buttonX = this.x + this.width - BUTTON_SIZE - SPACING
        val buttonY = this.y + (ENTRY_HEIGHT - BUTTON_SIZE) / 2
        for (button in this.buttons.reversed()) {
            button.setPosition(buttonX, buttonY)
            button.render(graphics, mouseX, mouseY, delta)
            buttonX -= BUTTON_SIZE + SPACING
        }
    }

    abstract fun updateButtons()

    abstract fun clickBackground(
        event: MouseButtonEvent,
        isDouble: Boolean
    ): Boolean

    override fun mouseClicked(event: MouseButtonEvent, isDouble: Boolean): Boolean {
        if (super.mouseClicked(event, isDouble)) {
            return true
        }

        val isInRange = event.x.toInt() in this.x..(this.x + this.width)
             && event.y.toInt() in this.y..(this.y + this.height)

        if (isInRange && this.clickBackground(event, isDouble)) {
            Minecraft.getInstance().soundManager.play(
                SimpleSoundInstance.forUI(
                    SoundEvents.UI_BUTTON_CLICK,
                    1.0f
                )
            )
            return true
        }

        return false
    }

    override fun children(): List<GuiEventListener> = buildList {
        this.add(textWidget)
        this.addAll(buttons)
    }

    override fun narratables(): List<NarratableEntry> = buildList {
        this.add(textWidget)
        this.addAll(buttons)
    }

}