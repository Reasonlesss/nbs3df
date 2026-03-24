package codes.reason.nbs3df.song.converter

import codes.reason.nbs3df.song.NBSSong
import codes.reason.nbs3df.template.Args
import codes.reason.nbs3df.template.BlockTagItem
import codes.reason.nbs3df.template.CodeBlock
import codes.reason.nbs3df.template.SlotItem

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
                        item = hiddenTag,
                        slot = 26
                    )
                )
            )
        )
    }
}