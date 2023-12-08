package com.byteguy8.bchords.utils

sealed class BChordError(val message: String)
class NoteCreationError(message: String) : BChordError(message)
class ChordCreationError(message: String): BChordError(message)