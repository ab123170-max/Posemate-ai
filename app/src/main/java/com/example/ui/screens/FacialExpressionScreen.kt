package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.model.ExpressionItem
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacialExpressionScreen(
    onStartPractice: (String) -> Unit,
    onBack: () -> Unit
) {
  var selectedExpression by remember { mutableStateOf<ExpressionItem?>(null) }

  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = "Facial Expression Coach",
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
    if (selectedExpression != null) {
      // Detail view for specific expression practice
      ExpressionDetailView(
          expression = selectedExpression!!,
          onBack = { selectedExpression = null },
          onPractice = { onStartPractice("expression_${selectedExpression!!.id}") }
      )
    } else {
      // List of expressions
      Column(
          modifier = Modifier
              .fillMaxSize()
              .padding(padding)
              .padding(horizontal = 20.dp)
      ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Master your eyes, smile, and jawline for every type of photo.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
          items(PoseRepository.expressions) { expr ->
            ExpressionCard(expression = expr, onClick = { selectedExpression = expr })
          }
        }
      }
    }
  }
}

@Composable
fun ExpressionCard(expression: ExpressionItem, onClick: () -> Unit) {
  Card(
      modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onClick)
          .testTag("expression_card_${expression.id}"),
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
              .clip(CircleShape)
              .background(SecondaryViolet.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
      ) {
        Icon(
            imageVector = Icons.Default.EmojiEmotions,
            contentDescription = null,
            tint = PrimaryNeon,
            modifier = Modifier.size(28.dp)
        )
      }
      Spacer(modifier = Modifier.width(16.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
            text = expression.title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = expression.description,
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

@Composable
fun ExpressionDetailView(
    expression: ExpressionItem,
    onBack: () -> Unit,
    onPractice: () -> Unit
) {
  Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(20.dp)
          .verticalScroll(androidx.compose.foundation.rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
      IconButton(onClick = onBack) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
      }
      Text(
          text = expression.title,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
      )
    }

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
        Text(
            text = "Expression Guide",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = PrimaryNeon
        )
        Text(
            text = expression.description,
            fontSize = 14.sp,
            color = TextSecondary
        )
      }
    }

    ExpressionInstructionRow(title = "Eyes", detail = expression.eyes)
    ExpressionInstructionRow(title = "Eyebrows", detail = expression.eyebrows)
    ExpressionInstructionRow(title = "Lips & Mouth", detail = expression.lips)
    ExpressionInstructionRow(title = "Jaw & Tension", detail = expression.jaw)
    ExpressionInstructionRow(title = "Head Position", detail = expression.head)

    Spacer(modifier = Modifier.height(8.dp))

    Button(
        onClick = onPractice,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag("practice_expression_button"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon)
    ) {
      Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = DarkBackground)
      Spacer(modifier = Modifier.width(8.dp))
      Text(
          text = "Practice with Live Camera",
          color = DarkBackground,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
      )
    }
  }
}

@Composable
fun ExpressionInstructionRow(title: String, detail: String) {
  Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = CardSurface)
  ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
          text = title,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = PrimaryNeon
      )
      Text(
          text = detail,
          fontSize = 13.sp,
          color = TextPrimary
      )
    }
  }
}
