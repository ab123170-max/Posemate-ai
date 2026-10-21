package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.SavedPhoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PoseViewModel(application: Application) : AndroidViewModel(application) {

  private val _savedPhotos = MutableStateFlow<List<SavedPhoto>>(emptyList())
  val savedPhotos: StateFlow<List<SavedPhoto>> = _savedPhotos.asStateFlow()

  private val _currentPoseId = MutableStateFlow<String?>(null)
  val currentPoseId: StateFlow<String?> = _currentPoseId.asStateFlow()

  private val _feedbackMessage = MutableStateFlow("Position yourself in front of the camera")
  val feedbackMessage: StateFlow<String> = _feedbackMessage.asStateFlow()

  init {
    loadSavedPhotos()
  }

  fun setCurrentPose(poseId: String?) {
    _currentPoseId.value = poseId
  }

  fun updateFeedback(message: String) {
    _feedbackMessage.value = message
  }

  fun savePhoto(bitmap: Bitmap, poseTitle: String, onSaved: (SavedPhoto) -> Unit) {
    viewModelScope.launch {
      try {
        val appDir = getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: getApplication<Application>().filesDir
        if (!appDir.exists()) appDir.mkdirs()

        val fileName = "PoseMate_${System.currentTimeMillis()}.jpg"
        val file = File(appDir, fileName)
        FileOutputStream(file).use { out ->
          bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }

        val savedPhoto = SavedPhoto(
            id = file.absolutePath,
            filePath = file.absolutePath,
            poseTitle = poseTitle,
            timestamp = System.currentTimeMillis()
        )

        _savedPhotos.update { current -> listOf(savedPhoto) + current }
        onSaved(savedPhoto)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun deletePhoto(photoId: String) {
    viewModelScope.launch {
      try {
        val file = File(photoId)
        if (file.exists()) {
          file.delete()
        }
        _savedPhotos.update { current -> current.filter { it.id != photoId } }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  private fun loadSavedPhotos() {
    viewModelScope.launch {
      try {
        val appDir = getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: getApplication<Application>().filesDir
        if (appDir.exists()) {
          val files = appDir.listFiles { _, name -> name.startsWith("PoseMate_") && name.endsWith(".jpg") }
          files?.let { fList ->
            val loaded = fList.sortedByDescending { it.lastModified() }.map { file ->
              SavedPhoto(
                  id = file.absolutePath,
                  filePath = file.absolutePath,
                  poseTitle = "Practice Session",
                  timestamp = file.lastModified()
              )
            }
            _savedPhotos.value = loaded
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }
}
