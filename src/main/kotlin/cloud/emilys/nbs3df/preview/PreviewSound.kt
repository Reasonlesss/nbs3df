package cloud.emilys.nbs3df.preview

import cloud.emilys.nbs3df.song.CustomInstrumentData
import cloud.emilys.nbs3df.song.CustomInstrument
import cloud.emilys.nbs3df.song.NBSSong
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import java.util.*

data class PreviewSound(
    val event: SoundEvent,
    val seed: Long?
) {
    companion object {
        val vanillaSounds = listOf(
            vanillaSound("minecraft:block.note_block.harp"),
            vanillaSound("minecraft:block.note_block.bass"),
            vanillaSound("minecraft:block.note_block.basedrum"),
            vanillaSound("minecraft:block.note_block.snare"),
            vanillaSound("minecraft:block.note_block.hat"),
            vanillaSound("minecraft:block.note_block.guitar"),
            vanillaSound("minecraft:block.note_block.flute"),
            vanillaSound("minecraft:block.note_block.bell"),
            vanillaSound("minecraft:block.note_block.chime"),
            vanillaSound("minecraft:block.note_block.xylophone"),
            vanillaSound("minecraft:block.note_block.iron_xylophone"),
            vanillaSound("minecraft:block.note_block.cow_bell"),
            vanillaSound("minecraft:block.note_block.didgeridoo"),
            vanillaSound("minecraft:block.note_block.bit"),
            vanillaSound("minecraft:block.note_block.banjo"),
            vanillaSound("minecraft:block.note_block.pling")
        )

        private fun vanillaSound(name: String) : PreviewSound {
            return PreviewSound(SoundEvent(Identifier.parse(name), Optional.empty()), null)
        }

        fun fromCustomInstrument(instrument: CustomInstrument) : PreviewSound? {
            val sound = CustomInstrumentData.findSound(instrument.soundFile)
            if (sound != null) {
                val identifier = Identifier.parse(sound.key)
                val seed = sound.seed
                return PreviewSound(SoundEvent(identifier, Optional.empty()), seed)
            }
            if (CustomInstrumentData.isSoundName(instrument.name)) {
                val identifier = Identifier.parse(CustomInstrumentData.names[instrument.name]!!)
                return PreviewSound(SoundEvent(identifier, Optional.empty()), null)
            }
            return null
        }

        fun buildPreviewSounds(song: NBSSong) : List<PreviewSound?> {
            return buildList {
                val vanillaCount = song.header.vanillaInstruments.toInt()
                addAll(vanillaSounds.take(vanillaCount))
                addAll(song.instruments.map {
                    fromCustomInstrument(it)
                })
            }
        }
    }
}
