package com.byteguy8.bchords.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.byteguy8.bchords.ShowMessage
import com.byteguy8.bchords.utils.Left
import com.byteguy8.bchords.utils.Right
import com.byteguy8.bchords.utils.buildChord
import com.byteguy8.bchords.utils.createNote
import com.byteguy8.bchords.intervals.LocalizeNote
import com.byteguy8.bchords.intervals.createAugmentedChord
import com.byteguy8.bchords.intervals.createAugmentedChordStr
import com.byteguy8.bchords.intervals.createDiminishedChord
import com.byteguy8.bchords.intervals.createDiminishedChordStr
import com.byteguy8.bchords.intervals.createMajorChord
import com.byteguy8.bchords.intervals.createMajorChordStr
import com.byteguy8.bchords.intervals.createMajorSeventhChord
import com.byteguy8.bchords.intervals.createMajorSeventhChordStr
import com.byteguy8.bchords.intervals.createMinorChord
import com.byteguy8.bchords.intervals.createMinorChordStr
import com.byteguy8.bchords.intervals.createMinorSeventhChord
import com.byteguy8.bchords.intervals.createMinorSeventhChordStr
import com.byteguy8.bchords.intervals.musicNotes
import com.byteguy8.bchords.playAudio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val types = listOf(
    "Major",
    "Minor",
    "Augmented",
    "Diminished",
    "Major Seventh",
    "Minor Seventh"
)

val chords = listOf(
    "C",
    "C# / Db",
    "D",
    "D# / Eb",
    "E",
    "F",
    "F# / Gb",
    "G",
    "G# / Ab",
    "A",
    "A# / Bb",
    "B"
)

const val MAIN_NOTE_NAME = "C3_48000.wav"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChordScreen(
    showMessage: ShowMessage,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(types[0]) }
    var expandTypes by remember { mutableStateOf(false) }

    var selectedChord by remember { mutableStateOf(chords[0]) }
    var expandChord by remember { mutableStateOf(false) }

    var chord by remember { mutableStateOf("") }
    var chordNotes: List<LocalizeNote> by remember { mutableStateOf(emptyList()) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val chordNotesHScrollState = rememberScrollState()
    var active by remember { mutableStateOf(true) }

    when (selectedType) {
        "Major" -> {
            chord = createMajorChordStr(musicNotes[chords.indexOf(selectedChord)])
            chordNotes = createMajorChord(musicNotes[chords.indexOf(selectedChord)])
        }

        "Minor" -> {
            chord = createMinorChordStr(musicNotes[chords.indexOf(selectedChord)])
            chordNotes = createMinorChord(musicNotes[chords.indexOf(selectedChord)])
        }

        "Augmented" -> {
            chord = createAugmentedChordStr(musicNotes[chords.indexOf(selectedChord)])
            chordNotes = createAugmentedChord(musicNotes[chords.indexOf(selectedChord)])
        }

        "Diminished" -> {
            chord = createDiminishedChordStr(musicNotes[chords.indexOf(selectedChord)])
            chordNotes = createDiminishedChord(musicNotes[chords.indexOf(selectedChord)])
        }

        "Major Seventh" -> {
            chord = createMajorSeventhChordStr(musicNotes[chords.indexOf(selectedChord)])
            chordNotes = createMajorSeventhChord(musicNotes[chords.indexOf(selectedChord)])
        }

        "Minor Seventh" -> {
            chord = createMinorSeventhChordStr(musicNotes[chords.indexOf(selectedChord)])
            chordNotes = createMinorSeventhChord(musicNotes[chords.indexOf(selectedChord)])
        }

        else -> {
            chord = ""
            chordNotes = emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (!active) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column {
                ExposedDropdownMenuBox(
                    expanded = expandTypes,
                    onExpandedChange = { expandTypes = it }
                ) {
                    OutlinedTextField(
                        value = selectedType,
                        onValueChange = {},
                        readOnly = true,
                        enabled = active,
                        label = { Text(text = "Type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandTypes)
                        },
                        modifier = Modifier
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandTypes,
                        onDismissRequest = { expandTypes = false }
                    ) {
                        types.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it) },
                                onClick = {
                                    selectedType = it
                                    expandTypes = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expandChord,
                    onExpandedChange = { expandChord = it }
                ) {
                    OutlinedTextField(
                        value = selectedChord,
                        onValueChange = {},
                        readOnly = true,
                        enabled = active,
                        label = { Text(text = "Chord") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandChord)
                        },
                        modifier = Modifier
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandChord,
                        onDismissRequest = { expandChord = false }
                    ) {
                        chords.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it) },
                                onClick = {
                                    selectedChord = it
                                    expandChord = false
                                }
                            )
                        }
                    }
                }
            }

            if (chordNotes.isEmpty()) {
                Text(
                    text = "Not Available",
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(top = 64.dp)
                )

                return
            }

            Row(
                modifier = Modifier
                    .horizontalScroll(chordNotesHScrollState)
            ) {
                val notesNames = chord.split("-")

                for (index in notesNames.indices) {
                    val noteName = notesNames[index]
                    val lNote = chordNotes[index]

                    Text(
                        text = noteName,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .padding(top = 64.dp)
                            .clickable(enabled = active) {
                                active = false

                                scope.launch(Dispatchers.IO) {
                                    when (val result = createNote(lNote, context)) {
                                        is Right -> launch(Dispatchers.Main) {
                                            playAudio(result.value, context)
                                            active = true
                                        }

                                        is Left -> launch(Dispatchers.Main) {
                                            active = true
                                        }
                                    }
                                }
                            }
                    )

                    if (index + 1 < chordNotes.size) {
                        Text(
                            text = " - ",
                            fontSize = 32.sp,
                            modifier = Modifier
                                .padding(top = 64.dp)
                        )
                    }
                }
            }

            TextButton(
                enabled = active,
                onClick = {
                    active = false

                    scope.launch(Dispatchers.IO) {
                        when (val result = buildChord(selectedType, selectedChord, context)) {
                            is Right -> {
                                launch(Dispatchers.Main) {
                                    playAudio(result.value, context)
                                    active = true
                                }
                            }

                            is Left -> {
                                showMessage("Failed to reproduce note")
                            }
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play chord"
                )

                Text(text = "Reproduce Chord")
            }
        }
    }
}