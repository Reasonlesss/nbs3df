package cloud.emilys.nbs3df.song.converter

import cloud.emilys.nbs3df.song.CustomInstrumentData
import cloud.emilys.nbs3df.song.CustomInstrument
import cloud.emilys.nbs3df.util.template.VariableItem
import cloud.emilys.nbs3df.util.template.SoundItem
import cloud.emilys.nbs3df.song.NBSSong
import cloud.emilys.nbs3df.util.template.CodeBlock
import cloud.emilys.nbs3df.util.template.createListCodeBlocks

object InstrumentConverter {
    private const val INSTRUMENTS_VARIABLE_NAME = "nbs:instruments"

    private val instrumentVariable = VariableItem(
        VariableItem.VariableData(
            name = INSTRUMENTS_VARIABLE_NAME,
            scope = VariableItem.Scope.LOCAL
        )
    )

    private val vanillaInstruments = listOf(
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.harp"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.bass"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.basedrum"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.snare"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.hat"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.guitar"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.flute"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.bell"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.chime"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.xylophone"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.iron_xylophone"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.cow_bell"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.didgeridoo"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.bit"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.banjo"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "minecraft:block.note_block.pling"
            )
        ),
        SoundItem(
            data = SoundItem.SoundData(
                pitch = 1.0f,
                vol = 2.0f,
                sound = "minecraft:block.note_block.trumpet"
            )
        ),
        SoundItem(
            data = SoundItem.SoundData(
                pitch = 1.0f,
                vol = 2.0f,
                sound = "minecraft:block.note_block.trumpet_exposed"
            )
        ),
        SoundItem(
            data = SoundItem.SoundData(
                pitch = 1.0f,
                vol = 2.0f,
                sound = "minecraft:block.note_block.trumpet_weathered"
            )
        ),
        SoundItem(
            data = SoundItem.SoundData(
                pitch = 1.0f,
                vol = 2.0f,
                sound = "minecraft:block.note_block.trumpet_oxidized"
            )
        )
    )

    fun convertCustomInstrument(instrument: CustomInstrument): SoundItem {
        val soundFile = CustomInstrumentData.findSoundFile(instrument.soundFile)

        if (soundFile != null) {
            return SoundItem(
                SoundItem.SoundData(
                    pitch = 1.0f,
                    vol = 2.0f,
                    sound = soundFile.key,
                    variant = soundFile.variantName
                )
            )
        }

        val soundKey = CustomInstrumentData.findSoundKey(instrument.name)

        if (soundKey != null) {
            return SoundItem(
                SoundItem.SoundData(
                    pitch = 1.0f,
                    vol = 2.0f,
                    sound = soundKey
                )
            )
        }

        val normalized = instrument.name
            .lowercase()
            .replace(" ", "_")
            .replace(Regex("[^a-z0-9_./-]"), "")

        return SoundItem(
            SoundItem.SoundData(
                pitch = 1.0f,
                vol = 2.0f,
                sound = "minecraft:$normalized"
            )
        )
    }

    fun convertInstruments(song: NBSSong): List<CodeBlock> {
        val instruments = buildList {
            val vanillaCount = song.header.vanillaInstruments.toInt()
            addAll(vanillaInstruments.take(vanillaCount))
            addAll(song.instruments.map {
                convertCustomInstrument(it)
            })
        }
        return createListCodeBlocks(
            variableItem = instrumentVariable,
            codeItems = instruments
        )
    }
}