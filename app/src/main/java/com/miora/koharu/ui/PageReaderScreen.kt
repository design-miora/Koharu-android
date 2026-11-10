package com.miora.koharu.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.miora.koharu.native.KoharuNative

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageReaderScreen(
    page: MangaPageItem,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var processingStatus by remember { mutableStateOf("Ready to process page") }
    var isProcessing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(page.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back to Grid")
                    }
                }
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
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            isProcessing = true
                            processingStatus = "Reading image bytes & calling Rust FFI..."

                            try {
                                // Read actual image bytes from ContentResolver
                                val inputStream = context.contentResolver.openInputStream(page.uri)
                                val imageBytes = inputStream?.readBytes() ?: byteArrayOf()
                                inputStream?.close()

                                if (imageBytes.isNotEmpty()) {
                                    // Send real bytes to Rust backend pipeline
                                    val responseJson = KoharuNative.processPage(imageBytes)
                                    processingStatus = "Rust Response: $responseJson"
                                } else {
                                    processingStatus = "Error: Could not read bytes from URI"
                                }
                            } catch (e: Exception) {
                                processingStatus = "Error: ${e.localizedMessage}"
                            }

                            isProcessing = false
                        },
                        enabled = !isProcessing,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isProcessing) "Processing..." else "Run OCR & Translation")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Page View Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Page ${page.index + 1}\n(URI: ${page.uri.lastPathSegment ?: "Local"})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Processing Feedback Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Pipeline Status",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = processingStatus,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
