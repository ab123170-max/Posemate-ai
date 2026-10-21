package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CameramanRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun readStringFromContext_returnsCameramanAI() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Cameraman AI", appName)
  }

  @Test
  fun testCameramanRepository_containsDefaultScenes() {
    val scenes = CameramanRepository.defaultScenes
    assertTrue(scenes.isNotEmpty())
    val interviewScene = scenes.find { it.id == "cinematic_interview" }
    assertNotNull(interviewScene)
    assertEquals(4, interviewScene?.shots?.size)
  }

  @Test
  fun testShotTypes_containsEssentialCategories() {
    val allShots = CameramanRepository.defaultScenes.flatMap { it.shots }
    val shotTypes = allShots.map { it.shotType }.toSet()
    assertTrue(shotTypes.contains("Wide"))
    assertTrue(shotTypes.contains("Medium"))
    assertTrue(shotTypes.contains("Close-up"))
  }
}
