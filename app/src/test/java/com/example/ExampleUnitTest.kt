package com.example

import com.example.model.SceneProject
import com.example.model.ShotItem
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testShotItem_initialStateNotCompleted() {
    val shot = ShotItem(
        id = "s1",
        title = "Wide Shot",
        shotType = "Wide",
        durationSeconds = 8,
        description = "Establishing shot",
        movementInstruction = "Pan left slowly",
        isCompleted = false
    )
    assertFalse(shot.isCompleted)
    assertEquals(8, shot.durationSeconds)
  }

  @Test
  fun testSceneProgressCalculation() {
    val shots = listOf(
        ShotItem("1", "S1", "Wide", 5, "", "", isCompleted = true),
        ShotItem("2", "S2", "Medium", 5, "", "", isCompleted = false),
        ShotItem("3", "S3", "Close-up", 5, "", "", isCompleted = true)
    )
    val completedCount = shots.count { it.isCompleted }
    val progressPercent = (completedCount.toFloat() / shots.size * 100).toInt()
    assertEquals(2, completedCount)
    assertEquals(66, progressPercent)
  }
}
