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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = "Settings & Privacy",
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Privacy Card
      Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface)
      ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PrimaryNeon.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = PrimaryNeon)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "100% On-Device Privacy",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimary
            )
          }
          Text(
              text = "All video recording, shot planning, and camera direction occur locally on your device. We never upload your video clips or personal footage to any cloud server.",
              fontSize = 13.sp,
              color = TextSecondary,
              lineHeight = 20.sp
          )
        }
      }

      // Quality Settings Card
      Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = CardSurface)
      ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SecondaryViolet.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.HighQuality, contentDescription = null, tint = PrimaryNeon)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Video Quality & Camera",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimary
            )
          }
          Text(
              text = "• Resolution: HD 1080p (60fps supported where available)\n• Audio: High quality AAC stereo recording\n• Stabilization: CameraX optical & digital stabilization enabled",
              fontSize = 13.sp,
              color = TextSecondary,
              lineHeight = 22.sp
          )
        }
      }

      // About Card
      Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = CardSurface)
      ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
              text = "Cameraman AI v1.0",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = TextPrimary
          )
          Text(
              text = "Professional AI video director and camera assistant.",
              fontSize = 13.sp,
              color = TextSecondary
          )
        }
      }
    }
  }
}
