package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.PoseViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPreviewScreen(
    photoPath: String,
    viewModel: PoseViewModel,
    onSavedSuccessfully: () -> Unit,
    onRetake: () -> Unit
) {
  val context = LocalContext.current
  val bitmap = remember(photoPath) {
    try {
      val file = File(photoPath)
      if (file.exists()) {
        BitmapFactory.decodeFile(file.absolutePath)
      } else null
    } catch (e: Exception) {
      null
    }
  }

  var isSaved by remember { mutableStateOf(false) }

  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = "Practice Result",
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
      // Photo Container
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
          if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured Practice Photo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
          } else {
            Text(
                text = "Failed to load photo",
                color = TextSecondary
            )
          }
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
                .testTag("retake_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
        ) {
          Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Retake", fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = {
              if (bitmap != null && !isSaved) {
                viewModel.savePhoto(bitmap, "Pose Practice") {
                  isSaved = true
                  onSavedSuccessfully()
                }
              }
            },
            enabled = bitmap != null && !isSaved,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .testTag("save_photo_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon)
        ) {
          Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = DarkBackground)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
              text = if (isSaved) "Saved!" else "Save Photo",
              color = DarkBackground,
              fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
