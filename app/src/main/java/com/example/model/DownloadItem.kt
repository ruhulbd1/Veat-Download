package com.example.model

enum class DownloadStatus {
    QUEUED,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED
}

data class DownloadItem(
    val id: String,
    val mediaId: String,
    val title: String,
    val creator: String,
    val type: MediaType,
    val thumbnailUrl: String,
    val streamUrl: String,
    val quality: String,
    val totalBytes: Long,
    val downloadedBytes: Long = 0L,
    val status: DownloadStatus = DownloadStatus.QUEUED,
    val speedBytesPerSec: Long = 0L,
    val progress: Float = 0f,
    val etaSeconds: Int = 0,
    val localFilePath: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    val formattedDownloaded: String
        get() {
            val mb = downloadedBytes.toDouble() / (1024 * 1024)
            return String.format("%.1f MB", mb)
        }

    val formattedTotal: String
        get() {
            val mb = totalBytes.toDouble() / (1024 * 1024)
            return String.format("%.1f MB", mb)
        }

    val formattedSpeed: String
        get() {
            val kb = speedBytesPerSec.toDouble() / 1024
            return if (kb > 1024) {
                String.format("%.1f MB/s", kb / 1024)
            } else {
                String.format("%.0f KB/s", kb)
            }
        }
}
