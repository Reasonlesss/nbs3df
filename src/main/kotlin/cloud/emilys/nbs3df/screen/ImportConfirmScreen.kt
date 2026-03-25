package cloud.emilys.nbs3df.screen

import cloud.emilys.nbs3df.NBS3DF
import cloud.emilys.nbs3df.song.NBSSong
import cloud.emilys.nbs3df.util.important
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.ConfirmScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

object ImportConfirmScreen {

    fun open(song: NBSSong, items: List<ItemStack>) {
        val songName = song.header.meta.name.ifEmpty {
            song.fileName
        }
        val author = if (song.header.meta.author.isNotEmpty()) {
            Component.literal(song.header.meta.author)
        } else {
            Component.translatable("nbs3df.screen.copyright.unknownCreator")
        }.important()

        Minecraft.getInstance().setScreen(ConfirmScreen(
            { accepted ->
                Minecraft.getInstance().setScreen(null)
                if (accepted) {
                    NBS3DF.giveIfNotPresent(items)
                }
            },
            Component.translatable("nbs3df.screen.copyright.title").important(),
            Component.translatable("nbs3df.screen.copyright.description",
                author,
                Component.literal(songName).important()
            ),
            Component.translatable("nbs3df.screen.copyright.yes"),
            Component.translatable("nbs3df.screen.copyright.no")
        ))
    }

}