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
                sound = "Harp"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Bass"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Bass Drum"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Snare Drum"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Hat"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Guitar"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Flute"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Bell"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Chime"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Xylophone"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Iron Xylophone"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Cow Bell"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Didgeridoo"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Bit"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Banjo"
            )
        ),
        SoundItem(
            SoundItem.SoundData(
                1.0f,
                2.0f,
                sound = "Pling"
            )
        )
    )

    fun convertCustomInstrument(instrument: CustomInstrument): SoundItem {
        val sound = CustomInstrumentData.findSound(instrument.soundFile)

        if (sound != null) {
            return SoundItem(
                SoundItem.SoundData(
                    pitch = 1.0f,
                    vol = 2.0f,
                    sound = sound.name,
                    variant = sound.variantName
                )
            )
        }

        if (CustomInstrumentData.isSoundName(instrument.name)) {
            return SoundItem(
                SoundItem.SoundData(
                    pitch = 1.0f,
                    vol = 2.0f,
                    sound = instrument.name
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
                key = "minecraft:$normalized"
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