package cloud.emilys.nbs3df.screen

import cloud.emilys.nbs3df.preview.SongPreviewHandler
import cloud.emilys.nbs3df.screen.entry.DirectoryNavigatorEntry
import cloud.emilys.nbs3df.screen.entry.ParentNavigatorEntry
import cloud.emilys.nbs3df.screen.entry.SongNavigatorEntry
import cloud.emilys.nbs3df.util.important
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Checkbox
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.layouts.SpacerElement
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import java.nio.file.Path

class FileNavigatorScreen(path: Path) : Screen(
    Component.translatable("nbs3df.screen.import.title")
) {
    val titleWidget = StringWidget(Component.literal(""), font)
    val pathWidget = StringWidget(Component.literal(""), font)

    val layout = HeaderAndFooterLayout(this)
    val list = FileNavigatorList(this)

    var lastVisitedPath = path

    var showHiddenFiles = false

    val hiddenFileCheckbox = Checkbox
        .builder(Component.translatable("nbs3df.screen.import.showHiddenFiles"), Minecraft.getInstance().font)
        .onValueChange { _, bool ->
            showHiddenFiles = bool
            this.navigateTo(lastVisitedPath)
        }
        .selected(false)
        .build()

    val closeButton = Button
        .builder(Component.translatable("nbs3df.screen.import.cancel")) {
            this.onClose()
        }.build()

    val refreshButton = Button
        .builder(Component.literal("⇵").withStyle(ChatFormatting.AQUA)) {
            this.navigateTo(this.lastVisitedPath)
        }
        .tooltip(Tooltip.create(Component.translatable("nbs3df.screen.import.refresh")))
        .bounds(0, 0, 16, 16)
        .build()

    val homeButton = Button
        .builder(Component.literal("★").withStyle(ChatFormatting.YELLOW)) {
            this.navigateTo(Path.of(System.getProperty("user.home")))
        }
        .tooltip(Tooltip.create(Component.translatable("nbs3df.screen.import.homeDirectory")))
        .bounds(0, 0, 16, 16)
        .build()

    val stopButton = Button
        .builder(Component.literal("⏹").withStyle(ChatFormatting.RED)) {
            SongPreviewHandler.stopSong()
        }
        .tooltip(Tooltip.create(Component.translatable("nbs3df.screen.import.stopPreview")))
        .bounds(0, 0, 16, 16)
        .build()

    val leftSpacer = DynamicWidthSpacer()
    val rightSpacer = DynamicWidthSpacer()


    init {
        this.navigateTo(path)
    }

    fun navigateTo(path: Path) {
        lastVisitedPath = path

        val path = path.toAbsolutePath()

        titleWidget.message = Component.translatable("nbs3df.screen.import.title").important()
        pathWidget.message = Component.literal(path.toString()).withStyle(ChatFormatting.GRAY)

        this.list.clear();
        this.repositionElements()
        FileNavigator.searchPath(path, showHiddenFiles)
            .thenAccept {
                Minecraft.getInstance().execute {
                    for (file in it) {
                        this.list.add(
                            when (file.type) {
                                FileType.PARENT -> ParentNavigatorEntry(this, file.path)
                                FileType.SONG -> SongNavigatorEntry(this, file.path)
                                FileType.DIRECTORY -> DirectoryNavigatorEntry(this, file.path)
                            }
                        )
                    }
                    this.repositionElements()
                }
            }
            .exceptionally {
                it.printStackTrace()
                null
            }
    }

    override fun removed() {
        SongPreviewHandler.stopSong()
    }

    override fun init() {
        val header = layout.addToHeader(LinearLayout.vertical().spacing(2))
        header.defaultCellSetting().alignHorizontallyCenter()
        header.addChild(this.titleWidget)
        header.addChild(this.pathWidget)

        layout.addToContents(this.list)

        val footer = layout.addToFooter(LinearLayout.horizontal())
        footer.defaultCellSetting().alignVerticallyMiddle()
        footer.addChild(this.hiddenFileCheckbox)
        footer.addChild(this.leftSpacer)
        footer.addChild(this.closeButton)
        footer.addChild(this.rightSpacer)
        footer.addChild(this.stopButton)
        footer.addChild(SpacerElement.width(2))
        footer.addChild(this.homeButton)
        footer.addChild(SpacerElement.width(2))
        footer.addChild(this.refreshButton)

        this.layout.visitWidgets {
            this.addRenderableWidget(it)
        }
        this.repositionElements()
    }

    override fun render(
        guiGraphics: GuiGraphics,
        i: Int,
        j: Int,
        f: Float
    ) {
        this.stopButton.visible = SongPreviewHandler.isPlaying()
        super.render(guiGraphics, i, j, f)
    }

    override fun repositionElements() {
        val halfScreenWidth = (this.width - this.closeButton.width - 20) / 2

        this.leftSpacer.width = halfScreenWidth - this.hiddenFileCheckbox.width
        this.rightSpacer.width = halfScreenWidth - this.refreshButton.width - this.homeButton.width - this.stopButton.width - 4

        this.list.updateSize(this.width, this.layout)
        this.layout.arrangeElements()
    }

}