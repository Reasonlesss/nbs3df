package cloud.emilys.nbs3df.song.converter

import cloud.emilys.nbs3df.song.NBSSong
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemLore

object SongIconGenerator {
    private val VERSION = Component.literal("NBS3DF v1.0 Song")
        .withStyle(ChatFormatting.DARK_GRAY)
        .withStyle { style -> style.withItalic(false) }
    private val MUSIC_NOTES = listOf(
        Component.empty()
            .append(Component.literal("    ♪").withColor(0xF34199))
            .append(Component.literal("      ♪").withColor(0xF34100)),
        Component.empty()
            .append(Component.literal("  ♪").withColor(0x77D700))
            .append(Component.literal("    ♪").withColor(0x009ABC)),
        Component.empty()
            .append(Component.literal("      ♪").withColor(0x8600CC))
            .append(Component.literal("     ♪").withColor(0x020AFE))
    ).map {
        it.withStyle { style -> style.withItalic(false) }
    }

    fun makeSongIcon(song: NBSSong, min: Int = -1, max: Int = -1): ItemStack {
        val icon = ItemStack(Items.NOTE_BLOCK)

        val name = Component.literal(song.header.meta.name.ifEmpty {
            song.fileName
        })
            .withColor(0xFF91C2)
            .withStyle { style -> style.withItalic(false) }
        if (min != -1 && max > 1) {
            name.append(Component.literal(" [$min/$max]")
                .withStyle(ChatFormatting.DARK_GRAY))
        }
        icon[DataComponents.CUSTOM_NAME] = name
        icon[DataComponents.LORE] = ItemLore(buildList {
            add(VERSION)
            addAll(MUSIC_NOTES)
            val author = song.header.meta.author
            val truncated = if (author.length > 45) {
                author.take(44) + "..."
            } else {
                author
            }

            if (author.isNotEmpty()) {
                add(Component.empty())
                add(
                    Component.literal("Created by $truncated")
                        .withStyle(ChatFormatting.GRAY)
                        .withStyle { it.withItalic(false) })
            }
        })
        return icon
    }

}