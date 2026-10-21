package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.CameramanModelFactory
import com.example.viewmodel.CameramanViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val navController = rememberNavController()
        val viewModel: CameramanViewModel = viewModel(factory = CameramanModelFactory(application))

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = DarkBackground
        ) { innerPadding ->
          NavHost(
              navController = navController,
              startDestination = "splash",
              modifier = Modifier.padding(innerPadding)
          ) {
            composable("splash") {
              SplashScreen(
                  onTimeout = {
                    navController.navigate("home") {
                      popUpTo("splash") { inclusive = true }
                    }
                  }
              )
            }
            composable("home") {
              HomeScreen(
                  viewModel = viewModel,
                  onStartShooting = { sceneId -> navController.navigate("scene_planner/$sceneId") },
                  onNavigateToProjects = { navController.navigate("projects") },
                  onNavigateToSettings = { navController.navigate("settings") }
              )
            }
            composable(
                route = "scene_planner/{sceneId}",
                arguments = listOf(navArgument("sceneId") { type = NavType.StringType })
            ) { backStackEntry ->
              val sceneId = backStackEntry.arguments?.getString("sceneId") ?: ""
              ScenePlannerScreen(
                  sceneId = sceneId,
                  viewModel = viewModel,
                  onStartCamera = { id -> navController.navigate("live_camera/$id") },
                  onBack = { navController.popBackStack() }
              )
            }
            composable(
                route = "live_camera/{sceneId}",
                arguments = listOf(navArgument("sceneId") { type = NavType.StringType })
            ) { backStackEntry ->
              val sceneId = backStackEntry.arguments?.getString("sceneId") ?: ""
              LiveCameraScreen(
                  sceneId = sceneId,
                  viewModel = viewModel,
                  onVideoRecorded = { path ->
                    val encoded = android.net.Uri.encode(path)
                    navController.navigate("video_preview/$encoded")
                  },
                  onBack = { navController.popBackStack() }
              )
            }
            composable(
                route = "video_preview/{path}",
                arguments = listOf(navArgument("path") { type = NavType.StringType })
            ) { backStackEntry ->
              val path = backStackEntry.arguments?.getString("path") ?: ""
              VideoPreviewScreen(
                  videoPath = path,
                  viewModel = viewModel,
                  onSaveToProject = {
                    navController.navigate("projects") {
                      popUpTo("home")
                    }
                  },
                  onRetake = { navController.popBackStack() }
              )
            }
            composable("projects") {
              ProjectsScreen(
                  viewModel = viewModel,
                  onBack = { navController.popBackStack() }
              )
            }
            composable("settings") {
              SettingsScreen(
                  onBack = { navController.popBackStack() }
              )
            }
          }
        }
      }
    }
  }
}
