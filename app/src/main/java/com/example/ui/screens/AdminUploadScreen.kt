package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.ui.components.MediaCard
import com.example.ui.theme.BadgeHd
import com.example.ui.theme.BadgeMusic
import com.example.ui.theme.BadgeVideo
import com.example.ui.theme.VeatCoral
import com.example.ui.theme.VeatOrange
import com.example.ui.theme.VeatRed
import java.util.UUID

@Composable
fun AdminUploadScreen(
    onUploadSuccess: (MediaItem) -> Unit,
    onNavigateToHome: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var creator by remember { mutableStateOf("") }
    var mediaType by remember { mutableStateOf(MediaType.VIDEO) }
    var category by remember { mutableStateOf("Trending") }
    var quality by remember { mutableStateOf("1080p FHD") }
    var duration by remember { mutableStateOf("03:45") }
    var streamUrl by remember {
        mutableStateOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4")
    }
    var thumbnailUrl by remember {
        mutableStateOf("https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?w=800&q=80")
    }
    var description by remember { mutableStateOf("") }
    var tagsInput by remember { mutableStateOf("4K, Cinematic, Showcase") }
    var uploadStatusMessage by remember { mutableStateOf<String?>(null) }

    val presetArtworks = listOf(
        "Cinema" to "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800&q=80",
        "Concert" to "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&q=80",
        "Cyber City" to "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&q=80",
        "Nature" to "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=800&q=80"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 96.dp)
    ) {
        // Admin Studio Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(VeatRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Admin Media Studio",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = VeatRed
                            ) {
                                Text(
                                    text = "ADMIN",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Upload and publish videos and songs to the live Veat catalog",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Media Type Selector (Video or Music)
        item {
            Text(
                text = "Select Media Type",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = mediaType == MediaType.VIDEO,
                    onClick = {
                        mediaType = MediaType.VIDEO
                        quality = "1080p FHD"
                        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Video")
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BadgeVideo,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_select_video")
                )

                FilterChip(
                    selected = mediaType == MediaType.MUSIC,
                    onClick = {
                        mediaType = MediaType.MUSIC
                        quality = "320 kbps MP3"
                        streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Headphones, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Music / Song")
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BadgeMusic,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_select_music")
                )
            }
        }

        item { Spacer(modifier = Modifier.height(14.dp)) }

        // Form Fields
        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title *") },
                placeholder = { Text(if (mediaType == MediaType.VIDEO) "e.g., Cyberpunk 2077 Night City 4K" else "e.g., Summer Chillout - Electronic Beat") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_title_field")
            )
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            OutlinedTextField(
                value = creator,
                onValueChange = { creator = it },
                label = { Text("Artist / Channel Creator *") },
                placeholder = { Text("e.g., Neon Horizon Productions") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_creator_field")
            )
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Quality and Duration Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = quality,
                    onValueChange = { quality = it },
                    label = { Text("Quality") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration (MM:SS)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Category Selection
        item {
            Text(
                text = "Category",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            val categories = listOf("Trending", "Videos", "Music", "Shorts")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VeatRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Direct Stream URL
        item {
            OutlinedTextField(
                value = streamUrl,
                onValueChange = { streamUrl = it },
                label = { Text("Media Stream / CDN URL *") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_stream_url")
            )
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Artwork URL + Quick Presets
        item {
            OutlinedTextField(
                value = thumbnailUrl,
                onValueChange = { thumbnailUrl = it },
                label = { Text("Thumbnail / Cover Image URL") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preset High-Res Covers:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presetArtworks) { (name, url) ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable { thumbnailUrl = url }
                            .then(
                                if (thumbnailUrl == url) Modifier.border(2.dp, VeatRed, RoundedCornerShape(8.dp)) else Modifier
                            )
                    ) {
                        Text(
                            text = name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Tags Input
        item {
            OutlinedTextField(
                value = tagsInput,
                onValueChange = { tagsInput = it },
                label = { Text("Tags (comma separated)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item { Spacer(modifier = Modifier.height(14.dp)) }

        // Success / Status Feedback
        if (uploadStatusMessage != null) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF10B981).copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uploadStatusMessage ?: "",
                            color = Color(0xFF10B981),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Publish Button
        item {
            Button(
                onClick = {
                    val finalTitle = title.ifBlank { if (mediaType == MediaType.VIDEO) "Cosmic Odyssey 4K" else "Echoes of Eternity" }
                    val finalCreator = creator.ifBlank { "Veat Studio Creators" }

                    val newItem = MediaItem(
                        id = "user_" + UUID.randomUUID().toString().take(8),
                        title = finalTitle,
                        creator = finalCreator,
                        type = mediaType,
                        category = category,
                        duration = duration.ifBlank { "03:45" },
                        quality = quality,
                        fileSizeBytes = if (mediaType == MediaType.VIDEO) 42_000_000L else 9_500_000L,
                        thumbnailUrl = thumbnailUrl,
                        streamUrl = streamUrl,
                        viewsCount = "1 view",
                        tags = tagsInput.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                        uploadDate = "Just now",
                        description = description.ifBlank { "Published via Veat Download Admin Studio." }
                    )

                    onUploadSuccess(newItem)
                    uploadStatusMessage = "Successfully uploaded \"$finalTitle\" to the catalog!"
                    title = ""
                    creator = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("admin_publish_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VeatRed)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Publish to Live Catalog",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // Quick Preset Pack Adder
        item {
            OutlinedButton(
                onClick = {
                    val quickVideo = MediaItem(
                        id = "quick_v_" + UUID.randomUUID().toString().take(6),
                        title = "For Bigger Escapes - 4K Mountain Ride",
                        creator = "Cinematic Studio",
                        type = MediaType.VIDEO,
                        category = "Videos",
                        duration = "00:15",
                        quality = "1080p FHD",
                        fileSizeBytes = 5_800_000L,
                        thumbnailUrl = "https://images.unsplash.com/photo-1502680390469-be75c86b636f?w=800&q=80",
                        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                        viewsCount = "1.2M views",
                        tags = listOf("Action", "4K", "Biking"),
                        uploadDate = "Today"
                    )
                    onUploadSuccess(quickVideo)
                    uploadStatusMessage = "Quick Media Pack item added to feed!"
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = VeatCoral)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Quick Trending Video to Feed", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
