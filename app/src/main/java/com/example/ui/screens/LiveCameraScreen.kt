package com.example.ui.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
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
import androidx.core.content.PermissionChecker
import com.example.data.CameramanRepository
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.CameramanViewModel
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveCameraScreen(
    sceneId: String,
    viewModel: CameramanViewModel,
    onVideoRecorded: (String) -> Unit,
    onBack: () -> Unit
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  val scene = CameramanRepository.defaultScenes.find { it.id == sceneId }
      ?: CameramanRepository.defaultScenes.first()
  val activeShot = scene.shots.firstOrNull() ?: scene.shots.first()

  var hasPermissions by remember {
    mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    )
  }

  val permissionLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    hasPermissions = permissions[Manifest.permission.CAMERA] == true &&
        permissions[Manifest.permission.RECORD_AUDIO] == true
  }

  LaunchedEffect(Unit) {
    if (!hasPermissions) {
      permissionLauncher.launch(
          arrayOf(
              Manifest.permission.CAMERA,
              Manifest.permission.RECORD_AUDIO
          )
      )
    }
  }

  var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
  var recording by remember { mutableStateOf<Recording?>(null) }
  var isRecording by remember { mutableStateOf(false) }
  var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
  var isTorchOn by remember { mutableStateOf(false) }
  var cameraInstance by remember { mutableStateOf<androidx.camera.core.Camera?>(null) }

  // Director movement instructions rotation
  val instructions = remember {
    listOf(
        "Pan camera slowly from left to right",
        "Hold steady, keep horizon perfectly level",
        "Smooth push-in toward the subject",
        "Tilt down gently to capture detail",
        "Maintain steady handheld breathing motion"
    )
  }
  var currentInstructionIndex by remember { mutableStateOf(0) }

  LaunchedEffect(isRecording) {
    while (isRecording) {
      delay(4000)
      currentInstructionIndex = (currentInstructionIndex + 1) % instructions.size
    }
  }

  var recordingSeconds by remember { mutableStateOf(0) }
  LaunchedEffect(isRecording) {
    recordingSeconds = 0
    while (isRecording) {
      delay(1000)
      recordingSeconds++
    }
  }

  if (!hasPermissions) {
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
            imageVector = Icons.Default.Videocam,
            contentDescription = null,
            tint = PrimaryNeon,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "Camera & Audio Permissions Required",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Cameraman AI requires camera and microphone access to record professional video clips and provide director coaching.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Button(
            onClick = {
              permissionLauncher.launch(
                  arrayOf(
                      Manifest.permission.CAMERA,
                      Manifest.permission.RECORD_AUDIO
                  )
              )
            },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon),
            modifier = Modifier.testTag("grant_permissions_button")
        ) {
          Text(text = "Grant Permissions", color = DarkBackground, fontWeight = FontWeight.Bold)
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
    // CameraX PreviewView
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

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HD))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            try {
              cameraProvider.unbindAll()
              cameraInstance = cameraProvider.bindToLifecycle(
                  lifecycleOwner,
                  cameraSelector,
                  preview,
                  videoCapture
              )
            } catch (exc: Exception) {
              Log.e("LiveCamera", "Use case binding failed", exc)
            }
          }, ContextCompat.getMainExecutor(ctx))
          previewView
        },
        modifier = Modifier.fillMaxSize()
    )

    // Top Controls Bar
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

      // Recording timer / Indicator
      if (isRecording) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.Red.copy(alpha = 0.8f)
        ) {
          Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(8.dp))
            val mins = recordingSeconds / 60
            val secs = recordingSeconds % 60
            Text(
                text = String.format(Locale.US, "%02d:%02d", mins, secs),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
          }
        }
      } else {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.Black.copy(alpha = 0.6f)
        ) {
          Text(
              text = activeShot.title,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
          )
        }
      }

      // Flip camera or Torch
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButton(
            onClick = {
              isTorchOn = !isTorchOn
              cameraInstance?.cameraControl?.enableTorch(isTorchOn)
            },
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
          Icon(
              imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
              contentDescription = "Torch",
              tint = if (isTorchOn) PrimaryNeon else Color.White
          )
        }
        IconButton(
            onClick = {
              cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
              } else {
                CameraSelector.DEFAULT_BACK_CAMERA
              }
            },
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
          Icon(imageVector = Icons.Default.FlipCameraIos, contentDescription = "Switch Camera", tint = Color.White)
        }
      }
    }

    // AI Director Movement Instruction Banner
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
          Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = DarkBackground)
          Spacer(modifier = Modifier.width(12.dp))
          Text(
              text = instructions[currentInstructionIndex],
              color = DarkBackground,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Bottom Recording Shutter Bar
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
        // Shot Info button
        IconButton(
            onClick = { /* show shot description dialog */ },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
          Icon(imageVector = Icons.Default.Info, contentDescription = "Shot Info", tint = Color.White)
        }

        // Recording Shutter Button
        FloatingActionButton(
            onClick = {
              val capture = videoCapture ?: return@FloatingActionButton
              if (recording != null) {
                // Stop recording
                recording?.stop()
                recording = null
                isRecording = false
              } else {
                // Start recording
                val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis())
                val contentValues = ContentValues().apply {
                  put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                  put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameramanAI")
                  }
                }

                val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
                    context.contentResolver,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                ).setContentValues(contentValues).build()

                if (ActivityCompatCheck(context)) {
                  recording = capture.output
                      .prepareRecording(context, mediaStoreOutputOptions)
                      .apply {
                        if (PermissionChecker.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PermissionChecker.PERMISSION_GRANTED
                        ) {
                          withAudioEnabled()
                        }
                      }
                      .start(ContextCompat.getMainExecutor(context)) { recordEvent ->
                        when (recordEvent) {
                          is VideoRecordEvent.Start -> {
                            isRecording = true
                          }
                          is VideoRecordEvent.Finalize -> {
                            isRecording = false
                            if (!recordEvent.hasError()) {
                              val uri = recordEvent.outputResults.outputUri
                              viewModel.addRecordedClip(uri.toString(), recordingSeconds, activeShot.title)
                              onVideoRecorded(uri.toString())
                            } else {
                              Log.e("LiveCamera", "Video capture error: ${recordEvent.error}")
                            }
                          }
                        }
                      }
                }
              }
            },
            modifier = Modifier
                .size(76.dp)
                .testTag("record_shutter_button"),
            shape = CircleShape,
            containerColor = if (isRecording) Color.Red else PrimaryNeon
        ) {
          Box(
              modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
                  .background(if (isRecording) Color.Red else PrimaryNeon),
              contentAlignment = Alignment.Center
          ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                contentDescription = "Record",
                tint = if (isRecording) Color.White else DarkBackground,
                modifier = Modifier.size(36.dp)
            )
          }
        }

        // Gallery / Clips shortcut
        IconButton(
            onClick = { /* view gallery */ },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
          Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = "Gallery", tint = Color.White)
        }
      }
    }
  }
}

fun ActivityCompatCheck(context: Context): Boolean {
  return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}
