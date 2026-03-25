package cloud.emilys.nbs3df.song.converter

import cloud.emilys.nbs3df.song.NBSSong
import cloud.emilys.nbs3df.util.template.Args
import cloud.emilys.nbs3df.util.template.BlockTagItem
import cloud.emilys.nbs3df.util.template.CodeBlock
import cloud.emilys.nbs3df.util.template.SlotItem
import cloud.emilys.nbs3df.util.template.toCodeItem

object LineStarterGenerator {
    private val hiddenTag = BlockTagItem(
        BlockTagItem.BlockTagData(
            option = "False",
            tag = "Is Hidden",
            action = "dynamic",
            block = "func"
        )
    )

    fun createLineStarter(song: NBSSong): CodeBlock {
        return CodeBlock(
            block = "func",
            data = song.header.meta.name,
            args = Args(
                items = listOf(
                    SlotItem(
                        item = SongIconGenerator.makeSongIcon(song).toCodeItem(),
                        slot = 0
                    ),
                    SlotItem(
                        item = hiddenTag,
                        slot = 26
                    )
                )
            )
        )
    }
}