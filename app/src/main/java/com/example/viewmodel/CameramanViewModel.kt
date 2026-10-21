package com.example.viewmodel

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CameramanRepository
import com.example.model.RecordedClip
import com.example.model.SceneProject
import com.example.model.ShotItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class CameramanViewModel(application: Application) : AndroidViewModel(application) {

  private val _scenes = MutableStateFlow<List<SceneProject>>(CameramanRepository.defaultScenes)
  val scenes: StateFlow<List<SceneProject>> = _scenes.asStateFlow()

  private val _currentScene = MutableStateFlow<SceneProject?>(CameramanRepository.defaultScenes.first())
  val currentScene: StateFlow<SceneProject?> = _currentScene.asStateFlow()

  private val _recordedClips = MutableStateFlow<List<RecordedClip>>(emptyList())
  val recordedClips: StateFlow<List<RecordedClip>> = _recordedClips.asStateFlow()

  private val _directorMessage = MutableStateFlow("Pan camera slowly from left to right. Keep horizon level.")
  val directorMessage: StateFlow<String> = _directorMessage.asStateFlow()

  init {
    loadRecordedClips()
  }

  fun selectScene(sceneId: String) {
    val found = _scenes.value.find { it.id == sceneId }
    if (found != null) {
      _currentScene.value = found
    }
  }

  fun toggleShotCompleted(shotId: String) {
    _currentScene.update { scene ->
      scene?.copy(
          shots = scene.shots.map { shot ->
            if (shot.id == shotId) shot.copy(isCompleted = !shot.isCompleted) else shot
          }
      )
    }
  }

  fun addRecordedClip(filePath: String, duration: Int, shotTitle: String) {
    viewModelScope.launch {
      val clip = RecordedClip(
          id = filePath,
          filePath = filePath,
          durationSeconds = duration,
          shotTitle = shotTitle,
          timestamp = System.currentTimeMillis()
      )
      _recordedClips.update { current -> listOf(clip) + current }
    }
  }

  fun deleteClip(clipId: String) {
    viewModelScope.launch {
      try {
        val file = File(clipId)
        if (file.exists()) file.delete()
        _recordedClips.update { current -> current.filter { it.id != clipId } }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun updateDirectorGuidance(message: String) {
    _directorMessage.value = message
  }

  private fun loadRecordedClips() {
    viewModelScope.launch {
      try {
        val appDir = getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            ?: getApplication<Application>().filesDir
        if (appDir.exists()) {
          val files = appDir.listFiles { _, name -> name.startsWith("Cameraman_") && name.endsWith(".mp4") }
          files?.let { fList ->
            val loaded = fList.sortedByDescending { it.lastModified() }.map { file ->
              RecordedClip(
                  id = file.absolutePath,
                  filePath = file.absolutePath,
                  durationSeconds = 10,
                  shotTitle = "Captured Shot",
                  timestamp = file.lastModified()
              )
            }
            _recordedClips.value = loaded
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }
}
