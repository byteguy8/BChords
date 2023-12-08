package com.byteguy8.bchords.intervals

import com.byteguy8.bchords.utils.addAtEnd

enum class IntervalType {
    MINOR,
    MAJOR,
    FAIR,
    AUGMENTED,
    DIMINISHED
}

sealed class NoteInterval(
    val semitones: Int,
    val type: IntervalType
) {
    fun forward(note: MusicNote, octave: Int = 3): LocalizeNote {
        return nextNote(semitones, note, octave)
    }

    fun backward(note: MusicNote, octave: Int = 3): LocalizeNote {
        return previousNote(semitones, note, octave)
    }

    open fun forwardStr(note: MusicNote): String {
        val lNote = forward(note)

        return when (type) {
            IntervalType.MINOR -> lNote.note.flatName()
            IntervalType.MAJOR -> lNote.note.sharpName()
            IntervalType.FAIR -> lNote.note.sharpName()
            IntervalType.AUGMENTED -> lNote.note.sharpName()
            IntervalType.DIMINISHED -> lNote.note.flatName()
        }
    }

    open fun backwardStr(note: MusicNote): String {
        val lNote = backward(note)

        return when (type) {
            IntervalType.MINOR -> lNote.note.flatName()
            IntervalType.MAJOR -> lNote.note.baseName
            IntervalType.FAIR -> lNote.note.baseName
            IntervalType.AUGMENTED -> lNote.note.sharpName()
            IntervalType.DIMINISHED -> lNote.note.flatName()
        }
    }
}

object MinorSecond : NoteInterval(1, IntervalType.MINOR)
object MajorSecond : NoteInterval(2, IntervalType.MAJOR)
object MinorThird : NoteInterval(3, IntervalType.MINOR)
object MajorThird : NoteInterval(4, IntervalType.MAJOR)
object FairFourth : NoteInterval(5, IntervalType.FAIR)
object AugmentedFourth : NoteInterval(6, IntervalType.AUGMENTED)
object DiminishedFifth : NoteInterval(6, IntervalType.DIMINISHED)
object FairFifth : NoteInterval(7, IntervalType.FAIR)
object AugmentedFifth : NoteInterval(8, IntervalType.AUGMENTED)
object MinorSixth : NoteInterval(8, IntervalType.MINOR)
object MajorSixth : NoteInterval(9, IntervalType.MAJOR)

object DiminishedSeventh : NoteInterval(9, IntervalType.DIMINISHED) {
    override fun forwardStr(note: MusicNote): String {
        val lNote = nextNote(11, note)
        return addAtEnd(lNote.note.flatName(), "b", 2)
    }
}

object MinorSeventh : NoteInterval(10, IntervalType.MINOR)
object MajorSeventh : NoteInterval(11, IntervalType.MAJOR)
object FairOctave : NoteInterval(12, IntervalType.FAIR)