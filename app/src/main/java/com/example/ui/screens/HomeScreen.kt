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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SceneProject
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
fun HomeScreen(
    viewModel: CameramanViewModel,
    onStartShooting: (String) -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
  val scenes by viewModel.scenes.collectAsState()

  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(PrimaryNeon, SecondaryViolet))),
                    contentAlignment = Alignment.Center
                ) {
                  Icon(
                      imageVector = Icons.Default.Videocam,
                      contentDescription = null,
                      tint = DarkBackground,
                      modifier = Modifier.size(20.dp)
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Cameraman AI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
              }
            },
            actions = {
              IconButton(
                  onClick = onNavigateToProjects,
                  modifier = Modifier.testTag("projects_button")
              ) {
                Icon(imageVector = Icons.Default.Movie, contentDescription = "Projects", tint = PrimaryNeon)
              }
              IconButton(
                  onClick = onNavigateToSettings,
                  modifier = Modifier.testTag("settings_button")
              ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = TextSecondary)
              }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
        )
      },
      containerColor = DarkBackground
  ) { padding ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
      // Hero Banner
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
          Box(
              modifier = Modifier
                  .fillMaxWidth()
                  .background(Brush.linearGradient(listOf(DarkSurface, CardSurface)))
                  .padding(24.dp)
          ) {
            Column {
              Badge(
                  containerColor = SecondaryViolet.copy(alpha = 0.2f),
                  contentColor = PrimaryNeon
              ) {
                Text(
                    text = "AI Film Director",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                  text = "Direct cinematic shots like a professional.",
                  style = MaterialTheme.typography.headlineMedium,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary,
                  lineHeight = 32.sp
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                  text = "Choose a scene template to generate AI shot lists, camera movement directions, and record professional video.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = TextSecondary
              )
              Spacer(modifier = Modifier.height(24.dp))
              Button(
                  onClick = {
                    val defaultScene = scenes.firstOrNull()
                    if (defaultScene != null) {
                      onStartShooting(defaultScene.id)
                    }
                  },
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(56.dp)
                      .testTag("start_shooting_button"),
                  shape = RoundedCornerShape(16.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon)
              ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = DarkBackground)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Start Quick Shoot",
                    color = DarkBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
              }
            }
          }
        }
      }

      // Section Header
      item {
        Text(
            text = "Scene Presets & Shot Lists",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
      }

      // Scene List
      items(scenes) { scene ->
        ScenePresetCard(scene = scene, onClick = { onStartShooting(scene.id) })
      }
    }
  }
}

@Composable
fun ScenePresetCard(scene: SceneProject, onClick: () -> Unit) {
  Card(
      modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onClick)
          .testTag("scene_card_${scene.id}"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = DarkSurface)
  ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
          modifier = Modifier
              .size(56.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(PrimaryNeon.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
      ) {
        Icon(
            imageVector = Icons.Default.MovieCreation,
            contentDescription = null,
            tint = PrimaryNeon,
            modifier = Modifier.size(28.dp)
        )
      }
      Spacer(modifier = Modifier.width(16.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
          Text(
              text = scene.title,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = TextPrimary
          )
          Badge(
              containerColor = SecondaryViolet.copy(alpha = 0.2f),
              contentColor = PrimaryNeon
          ) {
            Text(
                text = "${scene.shots.size} Shots",
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                fontSize = 11.sp
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = scene.sceneType,
            fontSize = 13.sp,
            color = TextSecondary
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Icon(
          imageVector = Icons.Default.ChevronRight,
          contentDescription = null,
          tint = TextSecondary
      )
    }
  }
}
