package com.byteguy8.bchords.intervals

fun createMajorChord(from: MusicNote, octave: Int = 3): List<LocalizeNote> {
    val third = MajorThird.forward(from, octave)
    val fifth = FairFifth.forward(from, octave)

    return listOf(LocalizeNote(octave, from), third, fifth)
}

fun createMajorChordStr(from: MusicNote): String {
    val thirdStr = MajorThird.forwardStr(from)
    val fifthStr = FairFifth.forwardStr(from)

    return "${from.sharpName()} - $thirdStr - $fifthStr"
}

fun createMinorChord(from: MusicNote, octave: Int = 3): List<LocalizeNote> {
    val minorThird = MinorThird.forward(from, octave)
    val fifth = FairFifth.forward(from, octave)

    return listOf(LocalizeNote(octave, from), minorThird, fifth)
}

fun createMinorChordStr(from: MusicNote): String {
    val minorThirdStr = MinorThird.forwardStr(from)
    val fifth = FairFifth.forwardStr(from)

    return "${from.sharpName()} - $minorThirdStr - $fifth"
}

fun createAugmentedChord(from: MusicNote, octave: Int = 3): List<LocalizeNote> {
    val third = MajorThird.forward(from, octave)
    val fifth = AugmentedFifth.forward(from, octave)

    return listOf(LocalizeNote(octave, from), third, fifth)
}

fun createAugmentedChordStr(from: MusicNote): String {
    val thirdStr = MajorThird.forwardStr(from)
    val fifthStr = AugmentedFifth.forwardStr(from)

    return "${from.sharpName()} - $thirdStr - $fifthStr"
}

fun createDiminishedChord(from: MusicNote, octave: Int = 3): List<LocalizeNote> {
    val minorThird = MinorThird.forward(from, octave)
    val diminishedFifth = DiminishedFifth.forward(from, octave)

    return listOf(LocalizeNote(octave, from), minorThird, diminishedFifth)
}

fun createDiminishedChordStr(from: MusicNote): String {
    val minorThird = MinorThird.forwardStr(from)
    val diminishedFifth = DiminishedFifth.forwardStr(from)

    return "${from.sharpName()} - $minorThird - $diminishedFifth"
}

fun createMajorSeventhChord(from: MusicNote, octave: Int = 3): List<LocalizeNote> {
    val third = MajorThird.forward(from, octave)
    val fifth = FairFifth.forward(from, octave)
    val seventh = MajorSeventh.forward(from, octave)

    return listOf(LocalizeNote(octave, from), third, fifth, seventh)
}

fun createMajorSeventhChordStr(from: MusicNote): String {
    val third = MajorThird.forwardStr(from)
    val fifth = FairFifth.forwardStr(from)
    val seventh = MajorSeventh.forwardStr(from)

    return "${from.sharpName()} - $third - $fifth - $seventh"
}

fun createMinorSeventhChord(from: MusicNote, octave: Int = 3): List<LocalizeNote> {
    val third = MajorThird.forward(from, octave)
    val fifth = FairFifth.forward(from, octave)
    val seventh = MinorSeventh.forward(from, octave)

    return listOf(LocalizeNote(octave, from), third, fifth, seventh)
}

fun createMinorSeventhChordStr(from: MusicNote): String {
    val third = MajorThird.forwardStr(from)
    val fifth = FairFifth.forwardStr(from)
    val minorSeventh = MinorSeventh.forwardStr(from)

    return "${from.sharpName()} - $third - $fifth - $minorSeventh"
}