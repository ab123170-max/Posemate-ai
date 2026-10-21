package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PoseRepository
import com.example.model.PoseItem
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoseDetailScreen(
    poseId: String,
    onStartPractice: (String) -> Unit,
    onBack: () -> Unit
) {
  val pose = PoseRepository.poses.find { it.id == poseId } ?: PoseRepository.poses.first()

  Scaffold(
      topBar = {
        TopAppBar(
            title = { Text(text = pose.title, fontWeight = FontWeight.Bold, color = TextPrimary) },
            navigationIcon = {
              IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
              }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
        )
      },
      bottomBar = {
        Surface(
            color = DarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
          Box(modifier = Modifier.padding(20.dp)) {
            Button(
                onClick = { onStartPractice(pose.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("practice_this_pose_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon)
            ) {
              Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = DarkBackground)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                  text = "Practice This Pose",
                  color = DarkBackground,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp
              )
            }
          }
        }
      },
      containerColor = DarkBackground
  ) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Visual Banner card
      Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface)
      ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
              modifier = Modifier
                  .size(80.dp)
                  .clip(CircleShape)
                  .background(PrimaryNeon.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
          ) {
            Icon(
                imageVector = Icons.Default.AccessibilityNew,
                contentDescription = null,
                tint = PrimaryNeon,
                modifier = Modifier.size(40.dp)
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
              text = pose.title,
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Badge(
              containerColor = SecondaryViolet.copy(alpha = 0.2f),
              contentColor = PrimaryNeon
          ) {
            Text(
                text = pose.category,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 12.sp
            )
          }
          Spacer(modifier = Modifier.height(12.dp))
          Text(
              text = pose.description,
              style = MaterialTheme.typography.bodyMedium,
              color = TextSecondary,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }

      // Instructions sections
      InstructionSection(
          title = "Body Positioning",
          content = pose.bodyPositioning,
          icon = Icons.Default.DirectionsWalk
      )
      InstructionSection(
          title = "Hand Positioning",
          content = pose.handPositioning,
          icon = Icons.Default.PanTool
      )
      InstructionSection(
          title = "Head Angle",
          content = pose.headAngle,
          icon = Icons.Default.Face
      )
      InstructionSection(
          title = "Suggested Expression",
          content = pose.suggestedExpression,
          icon = Icons.Default.EmojiEmotions
      )
      InstructionSection(
          title = "Common Mistakes to Avoid",
          content = pose.commonMistakes,
          icon = Icons.Default.Warning,
          isWarning = true
      )
    }
  }
}

@Composable
fun InstructionSection(
    title: String,
    content: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isWarning: Boolean = false
) {
  Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = CardSurface)
  ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
      Box(
          modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(if (isWarning) MaterialTheme.colorScheme.error.copy(alpha = 0.2f) else PrimaryNeon.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
      ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isWarning) MaterialTheme.colorScheme.error else PrimaryNeon,
            modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.width(16.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            fontSize = 13.sp,
            color = TextSecondary,
            lineHeight = 18.sp
        )
      }
    }
  }
}
