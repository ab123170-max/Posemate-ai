package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
  val infiniteTransition = rememberInfiniteTransition(label = "splash")
  val scale by infiniteTransition.animateFloat(
      initialValue = 0.9f,
      targetValue = 1.1f,
      animationSpec = infiniteRepeatable(
          animation = tween(1000, easing = FastOutSlowInEasing),
          repeatMode = RepeatMode.Reverse
      ),
      label = "pulse"
  )

  LaunchedEffect(Unit) {
    delay(2000)
    onTimeout()
  }

  Box(
      modifier = Modifier
          .fillMaxSize()
          .background(DarkBackground),
      contentAlignment = Alignment.Center
  ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      Box(
          modifier = Modifier
              .size(100.dp)
              .scale(scale)
              .clip(CircleShape)
              .background(Brush.linearGradient(listOf(PrimaryNeon, SecondaryViolet))),
          contentAlignment = Alignment.Center
      ) {
        Icon(
            imageVector = Icons.Default.VideoCameraFront,
            contentDescription = null,
            tint = DarkBackground,
            modifier = Modifier.size(52.dp)
        )
      }
      Text(
          text = "Cameraman AI",
          style = MaterialTheme.typography.headlineLarge,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
      )
      Text(
          text = "Your AI Cinematic Film Director",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary
      )
    }
  }
}
