package com.example.model

data class ShotItem(
    val id: String,
    val title: String,
    val shotType: String, // "Wide", "Medium", "Close-up", "Detail", "B-Roll"
    val durationSeconds: Int,
    val description: String,
    val movementInstruction: String,
    var isCompleted: Boolean = false
)

data class SceneProject(
    val id: String,
    val title: String,
    val sceneType: String,
    val shots: List<ShotItem>,
    val timestamp: Long = System.currentTimeMillis()
)

data class RecordedClip(
    val id: String,
    val filePath: String,
    val durationSeconds: Int,
    val shotTitle: String,
    val timestamp: Long = System.currentTimeMillis()
)
