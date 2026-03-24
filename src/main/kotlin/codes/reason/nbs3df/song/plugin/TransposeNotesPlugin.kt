package codes.reason.nbs3df.song.plugin

import codes.reason.nbs3df.song.NBSSong

object TransposeNotesPlugin : NBSPlugin {
    override fun apply(file: NBSSong): NBSSong {
        val transposedNotes = file.notes.mapValues { (_, noteList) ->
            noteList.map { note ->
                val transposedKey = transposeKey(note.key.toInt())
                note.copy(key = transposedKey.toByte())
            }
        }
        return file.copy(notes = transposedNotes)
    }

    private fun transposeKey(key: Int): Int {
        var transposedKey = key
        while (transposedKey < 33) {
            transposedKey += 12
        }
        while (transposedKey > 57) {
            transposedKey -= 12
        }
        return transposedKey
    }
}
