package cloud.emilys.nbs3df.screen

import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.layouts.LayoutElement
import java.util.function.Consumer

class DynamicWidthSpacer : LayoutElement {

    private var x = 0
    private var y = 0
    private var _width: Int = 0

    override fun setX(x: Int) {
        this.x = x
    }

    override fun setY(y: Int) {
        this.y = y
    }

    override fun getX() = this.x
    override fun getY() = this.y
    override fun getWidth() = this._width

    fun setWidth(width: Int) {
        this._width = width;
    }

    override fun getHeight() = 0

    override fun visitWidgets(consumer: Consumer<AbstractWidget>) {
    }
}