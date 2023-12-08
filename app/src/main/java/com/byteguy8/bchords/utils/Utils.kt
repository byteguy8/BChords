package com.byteguy8.bchords.utils

import android.content.Context
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.byteguy8.bchords.composables.MAIN_NOTE_NAME
import com.byteguy8.bchords.composables.chords
import com.byteguy8.bchords.intervals.LocalizeNote
import com.byteguy8.bchords.intervals.createAugmentedChord
import com.byteguy8.bchords.intervals.createDiminishedChord
import com.byteguy8.bchords.intervals.createMajorChord
import com.byteguy8.bchords.intervals.createMajorSeventhChord
import com.byteguy8.bchords.intervals.createMinorChord
import com.byteguy8.bchords.intervals.createMinorSeventhChord
import com.byteguy8.bchords.intervals.musicNotes
import java.io.File

fun addAtEnd(input: String, s: String, count: Int): String {
    val sb = StringBuilder()

    sb.append(input)

    for (x in 1..count) {
        sb.append(s)
    }

    return sb.toString()
}

fun createNote(
    lNote: LocalizeNote,
    context: Context
): Either<NoteCreationError, String> {
    var multiple = lNote.octave - 3

    if (multiple == 0) {
        multiple = 1
    }

    val noteName = "${lNote.note.sharpName()}${lNote.octave}_48000.wav"

    val appDir = context.filesDir
    val noteFile = File(appDir, noteName)

    if (noteFile.exists()) {
        return Right(noteFile.absolutePath)
    }

    val c3NoteFile = File(appDir, MAIN_NOTE_NAME)

    val command =
        "-i ${c3NoteFile.absolutePath} -filter:a \"asetrate=${lNote.note.frequency}/261.63*$multiple*48000\" ${noteFile.absolutePath}"

    val session = FFmpegKit.execute(command)

    return if (ReturnCode.isSuccess(session.returnCode)) {
        Right(noteFile.absolutePath)
    } else {
        session.logs?.forEach {
            System.err.println(it?.message ?: "invalid log")
        }

        Left(NoteCreationError("Unexpected error"))
    }
}

fun createChord(
    type: String,
    chord: String,
    octave: Int,
    paths: List<String>,
    context: Context
): Either<ChordCreationError, String> {
    val appDir = context.filesDir

    val filteredChord = chord
        .replace(" ", "_")
        .replace("/", "_")

    val filteredType = type
        .replace(" ", "_")

    val chordName = "${filteredChord}${octave}_${filteredType}_48000.wav"
    val chordFile = File(appDir, chordName)

    if (chordFile.exists()) {
        return Right(chordFile.absolutePath)
    }

    val inputStrBuilder = StringBuilder()
    val filterStrBuilder = StringBuilder()

    for (x in paths.indices) {
        val path = paths[x]

        inputStrBuilder.append("-i $path")

        if (x + 1 < paths.size) {
            inputStrBuilder.append(" ")
        }

        filterStrBuilder.append("[$x:a]")
    }

    filterStrBuilder.append("amix=${paths.size}[aout]")

    val command =
        "$inputStrBuilder -filter_complex '$filterStrBuilder' -map '[aout]' ${chordFile.absolutePath}"

    val session = FFmpegKit.execute(command)

    return if (ReturnCode.isSuccess(session.returnCode)) {
        Right(chordFile.absolutePath)
    } else {
        session.logs?.forEach {
            System.err.println(it?.message ?: "invalid log")
        }

        Left(ChordCreationError("Unexpected error"))
    }
}

fun buildChord(
    type: String,
    chord: String,
    context: Context
): Either<ChordCreationError, String> {
    val paths = mutableListOf<String>()

    val notes: List<LocalizeNote> = when (type) {
        "Major" -> {
            createMajorChord(musicNotes[chords.indexOf(chord)])
        }

        "Minor" -> {
            createMinorChord(musicNotes[chords.indexOf(chord)])
        }

        "Augmented" -> {
            createAugmentedChord(musicNotes[chords.indexOf(chord)])
        }

        "Diminished" -> {
            createDiminishedChord(musicNotes[chords.indexOf(chord)])
        }

        "Major Seventh" -> {
            createMajorSeventhChord(musicNotes[chords.indexOf(chord)])
        }

        "Minor Seventh" -> {
            createMinorSeventhChord(musicNotes[chords.indexOf(chord)])
        }

        else -> {
            return Left(ChordCreationError("Not supported operation"))
        }
    }

    for (x in notes.indices) {
        val note = notes[x]

        when (val result = createNote(note, context)) {
            is Right -> paths.add(result.value)
            is Left -> {
                System.err.println(result.error.message)
                return Left(ChordCreationError("Unknown error"))
            }
        }
    }

    return createChord(type, chord, notes[0].octave, paths, context)
}