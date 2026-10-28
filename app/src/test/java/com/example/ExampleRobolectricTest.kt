package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MediaRepository
import com.example.model.FormatPresets
import com.example.model.MediaType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Veat Download", appName)
  }

  @Test
  fun `media repository loads initial media items`() {
    val repo = MediaRepository.instance
    val items = repo.mediaList.value
    assertTrue("Initial media list should not be empty", items.isNotEmpty())
    val video = items.firstOrNull { it.type == MediaType.VIDEO }
    assertNotNull("Should contain at least one video", video)
    val options = FormatPresets.getOptionsForMedia(video!!)
    assertTrue("Video format options should exist", options.isNotEmpty())
  }
}
