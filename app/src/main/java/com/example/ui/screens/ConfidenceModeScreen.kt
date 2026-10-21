package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfidenceModeScreen(
    onStartLiveCoach: () -> Unit,
    onBack: () -> Unit
) {
  val steps = PoseRepository.confidenceRoutine
  var currentStepIndex by remember { mutableStateOf(0) }
  val step = steps[currentStepIndex]

  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = "Confidence Mode",
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
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Progress bar
        LinearProgressIndicator(
            progress = { (currentStepIndex + 1).toFloat() / steps.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = PrimaryNeon,
            trackColor = CardSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
          Badge(
              containerColor = SecondaryViolet.copy(alpha = 0.2f),
              contentColor = PrimaryNeon
          ) {
            Text(
                text = "Step ${step.stepNumber} of ${steps.size}",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontWeight = FontWeight.Bold
            )
          }
          Text(
              text = "${((currentStepIndex + 1).toFloat() / steps.size * 100).toInt()}% Complete",
              fontSize = 12.sp,
              color = TextSecondary
          )
        }

        // Step Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
          Column(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(PrimaryNeon.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
              Icon(
                  imageVector = Icons.Default.Favorite,
                  contentDescription = null,
                  tint = PrimaryNeon,
                  modifier = Modifier.size(36.dp)
              )
            }

            Text(
                text = step.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Text(
                text = step.instruction,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 24.sp
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                  modifier = Modifier.padding(16.dp),
                  verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = PrimaryNeon
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = step.tip,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.weight(1f),
                    lineHeight = 18.sp
                )
              }
            }
          }
        }
      }

      // Navigation Buttons
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        if (currentStepIndex > 0) {
          OutlinedButton(
              onClick = { currentStepIndex-- },
              modifier = Modifier
                  .weight(1f)
                  .height(56.dp),
              shape = RoundedCornerShape(16.dp)
          ) {
            Text(text = "Previous", color = TextPrimary, fontWeight = FontWeight.Bold)
          }
        }

        Button(
            onClick = {
              if (currentStepIndex < steps.size - 1) {
                currentStepIndex++
              } else {
                onStartLiveCoach()
              }
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .testTag("confidence_next_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon)
        ) {
          Text(
              text = if (currentStepIndex < steps.size - 1) "Next Step" else "Start Live Practice",
              color = DarkBackground,
              fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
