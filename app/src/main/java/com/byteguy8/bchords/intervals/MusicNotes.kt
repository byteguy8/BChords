package com.byteguy8.bchords.intervals

import java.lang.IllegalArgumentException

sealed class MusicNote(
    val baseName: String,
    val isSharp: Boolean,
    val frequency: Float
) {
    fun flatName() = if (isSharp)
        "${nextNote(this).baseName}b" else baseName

    fun sharpName() = if (isSharp)
        "${baseName}#" else baseName
}

data class LocalizeNote(
    val octave: Int,
    val note: MusicNote
)

object CNote : MusicNote(
    "C",
    false,
    261.63f
)

object CSharpNote : MusicNote(
    "C",
    true,
    277.18f
)

object DNote : MusicNote(
    "D",
    false,
    293.66f
)

object DSharpNote : MusicNote(
    "D",
    true,
    311.13f
)

object ENote : MusicNote(
    "E",
    false,
    329.63f
)

object FNote : MusicNote(
    "F",
    false,
    349.23f
)

object FSharpNote : MusicNote(
    "F",
    true,
    369.99f
)

object GNote : MusicNote(
    "G",
    false,
    392.00f
)

object GSharpNote : MusicNote(
    "G",
    true,
    415.30f
)

object ANote : MusicNote(
    "A",
    false,
    440.00f
)

object ASharpNote : MusicNote(
    "A",
    true,
    466.16f
)

object BNote : MusicNote(
    "B",
    false,
    493.88f
)

val musicNotes = listOf(
    CNote,
    CSharpNote,
    DNote,
    DSharpNote,
    ENote,
    FNote,
    FSharpNote,
    GNote,
    GSharpNote,
    ANote,
    ASharpNote,
    BNote
)

fun nextNote(note: MusicNote): MusicNote {
    val index = musicNotes.indexOf(note)

    return if (index + 1 >= musicNotes.size) {
        musicNotes[0]
    } else {
        musicNotes[index + 1]
    }
}

fun nextNote(count: Int, from: Int, octave: Int): LocalizeNote {
    if (from < 1 || from > musicNotes.size) {
        throw IllegalArgumentException("Illegal from index $from. Index should be from 1 to ${musicNotes.size}, inclusive")
    }

    return nextNote(count, musicNotes[from - 1], octave)
}

fun nextNote(count: Int, from: MusicNote, octave: Int = 3): LocalizeNote {
    var counter = 0
    var currentOctave = octave
    var currentNote = musicNotes.indexOf(from)

    while (true) {
        if (currentNote + 1 >= musicNotes.size) {
            currentNote = 0
            currentOctave++
        } else {
            currentNote += 1
        }

        counter++

        if (counter == count) {
            break
        }
    }

    return LocalizeNote(currentOctave, musicNotes[currentNote])
}

fun previousNote(note: MusicNote): MusicNote {
    val index = musicNotes.indexOf(note)

    return if (index - 1 < 0) {
        musicNotes[musicNotes.size - 1]
    } else {
        musicNotes[index - 1]
    }
}

fun previousNote(count: Int, from: Int, octave: Int = 3): LocalizeNote {
    if (from < 1 || from > musicNotes.size) {
        throw IllegalArgumentException("Illegal from index $from. Index should be from 1 to ${musicNotes.size}, inclusive")
    }

    return previousNote(count, musicNotes[from - 1], octave)
}

fun previousNote(count: Int, from: MusicNote, octave: Int = 3): LocalizeNote {
    if (octave - count / musicNotes.size < 1) {
        throw IllegalArgumentException("count exceed the octave minimum, which is 1")
    }

    var counter = 0
    var currentOctave = octave
    var currentNote = musicNotes.indexOf(from)

    while (true) {
        if (currentNote - 1 < 0) {
            currentOctave--
            currentNote = musicNotes.size - 1
        } else {
            currentNote -= 1
        }

        counter++

        if (counter == count) {
            break
        }
    }

    return LocalizeNote(currentOctave, musicNotes[currentNote])
}