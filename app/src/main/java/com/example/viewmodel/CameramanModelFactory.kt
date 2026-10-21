package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CameramanModelFactory(private val application: Application) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(CameramanViewModel::class.java)) {
      return CameramanViewModel(application) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
