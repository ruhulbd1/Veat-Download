package com.example.model

data class FormatOption(
    val id: String,
    val resolutionOrBitrate: String,
    val format: String, // MP4, MP3, M4A, WEBM
    val type: MediaType,
    val approximateSizeBytes: Long,
    val isRecommended: Boolean = false
) {
    val formattedSize: String
        get() {
            val mb = approximateSizeBytes.toDouble() / (1024 * 1024)
            return String.format("%.1f MB", mb)
        }
}

object FormatPresets {
    fun getOptionsForMedia(media: MediaItem): List<FormatOption> {
        val baseSize = media.fileSizeBytes
        return if (media.type == MediaType.VIDEO) {
            listOf(
                FormatOption("v_1080", "1080p Full HD", "MP4", MediaType.VIDEO, baseSize, isRecommended = true),
                FormatOption("v_720", "720p HD", "MP4", MediaType.VIDEO, (baseSize * 0.65).toLong()),
                FormatOption("v_480", "480p SD", "MP4", MediaType.VIDEO, (baseSize * 0.35).toLong()),
                FormatOption("v_360", "360p Fast", "MP4", MediaType.VIDEO, (baseSize * 0.20).toLong()),
                FormatOption("a_320", "320 kbps (Audio)", "MP3", MediaType.MUSIC, 8_500_000L),
                FormatOption("a_128", "128 kbps (Audio)", "MP3", MediaType.MUSIC, 3_800_000L)
            )
        } else {
            listOf(
                FormatOption("a_320", "320 kbps High Quality", "MP3", MediaType.MUSIC, baseSize, isRecommended = true),
                FormatOption("a_192", "192 kbps Standard", "MP3", MediaType.MUSIC, (baseSize * 0.65).toLong()),
                FormatOption("a_128", "128 kbps Compact", "MP3", MediaType.MUSIC, (baseSize * 0.45).toLong()),
                FormatOption("a_m4a", "M4A Apple Lossless", "M4A", MediaType.MUSIC, (baseSize * 0.85).toLong())
            )
        }
    }
}
