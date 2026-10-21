package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ShotItem
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.CameramanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenePlannerScreen(
    sceneId: String,
    viewModel: CameramanViewModel,
    onStartCamera: (String) -> Unit,
    onBack: () -> Unit
) {
  LaunchedEffect(sceneId) {
    viewModel.selectScene(sceneId)
  }

  val currentScene by viewModel.currentScene.collectAsState()

  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = currentScene?.title ?: "Scene Planner",
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
              )
            },
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
                onClick = { onStartCamera(sceneId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("start_camera_directing_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon)
            ) {
              Icon(imageVector = Icons.Default.Videocam, contentDescription = null, tint = DarkBackground)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                  text = "Start Live Camera Directing",
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
    if (currentScene == null) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Scene not found", color = TextSecondary)
      }
    } else {
      LazyColumn(
          modifier = Modifier
              .fillMaxSize()
              .padding(padding)
              .padding(horizontal = 20.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
          contentPadding = PaddingValues(vertical = 16.dp)
      ) {
        item {
          Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = DarkSurface)
          ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                  text = "Capture Pipeline",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = PrimaryNeon
              )
              Text(
                  text = "1. Analyze Scene → 2. Plan Shots → 3. Direct Camera → 4. Record & Review",
                  fontSize = 13.sp,
                  color = TextSecondary
              )
            }
          }
        }

        item {
          Text(
              text = "Shot List (${currentScene!!.shots.count { it.isCompleted }}/${currentScene!!.shots.size} Completed)",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
          )
        }

        items(currentScene!!.shots) { shot ->
          ShotCard(
              shot = shot,
              onToggleComplete = { viewModel.toggleShotCompleted(shot.id) },
              onStartShotCamera = { onStartCamera(sceneId) }
          )
        }
      }
    }
  }
}

@Composable
fun ShotCard(
    shot: ShotItem,
    onToggleComplete: () -> Unit,
    onStartShotCamera: () -> Unit
) {
  Card(
      modifier = Modifier
          .fillMaxWidth()
          .testTag("shot_card_${shot.id}"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = CardSurface)
  ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Checkbox(
              checked = shot.isCompleted,
              onCheckedChange = { onToggleComplete() },
              colors = CheckboxDefaults.colors(checkedColor = PrimaryNeon)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
              text = shot.title,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = if (shot.isCompleted) TextSecondary else TextPrimary
          )
        }
        Badge(
            containerColor = SecondaryViolet.copy(alpha = 0.2f),
            contentColor = PrimaryNeon
        ) {
          Text(
              text = shot.shotType,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              fontSize = 11.sp
          )
        }
      }

      Text(
          text = shot.description,
          fontSize = 13.sp,
          color = TextSecondary
      )

      Surface(
          shape = RoundedCornerShape(12.dp),
          color = DarkSurface,
          modifier = Modifier.fillMaxWidth()
      ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
              imageVector = Icons.Default.Videocam,
              contentDescription = null,
              tint = PrimaryNeon,
              modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
              text = "Movement: ${shot.movementInstruction}",
              fontSize = 12.sp,
              color = TextPrimary,
              modifier = Modifier.weight(1f)
          )
        }
      }
    }
  }
}
