package codes.reason.nbs3df.screen

import codes.reason.nbs3df.NBS3DF
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.util.ARGB

class DirectorySelectorEntry(val name: String) : SongSelectorEntry() {

    companion object {
        const val NORMAL_LINE_HEIGHT = 8

        val ICON = NBS3DF.id("directory")
    }

    override fun renderContent(
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        hovered: Boolean,
        delta: Float
    ) {
        if (hovered) {
            graphics.fill(this.x, this.y, this.x + this.width, this.y + this.height, ARGB.white(0.2f))
        }

        val minecraft = Minecraft.getInstance()
        val y = this.y + (this.height - NORMAL_LINE_HEIGHT) / 2

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ICON, 4 + this.x, y, 8, 8)
        graphics.drawString(minecraft.font, "$name/", 16 + this.x, y, ARGB.white(1f))
    }

    override fun children(): List<GuiEventListener> = listOf()
    override fun narratables(): List<NarratableEntry> = listOf()

}
