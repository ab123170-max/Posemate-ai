package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun PoseLibraryScreen(
    onSelectPose: (String) -> Unit,
    onBack: () -> Unit
) {
  var selectedCategory by remember { mutableStateOf("All") }
  val categories = listOf("All", "Portrait", "Full Body", "Casual", "Confident")

  val filteredPoses = remember(selectedCategory) {
    if (selectedCategory == "All") {
      PoseRepository.poses
    } else {
      PoseRepository.poses.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }
  }

  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = "Pose Library",
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
              )
            },
            navigationIcon = {
              IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
              }
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
            .padding(horizontal = 20.dp)
    ) {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
          text = "Select a pose to view step-by-step instructions and practice with live AI camera feedback.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary
      )
      Spacer(modifier = Modifier.height(16.dp))

      // Category filter chips
      LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
      ) {
        items(categories) { category ->
          FilterChip(
              selected = selectedCategory == category,
              onClick = { selectedCategory = category },
              label = { Text(category) },
              colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PrimaryNeon,
                  selectedLabelColor = DarkBackground,
                  containerColor = CardSurface,
                  labelColor = TextPrimary
              ),
              modifier = Modifier.testTag("chip_$category")
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // List of Poses
      LazyColumn(
          verticalArrangement = Arrangement.spacedBy(16.dp),
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        items(filteredPoses) { pose ->
          PoseCard(pose = pose, onClick = { onSelectPose(pose.id) })
        }
      }
    }
  }
}

@Composable
fun PoseCard(pose: PoseItem, onClick: () -> Unit) {
  Card(
      modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onClick)
          .testTag("pose_card_${pose.id}"),
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
              .size(64.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(PrimaryNeon.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
      ) {
        Icon(
            imageVector = Icons.Default.AccessibilityNew,
            contentDescription = null,
            tint = PrimaryNeon,
            modifier = Modifier.size(32.dp)
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
              text = pose.title,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = TextPrimary
          )
          Badge(
              containerColor = SecondaryViolet.copy(alpha = 0.2f),
              contentColor = PrimaryNeon
          ) {
            Text(
                text = pose.category,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                fontSize = 11.sp
            )
          }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = pose.description,
            fontSize = 13.sp,
            color = TextSecondary,
            maxLines = 2
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
