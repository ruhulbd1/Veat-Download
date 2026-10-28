package com.example.data

import com.example.model.MediaItem
import com.example.model.MediaType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MediaRepository {
    private val _mediaList = MutableStateFlow<List<MediaItem>>(SampleData.initialMediaList)
    val mediaList: StateFlow<List<MediaItem>> = _mediaList.asStateFlow()

    private val _categories = MutableStateFlow(
        listOf("All", "Trending", "Videos", "Music", "Shorts")
    )
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    fun addMedia(mediaItem: MediaItem) {
        _mediaList.update { current ->
            listOf(mediaItem) + current
        }
    }

    fun deleteMedia(id: String) {
        _mediaList.update { current ->
            current.filterNot { it.id == id }
        }
    }

    fun resetDefaults() {
        _mediaList.value = SampleData.initialMediaList
    }

    fun getById(id: String): MediaItem? {
        return _mediaList.value.find { it.id == id }
    }

    companion object {
        val instance by lazy { MediaRepository() }
    }
}
