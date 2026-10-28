package com.example.model

enum class MediaType {
    VIDEO,
    MUSIC
}

data class MediaItem(
    val id: String,
    val title: String,
    val creator: String,
    val type: MediaType,
    val category: String,
    val duration: String,
    val quality: String,
    val fileSizeBytes: Long,
    val thumbnailUrl: String,
    val streamUrl: String,
    val viewsCount: String,
    val tags: List<String> = emptyList(),
    val uploadDate: String = "Recent",
    val description: String = ""
) {
    val formattedSize: String
        get() {
            val mb = fileSizeBytes.toDouble() / (1024 * 1024)
            return if (mb >= 1024) {
                String.format("%.1f GB", mb / 1024)
            } else {
                String.format("%.1f MB", mb)
            }
        }
}
