package com.example.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.CameramanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPreviewScreen(
    videoPath: String,
    viewModel: CameramanViewModel,
    onSaveToProject: () -> Unit,
    onRetake: () -> Unit
) {
  val videoUri = remember(videoPath) { Uri.decode(videoPath) }

  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = "Recorded Shot Preview",
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
              )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
        )
      },
      containerColor = DarkBackground
  ) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Video Preview Container using Android VideoView
      Card(
          modifier = Modifier
              .weight(1f)
              .fillMaxWidth(),
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface)
      ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
          AndroidView(
              factory = { ctx ->
                android.widget.VideoView(ctx).apply {
                  setVideoURI(Uri.parse(videoUri))
                  setOnPreparedListener { mp ->
                    mp.isLooping = true
                    start()
                  }
                }
              },
              modifier = Modifier
                  .fillMaxSize()
                  .clip(RoundedCornerShape(16.dp))
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Action Buttons
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        OutlinedButton(
            onClick = onRetake,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .testTag("retake_video_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
        ) {
          Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Retake Shot", fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = onSaveToProject,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .testTag("save_video_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon)
        ) {
          Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = DarkBackground)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
              text = "Save to Project",
              color = DarkBackground,
              fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
