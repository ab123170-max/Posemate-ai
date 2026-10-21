package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SavedPhoto
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.PoseViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPhotosScreen(
    viewModel: PoseViewModel,
    onBack: () -> Unit
) {
  val savedPhotos by viewModel.savedPhotos.collectAsState()

  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = "Saved Practice Photos",
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
    if (savedPhotos.isEmpty()) {
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
              imageVector = Icons.Default.PhotoLibrary,
              contentDescription = null,
              tint = PrimaryNeon,
              modifier = Modifier.size(64.dp)
          )
          Text(
              text = "No Saved Practice Photos Yet",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
          )
          Text(
              text = "Complete a live practice session and save your best photos to review your progress here.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextSecondary,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    } else {
      LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          modifier = Modifier
              .fillMaxSize()
              .padding(padding)
              .padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(savedPhotos) { photo ->
          SavedPhotoCard(photo = photo, onDelete = { viewModel.deletePhoto(photo.id) })
        }
      }
    }
  }
}

@Composable
fun SavedPhotoCard(photo: SavedPhoto, onDelete: () -> Unit) {
  val bitmap = remember(photo.filePath) {
    try {
      val file = File(photo.filePath)
      if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    } catch (e: Exception) {
      null
    }
  }

  Card(
      modifier = Modifier
          .fillMaxWidth()
          .height(220.dp)
          .testTag("saved_photo_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = DarkSurface)
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Saved Photo",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
      } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CardSurface),
            contentAlignment = Alignment.Center
        ) {
          Text(text = "Image missing", color = TextSecondary, fontSize = 12.sp)
        }
      }

      IconButton(
          onClick = onDelete,
          modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(8.dp)
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color.Black.copy(alpha = 0.6f))
              .testTag("delete_photo_button")
      ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
