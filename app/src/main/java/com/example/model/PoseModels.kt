package com.example.model

data class PoseItem(
    val id: String,
    val title: String,
    val category: String, // "Full Body", "Portrait", "Casual", "Confident"
    val description: String,
    val bodyPositioning: String,
    val handPositioning: String,
    val headAngle: String,
    val suggestedExpression: String,
    val commonMistakes: String,
    val iconName: String = "ic_pose"
)

data class ExpressionItem(
    val id: String,
    val title: String,
    val description: String,
    val eyes: String,
    val eyebrows: String,
    val lips: String,
    val jaw: String,
    val head: String
)

data class ConfidenceStep(
    val stepNumber: Int,
    val title: String,
    val instruction: String,
    val tip: String
)

data class SavedPhoto(
    val id: String,
    val filePath: String,
    val poseTitle: String,
    val timestamp: Long
)
