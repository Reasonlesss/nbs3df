package codes.reason.nbs3df.command

import codes.reason.nbs3df.PlayerTemplate
import codes.reason.nbs3df.util.giveItem
import codes.reason.nbs3df.util.makeTemplateItem
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.world.item.Items

object PlayerCommand : Command() {

    override fun get(): LiteralArgumentBuilder<FabricClientCommandSource> {
        return literal("player").executes {
            val template = makeTemplateItem(
                name = Component.text("NBS3DF Music Player")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.color(0xAA7FFF))
                    .append(Component.text(" [v1.0]", NamedTextColor.DARK_GRAY)),
                lore = listOf(
                    gray("A compact music player for playing"),
                    gray("songs using the NBS3DF music format."),
                    Component.empty(),
                    gray("To use, simply call the generated"),
                    gray("music function followed by this"),
                    gray("function to play the song"),
                    Component.empty(),
                    red("Only supports NBS3DF songs."),
                ),
                template = PlayerTemplate.TEMPLATE,
                type = Items.JUKEBOX
            )
            giveItem(template)
            1
        }
    }

    private fun gray(text: String) = Component.text(text, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
    private fun red(text: String) = Component.text(text, NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)

}