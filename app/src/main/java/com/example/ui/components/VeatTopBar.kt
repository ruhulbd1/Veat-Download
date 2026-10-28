package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VeatCoral
import com.example.ui.theme.VeatOrange
import com.example.ui.theme.VeatRed

@Composable
fun VeatTopBar(
    activeDownloadCount: Int = 0,
    onSearchClick: () -> Unit,
    onDownloadsClick: () -> Unit,
    onUploadClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Logo Icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(VeatRed, VeatCoral, VeatOrange)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Veat Logo",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // App Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Veat",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = VeatRed
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "Download",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Search Action
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.testTag("topbar_search_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Media",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Admin Upload Studio Action
            IconButton(
                onClick = onUploadClick,
                modifier = Modifier.testTag("topbar_upload_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = "Upload Media Studio",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Download Manager Shortcut with Badge
            IconButton(
                onClick = onDownloadsClick,
                modifier = Modifier.testTag("topbar_downloads_btn")
            ) {
                BadgedBox(
                    badge = {
                        if (activeDownloadCount > 0) {
                            Badge(
                                containerColor = VeatRed,
                                contentColor = Color.White
                            ) {
                                Text("$activeDownloadCount", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download Manager",
                        tint = if (activeDownloadCount > 0) VeatRed else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
