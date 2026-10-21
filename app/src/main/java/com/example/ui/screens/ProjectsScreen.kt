package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RecordedClip
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
fun ProjectsScreen(
    viewModel: CameramanViewModel,
    onBack: () -> Unit
) {
  val clips by viewModel.recordedClips.collectAsState()

  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = "Project Clips Library",
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
      containerColor = DarkBackground
  ) { padding ->
    if (clips.isEmpty()) {
      Box(
          modifier = Modifier
              .fillMaxSize()
              .padding(padding)
              .padding(24.dp),
          contentAlignment = Alignment.Center
      ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Icon(
              imageVector = Icons.Default.Movie,
              contentDescription = null,
              tint = PrimaryNeon,
              modifier = Modifier.size(64.dp)
          )
          Text(
              text = "No Recorded Clips Yet",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
          )
          Text(
              text = "Use the AI Director live camera to record professional video clips and build your project sequence.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextSecondary,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
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
        items(clips) { clip ->
          ClipCard(clip = clip, onDelete = { viewModel.deleteClip(clip.id) })
        }
      }
    }
  }
}

@Composable
fun ClipCard(clip: RecordedClip, onDelete: () -> Unit) {
  Card(
      modifier = Modifier
          .fillMaxWidth()
          .testTag("clip_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = DarkSurface)
  ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
          modifier = Modifier
              .size(56.dp)
              .background(PrimaryNeon.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
          contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = PrimaryNeon)
      }
      Spacer(modifier = Modifier.width(16.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
            text = clip.shotTitle,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Duration: ~${clip.durationSeconds}s",
            fontSize = 13.sp,
            color = TextSecondary
        )
      }
      IconButton(
          onClick = onDelete,
          modifier = Modifier.testTag("delete_clip_button")
      ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.error
        )
      }
    }
  }
}
