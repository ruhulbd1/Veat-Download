package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DownloadManager
import com.example.data.MediaRepository
import com.example.model.DownloadItem
import com.example.model.DownloadStatus
import com.example.model.FormatOption
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.player.VeatPlayer
import com.example.ui.components.DownloadOptionDialog
import com.example.ui.components.FullMediaPlayerDialog
import com.example.ui.components.MiniPlayerBar
import com.example.ui.components.VeatTopBar
import com.example.ui.screens.AdminUploadScreen
import com.example.ui.screens.DownloadManagerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.VeatCoral
import com.example.ui.theme.VeatRed
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    HOME,
    SEARCH,
    DOWNLOADS,
    ADMIN_UPLOAD
}

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun VeatMainApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val mediaRepository = remember { MediaRepository.instance }
    val downloadManager = remember { DownloadManager.getInstance(context) }
    val veatPlayer = remember { VeatPlayer.getInstance(context) }

    val mediaList by mediaRepository.mediaList.collectAsState()
    val categories by mediaRepository.categories.collectAsState()
    val downloads by downloadManager.downloads.collectAsState()
    val playerState by veatPlayer.playerState.collectAsState()

    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    var selectedMediaForDownload by remember { mutableStateOf<MediaItem?>(null) }

    val activeDownloadCount = remember(downloads) {
        downloads.count { it.status == DownloadStatus.DOWNLOADING || it.status == DownloadStatus.QUEUED }
    }

    // Handle Android system Back button: return to Home screen if on secondary screen
    BackHandler(enabled = currentScreen != AppScreen.HOME || playerState.isFullPlayerVisible) {
        if (playerState.isFullPlayerVisible) {
            veatPlayer.closeFullPlayer()
        } else {
            currentScreen = AppScreen.HOME
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            VeatTopBar(
                activeDownloadCount = activeDownloadCount,
                onSearchClick = { currentScreen = AppScreen.SEARCH },
                onDownloadsClick = { currentScreen = AppScreen.DOWNLOADS },
                onUploadClick = { currentScreen = AppScreen.ADMIN_UPLOAD }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                // Mini Player Bar (shown whenever player has an active audio/video loaded)
                AnimatedVisibility(
                    visible = playerState.isMiniPlayerVisible && playerState.currentItem != null,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    MiniPlayerBar(
                        playerState = playerState,
                        onTogglePlayPause = { veatPlayer.togglePlayPause() },
                        onExpand = { veatPlayer.openFullPlayer() },
                        onClose = { veatPlayer.closePlayer() }
                    )
                }

                // Standard Navigation Bar
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.HOME,
                        onClick = { currentScreen = AppScreen.HOME },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VeatRed,
                            selectedTextColor = VeatRed,
                            indicatorColor = VeatRed.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.SEARCH,
                        onClick = { currentScreen = AppScreen.SEARCH },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                                contentDescription = "Search"
                            )
                        },
                        label = { Text("Search", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VeatRed,
                            selectedTextColor = VeatRed,
                            indicatorColor = VeatRed.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_search")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.DOWNLOADS,
                        onClick = { currentScreen = AppScreen.DOWNLOADS },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (activeDownloadCount > 0) {
                                        Badge(containerColor = VeatRed, contentColor = Color.White) {
                                            Text("$activeDownloadCount")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentScreen == AppScreen.DOWNLOADS) Icons.Filled.Download else Icons.Outlined.Download,
                                    contentDescription = "Downloads"
                                )
                            }
                        },
                        label = { Text("Downloads", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VeatRed,
                            selectedTextColor = VeatRed,
                            indicatorColor = VeatRed.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_downloads")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.ADMIN_UPLOAD,
                        onClick = { currentScreen = AppScreen.ADMIN_UPLOAD },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.ADMIN_UPLOAD) Icons.Filled.CloudUpload else Icons.Outlined.CloudUpload,
                                contentDescription = "Admin Upload"
                            )
                        },
                        label = { Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VeatRed,
                            selectedTextColor = VeatRed,
                            indicatorColor = VeatRed.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_admin")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        mediaList = mediaList,
                        categories = categories,
                        onMediaClick = { media ->
                            veatPlayer.playMedia(media, openFullScreen = false)
                        },
                        onDownloadClick = { media ->
                            selectedMediaForDownload = media
                        },
                        onNavigateToSearch = {
                            currentScreen = AppScreen.SEARCH
                        },
                        onDirectUrlDownload = { url ->
                            // Create direct stream media item and enqueue
                            val directItem = MediaItem(
                                id = "direct_" + UUID.randomUUID().toString().take(6),
                                title = "Direct Download (" + url.substringAfterLast("/").take(24) + ")",
                                creator = "Direct Stream Portal",
                                type = if (url.contains(".mp3") || url.contains("audio")) MediaType.MUSIC else MediaType.VIDEO,
                                category = "Downloads",
                                duration = "--:--",
                                quality = "Original Direct Quality",
                                fileSizeBytes = 28_000_000L,
                                thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&q=80",
                                streamUrl = url,
                                viewsCount = "Direct"
                            )
                            mediaRepository.addMedia(directItem)
                            selectedMediaForDownload = directItem
                        }
                    )
                }

                AppScreen.SEARCH -> {
                    SearchScreen(
                        mediaList = mediaList,
                        onMediaClick = { media ->
                            veatPlayer.playMedia(media, openFullScreen = false)
                        },
                        onDownloadClick = { media ->
                            selectedMediaForDownload = media
                        },
                        onDirectDownload = { url ->
                            val directItem = MediaItem(
                                id = "direct_" + UUID.randomUUID().toString().take(6),
                                title = "Direct Stream (" + url.substringAfterLast("/").take(24) + ")",
                                creator = "Direct Web Downloader",
                                type = if (url.contains(".mp3") || url.contains("audio")) MediaType.MUSIC else MediaType.VIDEO,
                                category = "Downloads",
                                duration = "--:--",
                                quality = "Original Quality",
                                fileSizeBytes = 35_000_000L,
                                thumbnailUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=800&q=80",
                                streamUrl = url,
                                viewsCount = "Direct"
                            )
                            mediaRepository.addMedia(directItem)
                            selectedMediaForDownload = directItem
                        }
                    )
                }

                AppScreen.DOWNLOADS -> {
                    DownloadManagerScreen(
                        downloads = downloads,
                        onPauseDownload = { id -> downloadManager.pauseDownload(id) },
                        onResumeDownload = { id -> downloadManager.resumeDownload(id) },
                        onCancelDownload = { id -> downloadManager.cancelDownload(id) },
                        onDeleteDownloaded = { id -> downloadManager.deleteDownloaded(id) },
                        onPlayDownloaded = { item ->
                            val mediaItem = mediaRepository.getById(item.mediaId) ?: MediaItem(
                                id = item.mediaId,
                                title = item.title,
                                creator = item.creator,
                                type = item.type,
                                category = "Downloaded",
                                duration = "--:--",
                                quality = item.quality,
                                fileSizeBytes = item.totalBytes,
                                thumbnailUrl = item.thumbnailUrl,
                                streamUrl = item.streamUrl,
                                viewsCount = "Downloaded File"
                            )
                            veatPlayer.playMedia(mediaItem, openFullScreen = true)
                        },
                        onPauseAll = { downloadManager.pauseAll() },
                        onResumeAll = { downloadManager.resumeAll() },
                        onNavigateToHome = { currentScreen = AppScreen.HOME }
                    )
                }

                AppScreen.ADMIN_UPLOAD -> {
                    AdminUploadScreen(
                        onUploadSuccess = { newItem ->
                            mediaRepository.addMedia(newItem)
                            scope.launch {
                                snackbarHostState.showSnackbar("Published \"${newItem.title}\" to catalog!")
                            }
                        },
                        onNavigateToHome = { currentScreen = AppScreen.HOME }
                    )
                }
            }
        }
    }

    // Download Option Selection Dialog
    selectedMediaForDownload?.let { media ->
        DownloadOptionDialog(
            media = media,
            onDismiss = { selectedMediaForDownload = null },
            onStartDownload = { format ->
                val dlId = downloadManager.startDownload(media, format)
                scope.launch {
                    snackbarHostState.showSnackbar("Download started: ${media.title} (${format.resolutionOrBitrate})")
                }
                selectedMediaForDownload = null
            }
        )
    }

    // Full Media Player Dialog
    if (playerState.isFullPlayerVisible && playerState.currentItem != null) {
        FullMediaPlayerDialog(
            playerState = playerState,
            onClose = { veatPlayer.closeFullPlayer() },
            onTogglePlayPause = { veatPlayer.togglePlayPause() },
            onSeek = { frac -> veatPlayer.seekTo(frac) },
            onDownloadClick = {
                val cur = playerState.currentItem
                if (cur != null) {
                    veatPlayer.closeFullPlayer()
                    selectedMediaForDownload = cur
                }
            }
        )
    }
}
