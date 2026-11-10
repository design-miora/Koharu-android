package com.miora.koharu

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miora.koharu.ui.MangaPageItem
import com.miora.koharu.ui.PageGridScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KoharuMainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoharuMainScreen() {
    var workspaceTitle by remember { mutableStateOf("No active workspace") }
    var pages by remember { mutableStateOf(listOf<MangaPageItem>()) }

    // SAF Directory Picker (Opening a Manga Folder)
    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            workspaceTitle = "Folder: ${it.lastPathSegment ?: it.path}"
            // Generate mock pages for testing the grid view
            pages = (0..11).map { index ->
                MangaPageItem(
                    index = index,
                    uri = it,
                    name = "Page_${index + 1}.jpg",
                    isProcessed = index % 3 == 0
                )
            }
        }
    }

    // SAF Document Picker (Opening CBZ / Image Files)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            workspaceTitle = "Archive: ${it.lastPathFantastic work! Your memory-conscious `PageGridScreen.kt` component is successfully committed, laying down a solid foundation for handling multi-page manga chapters without hitting mobile RAM bottlenecks.

Since you've completed setting up the core navigation, document picker, FFI skeleton, and grid viewer, you are in a great position to wire everything together in `MainActivity.kt` or begin exploring local model integration. 

Let me know whenever you're ready to tackle the next phase, and we'll keep moving forward step by step!
