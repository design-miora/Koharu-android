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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KoharuHomeScreen()
                }
            }
        }
    }
}

@Composable
fun KoharuHomeScreen() {
    var selectedUriText by remember { mutableStateOf("No file or folder selected") }

    // SAF Directory Picker (Opening a Manga Folder)
    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            selectedUriText = "Selected Folder: ${it.path}"
        }
    }

    // SAF Document Picker (Opening CBZ / Image Files)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            selectedUriText = "Selected File: ${it.path}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Koharu Android",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Hybrid AI Manga Processor & Translation Suite",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Active Workspace",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedUriText,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { folderPickerLauncher.launch(null) }) {
                Text("Open Folder")
            }

            Button(onClick = { 
                filePickerLauncher.launch(arrayOf("application/x-cbz", "image/*", "application/zip")) 
            }) {
                Text("Open CBZ / Image")
            }
        }
    }
}
