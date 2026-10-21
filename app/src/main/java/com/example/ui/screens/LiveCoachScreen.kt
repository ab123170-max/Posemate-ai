package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.data.PoseRepository
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun LiveCoachScreen(
    poseId: String?,
    onPhotoCaptured: (String) -> Unit,
    onBack: () -> Unit
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  val pose = poseId?.let { id -> PoseRepository.poses.find { it.id == id } }
  val poseTitle = pose?.title ?: "Free Practice"

  var hasCameraPermission by remember {
    mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED
    )
  }

  val permissionLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.RequestPermission()
  ) { granted ->
    hasCameraPermission = granted
  }

  LaunchedEffect(Unit) {
    if (!hasCameraPermission) {
      permissionLauncher.launch(Manifest.permission.CAMERA)
    }
  }

  var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
  val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

  // Real-time coaching feedback cycling for realism and practical guidance
  val feedbackList = remember {
    listOf(
        "Straighten your head slightly.",
        "Relax your shoulders and drop them down.",
        "Turn your body a little sideways.",
        "Keep your back straight and core engaged.",
        "Shift your weight onto your back foot.",
        "Look directly into the camera lens with confidence."
    )
  }
  var currentFeedbackIndex by remember { mutableStateOf(0) }

  LaunchedEffect(Unit) {
    while (true) {
      delay(4000)
      currentFeedbackIndex = (currentFeedbackIndex + 1) % feedbackList.size
    }
  }

  var isCountdownActive by remember { mutableStateOf(false) }
  var countdownSeconds by remember { mutableStateOf(3) }

  LaunchedEffect(isCountdownActive) {
    if (isCountdownActive) {
      countdownSeconds = 3
      while (countdownSeconds > 0) {
        delay(1000)
        countdownSeconds--
      }
      isCountdownActive = false
      // Capture photo
      imageCapture?.let { capture ->
        val photoFile = java.io.File(context.cacheDir, "captured_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
              override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onPhotoCaptured(photoFile.absolutePath)
              }

              override fun onError(exception: ImageCaptureException) {
                Log.e("LiveCoach", "Capture failed: ${exception.message}", exception)
              }
            }
        )
      }
    }
  }

  if (!hasCameraPermission) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
      Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = null,
            tint = PrimaryNeon,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "Camera Permission Required",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "PoseMate AI needs camera access to provide real-time pose coaching and capture your practice photos securely on-device.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Button(
            onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon),
            modifier = Modifier.testTag("grant_permission_button")
        ) {
          Text(text = "Grant Permission", color = DarkBackground, fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = onBack) {
          Text(text = "Go Back", color = TextSecondary)
        }
      }
    }
    return
  }

  Box(
      modifier = Modifier
          .fillMaxSize()
          .background(DarkBackground)
  ) {
    // Camera Preview
    AndroidView(
        factory = { ctx ->
          val previewView = PreviewView(ctx).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
          }
          val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
          cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
              it.setSurfaceProvider(previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
              cameraProvider.unbindAll()
              cameraProvider.bindToLifecycle(
                  lifecycleOwner,
                  cameraSelector,
                  preview,
                  imageCapture
              )
            } catch (exc: Exception) {
              Log.e("LiveCoach", "Use case binding failed", exc)
            }
          }, ContextCompat.getMainExecutor(ctx))
          previewView
        },
        modifier = Modifier.fillMaxSize()
    )

    // Pose Guide Overlay / Skeleton lines simulation & Framing Guide
    Canvas(modifier = Modifier.fillMaxSize()) {
      val width = size.width
      val height = size.height

      // Draw subtle framing guide box in center
      drawRect(
          color = Color.White.copy(alpha = 0.2f),
          topLeft = androidx.compose.ui.geometry.Offset(width * 0.15f, height * 0.1f),
          size = androidx.compose.ui.geometry.Size(width * 0.7f, height * 0.75f),
          style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
      )
    }

    // Top Bar overlay
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
          onClick = onBack,
          modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.5f))
      ) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
      }

      Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color.Black.copy(alpha = 0.6f)
      ) {
        Text(
            text = poseTitle,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
      }

      Box(modifier = Modifier.size(44.dp)) // Spacer
    }

    // Live AI Correction Instruction Banner (Only one primary instruction at a time)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .padding(top = 90.dp)
            .padding(horizontal = 24.dp)
    ) {
      Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          color = PrimaryNeon.copy(alpha = 0.9f)
      ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
              imageVector = Icons.Default.Psychology,
              contentDescription = null,
              tint = DarkBackground
          )
          Spacer(modifier = Modifier.width(12.dp))
          Text(
              text = feedbackList[currentFeedbackIndex],
              color = DarkBackground,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Countdown Overlay
    if (isCountdownActive) {
      Box(
          modifier = Modifier
              .fillMaxSize()
              .background(Color.Black.copy(alpha = 0.6f)),
          contentAlignment = Alignment.Center
      ) {
        Text(
            text = "$countdownSeconds",
            fontSize = 96.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryNeon
        )
      }
    }

    // Bottom Action Bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
      ) {
        // Info button or switch pose
        IconButton(
            onClick = { /* info dialog or toggle */ },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
          Icon(imageVector = Icons.Default.Info, contentDescription = "Tips", tint = Color.White)
        }

        // Capture Shutter Button
        FloatingActionButton(
            onClick = { isCountdownActive = true },
            modifier = Modifier
                .size(76.dp)
                .testTag("shutter_button"),
            shape = CircleShape,
            containerColor = PrimaryNeon
        ) {
          Box(
              modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
                  .background(PrimaryNeon),
              contentAlignment = Alignment.Center
          ) {
            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = "Capture",
                tint = DarkBackground,
                modifier = Modifier.size(36.dp)
            )
          }
        }

        // Flip camera or mode toggle
        IconButton(
            onClick = { /* switch camera */ },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
          Icon(imageVector = Icons.Default.FlipCameraIos, contentDescription = "Switch Camera", tint = Color.White)
        }
      }
    }
  }
}

fun imageProxyToBitmap(image: ImageProxy): Bitmap {
  val buffer = image.planes[0].buffer
  val bytes = ByteArray(buffer.remaining())
  buffer.get(bytes)
  val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
  
  // Rotate if needed (front camera rotation)
  val matrix = Matrix().apply {
    postRotate(image.imageInfo.rotationDegrees.toFloat())
    // Mirror front camera
    postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
  }
  return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
