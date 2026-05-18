package com.vimla.myapplication.Screen.ImageCompressor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReducedImageSized() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var originalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var compressedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isCompressing by remember { mutableStateOf(false) }

    var originalSize by remember { mutableStateOf<Long?>(null) }
    var compressedSize by remember { mutableStateOf<Long?>(null) }
    var originalDimensions by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var compressedDimensions by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    var showFullScreenDialog by remember { mutableStateOf(false) }
    var fullScreenImageType by remember { mutableStateOf<ImageType?>(null) }

    // Fixed compression settings
    val COMPRESSION_QUALITY = 35
    val MAX_DIMENSIONS = 10000  // Very high to preserve original dimensions

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            compressedBitmap = null
            compressedSize = null

            scope.launch(Dispatchers.IO) {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    originalSize = stream.available().toLong()
                }

                context.contentResolver.openInputStream(it)?.use { stream ->
                    originalBitmap = BitmapFactory.decodeStream(stream)
                    originalDimensions = originalBitmap?.let { bmp ->
                        Pair(bmp.width, bmp.height)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Image Compression Tester",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Fixed at 35% Quality • Original Dimensions Preserved",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Image from Gallery")
        }

        Spacer(modifier = Modifier.height(24.dp))

        selectedImageUri?.let { uri ->

            // Compress Button Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Compression Settings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoChip("Quality: 35%", MaterialTheme.colorScheme.primary)
                        InfoChip("Original Size", MaterialTheme.colorScheme.secondary)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isCompressing) {
                        Button(
                            onClick = {
                                scope.launch {
                                    isCompressing = true
                                    val compressedBytes = withContext(Dispatchers.IO) {
                                        ImageCompressor.compressImage(
                                            context = context,
                                            imageUri = uri,
                                            maxWidth = MAX_DIMENSIONS,
                                            maxHeight = MAX_DIMENSIONS,
                                            quality = COMPRESSION_QUALITY
                                        )
                                    }

                                    compressedBytes?.let { bytes ->
                                        compressedSize = bytes.size.toLong()
                                        compressedBitmap = BitmapFactory.decodeByteArray(
                                            bytes, 0, bytes.size
                                        )
                                        compressedDimensions = compressedBitmap?.let { bmp ->
                                            Pair(bmp.width, bmp.height)
                                        }
                                    }

                                    isCompressing = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Compress Image (35%)")
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            // Statistics Card
            if (compressedSize != null && originalSize != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Compression Results",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        val reduction = ((originalSize!! - compressedSize!!) * 100 / originalSize!!)
                        val compressionRatio = String.format("%.2f", originalSize!!.toFloat() / compressedSize!!.toFloat())

                        StatRow("Original Size:", "${originalSize!! / 1024} KB")
                        StatRow("Compressed Size:", "${compressedSize!! / 1024} KB")
                        StatRow("Size Reduction:", "$reduction%")
                        StatRow("Compression Ratio:", "$compressionRatio:1")

                        Spacer(modifier = Modifier.height(8.dp))

                        originalDimensions?.let { (w, h) ->
                            StatRow("Original Dimensions:", "${w} × ${h} px")
                        }
                        compressedDimensions?.let { (w, h) ->
                            StatRow("Compressed Dimensions:", "${w} × ${h} px")
                        }

                        // Verify dimensions are same
                        if (originalDimensions == compressedDimensions) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = Color.Green.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "✓ Dimensions Preserved",
                                    color = Color(0xFF006400),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Swipeable Image Comparison with HorizontalPager
            if (compressedBitmap != null) {
                Text(
                    text = "Swipe to Compare (Tap to Zoom)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                val pagerState = rememberPagerState(pageCount = { 2 })

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            when (page) {
                                0 -> {
                                    // Original Image
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        originalBitmap?.let { bitmap ->
                                            Image(
                                                bitmap = bitmap.asImageBitmap(),
                                                contentDescription = "Original Image",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clickable {
                                                        fullScreenImageType = ImageType.ORIGINAL
                                                        showFullScreenDialog = true
                                                    },
                                                contentScale = ContentScale.Fit
                                            )
                                        }

                                        // Label
                                        Surface(
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(16.dp),
                                            color = Color.Black.copy(alpha = 0.7f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Original",
                                                    color = Color.White,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "${originalSize?.let { it / 1024 }} KB",
                                                    color = Color.White,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    }
                                }
                                1 -> {
                                    // Compressed Image
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        compressedBitmap?.let { bitmap ->
                                            Image(
                                                bitmap = bitmap.asImageBitmap(),
                                                contentDescription = "Compressed Image",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clickable {
                                                        fullScreenImageType = ImageType.COMPRESSED
                                                        showFullScreenDialog = true
                                                    },
                                                contentScale = ContentScale.Fit
                                            )
                                        }

                                        // Label
                                        Surface(
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(16.dp),
                                            color = Color.Green.copy(alpha = 0.7f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Compressed 35%",
                                                    color = Color.White,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "${compressedSize?.let { it / 1024 }} KB",
                                                    color = Color.White,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Page Indicator
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(2) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (pagerState.currentPage == index)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                Color.Gray.copy(alpha = 0.5f)
                                        )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Swipe left/right to compare images • Tap to view full screen with zoom",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Full Screen Zoomable Image Dialog
    if (showFullScreenDialog && fullScreenImageType != null) {
        FullScreenImageDialog(
            bitmap = if (fullScreenImageType == ImageType.ORIGINAL) originalBitmap else compressedBitmap,
            imageType = fullScreenImageType!!,
            onDismiss = { showFullScreenDialog = false }
        )
    }
}

@Composable
fun InfoChip(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun FullScreenImageDialog(
    bitmap: Bitmap?,
    imageType: ImageType,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Full Screen Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(minScale = 1f, maxScale = 5f),
                    contentScale = ContentScale.Fit
                )
            }

            // Header with title and close button
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                color = Color.Black.copy(alpha = 0.7f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (imageType == ImageType.ORIGINAL) "Original Image" else "Compressed Image (35%)",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            }

            // Instructions
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Pinch to zoom • Double-tap to reset • Drag to pan",
                    color = Color.White,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

enum class ImageType {
    ORIGINAL,
    COMPRESSED
}
