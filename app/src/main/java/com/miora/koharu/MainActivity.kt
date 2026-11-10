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
import com.miora.koharu.native.KoharuNative
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
    var workspaceTitle by remember { mutableStateOf("Koharu Core: ${KoharuNative.getCoreVersion()}") }
    var pages by remember { mutableStateOf(listOf<MangaPageItem>()) }

    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            workspaceTitle = "Folder: ${it.lastPathSegment ?: it.path}"
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

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            workspaceTitle = "Archive: ${it.lastPathSegment ?: it.path}"
            pages = (0..7).map { index ->
                MangaPageItem(
                    index = index,
                    uri = it,
                    name = "Archive_Page_${index + 1}.jpg",
                    isProcessed = index == 0
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(workspaceTitle, style = MaterialTheme.typography.titleMedium) }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { folderPickerLauncher.launch(null) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Open Folder")
                    }
                    Button(
                        onClick = { 
                            filePickerLauncher.launch(arrayOf("application/x-cbz", "image/*", "application/zip")) 
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Open CBZ")
                    }
                }
            }
        }
    ) { innerPadding ->
        PageGridScreen(
            pages = pages,
            onPageClick = { selectedPage ->
                // Page click handler
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}
