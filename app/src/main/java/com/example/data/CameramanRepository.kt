package com.example.data

import com.example.model.SceneProject
import com.example.model.ShotItem

object CameramanRepository {
  val defaultScenes = listOf(
      SceneProject(
          id = "cinematic_interview",
          title = "Cinematic Interview",
          sceneType = "Interview & Dialogue",
          shots = listOf(
              ShotItem("s1", "Establishing Wide Shot", "Wide", 8, "Capture the full environment and subject context.", "Pan slowly across the room from left to right."),
              ShotItem("s2", "Medium Profile Shot", "Medium", 10, "Frame subject from waist up with shallow depth of field.", "Hold steady with slight handheld breathing motion."),
              ShotItem("s3", "Close-up Emotional Focus", "Close-up", 12, "Tight frame on subject's eyes and expressions during key answers.", "Zoom in slowly or push in smoothly toward subject."),
              ShotItem("s4", "Hands & Detail B-Roll", "Detail", 6, "Close-up shot of hands gesturing or holding objects.", "Hold camera stable at eye level with the subject's hands.")
          )
      ),
      SceneProject(
          id = "action_sequence",
          title = "Action Chase Sequence",
          sceneType = "Action & Movement",
          shots = listOf(
              ShotItem("a1", "Wide Tracking Shot", "Wide", 6, "Follow subject moving rapidly across the frame.", "Track parallel to the subject at matching speed."),
              ShotItem("a2", "Low Angle Power Shot", "Wide", 5, "Camera positioned low looking up to emphasize speed.", "Tilt up as subject passes overhead."),
              ShotItem("a3", "Dynamic Close-up", "Close-up", 4, "Intense focus on facial expression or footsteps.", "Keep subject centered with high shutter speed."),
              ShotItem("a4", "Point of View (POV)", "B-Roll", 6, "Immersive shot looking ahead from subject's perspective.", "Walk forward with minimal vertical bobbing.")
          )
      ),
      SceneProject(
          id = "travel_vlog",
          title = "Travel Vlog Cinematic",
          sceneType = "Travel & Lifestyle",
          shots = listOf(
              ShotItem("t1", "Scenic Panorama", "Wide", 10, "Majestic view of landscape or landmark.", "Smooth panning motion from left to right."),
              ShotItem("t2", "Walking Exploration", "Medium", 8, "Vlogger walking through bustling streets.", "Follow closely behind or walk backward facing subject."),
              ShotItem("t3", "Local Detail B-Roll", "Detail", 5, "Close-up of local food, crafts, or textures.", "Macro close-up with steady handheld hold."),
              ShotItem("t4", "Sunset Silhouette", "Wide", 8, "Striking silhouette against the golden hour sky.", "Lock off camera tripod-style or hold ultra-steady.")
          )
      ),
      SceneProject(
          id = "product_showcase",
          title = "Product Commercial",
          sceneType = "Commercial & Product",
          shots = listOf(
              ShotItem("p1", "Hero Product Reveal", "Medium", 6, "Introduce the product in its environment.", "Orbit slowly around the product in a 360 arc."),
              ShotItem("p2", "Feature Macro Detail", "Detail", 5, "Focus on the intricate texture or branding.", "Slow slider-style push-in movement."),
              ShotItem("p3", "In-Use Demonstration", "Close-up", 8, "Show someone interacting naturally with the product.", "Keep focus locked on the product interface.")
          )
      )
  )
}
