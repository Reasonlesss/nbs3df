package cloud.emilys.nbs3df

import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemLore

object PlayerIconGenerator {
    private val NAME = Component.literal("Note Block Music Player")
        .withColor(0xFF91C2)
        .withStyle { style -> style.withItalic(false) }
    private val VERSION = Component.literal("NBS3DF v1.0 Player")
        .withStyle(ChatFormatting.DARK_GRAY)
        .withStyle { style -> style.withItalic(false) }
    private val CREDIT = Component.literal("github.com/Reasonlesss/nbs3df")
        .withStyle(ChatFormatting.GRAY, ChatFormatting.UNDERLINE)
        .withStyle { style -> style.withItalic(false) }
    private val DESCRIPTION = listOf(
        "A compact music player for",
        "playing songs imported using ",
        "the NBS3DF music format.",
        "",
        "To use, simply call the",
        "generated music function",
        "followed by this function",
        "to play the song"
    ).map {
        Component.literal(it)
            .withColor(0xFFE8F7)
            .withStyle { style -> style.withItalic(false) }
    }

    fun makePlayerIcon() : ItemStack {
        val stack = ItemStack(Items.JUKEBOX)
        stack[DataComponents.CUSTOM_NAME] = NAME
        stack[DataComponents.LORE] = ItemLore(buildList {
            add(VERSION)
            add(Component.empty())
            addAll(DESCRIPTION)
            add(Component.empty())
            add(CREDIT)
        })
        return stack
    }
}