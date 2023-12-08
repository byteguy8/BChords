package com.byteguy8.bchords

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.byteguy8.bchords.composables.ChordScreen
import com.byteguy8.bchords.composables.MAIN_NOTE_NAME
import com.byteguy8.bchords.ui.theme.BChordsTheme
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BChordsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

var globalPlayer: MediaPlayer? = null

fun playAudio(path: String, context: Context) {
    val player = globalPlayer

    if (player != null) {
        if (player.isPlaying) {
            player.stop()
        }

        player.release()
        globalPlayer = null
    }

    val audioPlayer = (MediaPlayer.create(context, Uri.fromFile(File(path))) ?: return).also {
        globalPlayer = it
    }

    audioPlayer.start()
}

fun initialSetup(context: Context) {
    val assets = context.assets
    val appDir = context.filesDir

    val c3NoteFile = File(appDir, MAIN_NOTE_NAME)

    if (c3NoteFile.exists()) {
        return
    }

    val buffer = ByteArray(1024)

    var output: OutputStream? = null
    var input: InputStream? = null

    try {
        output = c3NoteFile.outputStream()
        input = assets.open("Samples_C3v16.wav")

        while (true) {
            val read = input.read(buffer, 0, buffer.size)

            if (read == -1) {
                break
            }

            output.write(buffer, 0, read)
        }
    } finally {
        output?.flush()
        output?.close()
        input?.close()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    initialSetup(LocalContext.current)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val showMessage: ShowMessage = {
        scope.launch {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "BChords") })
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        ChordScreen(
            showMessage,
            modifier = Modifier
                .padding(paddingValues)
        )
    }
}

typealias ShowMessage = (msg: String) -> Unit

