package codes.reason.nbs3df.song.converter

import codes.reason.nbs3df.song.NBSSong
import codes.reason.nbs3df.util.ByteWriter
import kotlin.math.pow

object SongByteEncoder {
    /*
        Byte data format v1:
          unsigned short - Delay from previous note
           unsigned byte - Instrument
             signed byte - Volume
          unsigned short - Pitch
             signed byte - Panning (-100 to 100)
     */
    fun encodeSongBytes(song: NBSSong): ByteArray {
        val writer = ByteWriter()

        var previousTick = 0
        song.notes.keys.sorted().forEach { tick ->
            val initialDelay = (tick - previousTick).toShort()
            previousTick = tick

            song.notes[tick].orEmpty().forEachIndexed { index, note ->
                val layer = song.layers[note.layer]
                val delay = if (index == 0) initialDelay else 0.toShort()

                writer.writeShort(delay)
                writer.writeByte(note.instrument)
                writer.writeByte((note.velocity * layer.volume) / 100)
                // Calculate the custom instrument offset
                if (note.instrument >= song.header.vanillaInstruments) {
                    val instrumentKey = song.instruments[note.instrument - song.header.vanillaInstruments].soundKey

                    writer.writeShort(calculatePitchValue(note.key + instrumentKey - 45, note.pitch))
                } else {
                    writer.writeShort(calculatePitchValue(note.key.toInt(), note.pitch))
                }
                writer.writeByte((((note.panning + layer.stereo) / 2) - 100) * -1)
            }
        }

        return writer.toByteArray()
    }

    private fun calculatePitchValue(key: Int, finePitch: Short): Int {
        val coercedKey = key.coerceIn(33, 57)
        val clampedFinePitch = finePitch.coerceIn(-1200, 1200)

        val semitones = ((coercedKey - 33.0) + (clampedFinePitch / 100.0)).coerceIn(0.0, 24.0)
        val freqRatio = 2.0.pow(semitones / 12.0)

        return ((0.5 + ((freqRatio - 1) / 3) * 1.5) * 1000.0).toInt()
    }
}