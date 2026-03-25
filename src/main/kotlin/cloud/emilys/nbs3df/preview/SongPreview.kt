package cloud.emilys.nbs3df.preview

import cloud.emilys.nbs3df.song.NBSSong
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import kotlin.math.pow

class SongPreview(
    private val song: NBSSong,
    private val sounds: List<PreviewSound?>
) {
    companion object {
        private const val PREVIEW_DURATION = 900
    }
    private val startTick = maxOf(0, (song.notes.size - PREVIEW_DURATION) / 2)
    private val endTick = minOf(song.notes.size, startTick + PREVIEW_DURATION)
    private var tick = startTick
    var playing: Boolean = true

    fun tick() {
        if (tick >= endTick) {
            playing = false
            return
        }

        val minecraft = Minecraft.getInstance()
        val level = minecraft.level!!
        val player = minecraft.player!!

        this.song.notes[this.tick++].orEmpty().forEach {
            val sound = sounds[it.instrument.toInt()] ?: return@forEach
            val layer = song.layers[it.layer]

            val pitch = if (it.instrument >= song.header.vanillaInstruments) {
                val instrumentKey = song.instruments[it.instrument - song.header.vanillaInstruments].soundKey
                calculatePitchValue(it.key + instrumentKey - 45, it.pitch)
            } else {
                calculatePitchValue(it.key.toInt(), it.pitch)
            }

            val instance = SimpleSoundInstance(
                sound.event,
                SoundSource.RECORDS,
                (it.velocity * layer.volume) / 10000f,
                pitch,
                RandomSource.create(sound.seed ?: level.random.nextLong()),
                player.x,
                player.y,
                player.z
            );

            minecraft.soundManager.play(instance)
        }
    }

    private fun calculatePitchValue(key: Int, finePitch: Short): Float {
        val coercedKey = key.coerceIn(33, 57)
        val clampedFinePitch = finePitch.coerceIn(-1200, 1200)

        val semitones = ((coercedKey - 33.0) + (clampedFinePitch / 100.0)).coerceIn(0.0, 24.0)
        val freqRatio = 2.0.pow(semitones / 12.0)

        return (0.5 + ((freqRatio - 1) / 3) * 1.5).toFloat()
    }
}