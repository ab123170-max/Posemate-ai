package com.example.data

import com.example.model.ConfidenceStep
import com.example.model.ExpressionItem
import com.example.model.PoseItem

object PoseRepository {
  val poses = listOf(
      PoseItem(
          id = "standing_portrait",
          title = "Standing Portrait",
          category = "Portrait",
          description = "A classic portrait pose that elongates the body and creates a polished, professional silhouette.",
          bodyPositioning = "Angle your body 45 degrees to the camera. Shift your weight onto your back foot.",
          handPositioning = "Keep arms relaxed with a slight bend at the elbows. Avoid stiff sides.",
          headAngle = "Face forward or tilt slightly toward the camera with the chin down just a fraction.",
          suggestedExpression = "Gentle, approachable smile or calm confidence.",
          commonMistakes = "Standing completely square to the camera can look stiff and blocky."
      ),
      PoseItem(
          id = "casual_standing",
          title = "Casual Standing",
          category = "Casual",
          description = "Relaxed and approachable posture ideal for lifestyle, social media, and everyday photos.",
          bodyPositioning = "Stand naturally with feet shoulder-width apart. Let one knee bend slightly.",
          handPositioning = "One hand resting casually on a thigh or holding a jacket/bag strap.",
          headAngle = "Natural head tilt, looking slightly off-center or directly at the lens.",
          suggestedExpression = "Warm, natural smile.",
          commonMistakes = "Slouching shoulders or locking your knees."
      ),
      PoseItem(
          id = "hands_in_pockets",
          title = "Hands-in-Pockets",
          category = "Casual",
          description = "Instantly relaxes tense upper body posture and exudes effortless confidence.",
          bodyPositioning = "Feet slightly apart, weight shifted to one side. Shoulders dropped.",
          handPositioning = "Thumbs hooked in front pockets with fingers relaxed outside.",
          headAngle = "Level head or slight downward glance before looking up.",
          suggestedExpression = "Subtle smirk or calm friendly gaze.",
          commonMistakes = "Pushing hands too deep into tight pockets, causing hunching."
      ),
      PoseItem(
          id = "one_hand_on_hip",
          title = "One-Hand-on-Hip",
          category = "Full Body",
          description = "Defines the waistline and creates dynamic angles that add energy to full-body shots.",
          bodyPositioning = "Place one foot forward pointing toward the lens. Pop one hip outward.",
          handPositioning = "Rest one hand firmly on your hip with elbow pointing slightly outward.",
          headAngle = "Chin up and rotated slightly toward the camera.",
          suggestedExpression = "Confident, engaging smile.",
          commonMistakes = "Pressing hand too hard against the waist, making the arm look rigid."
      ),
      PoseItem(
          id = "walking_towards",
          title = "Walking Towards Camera",
          category = "Full Body",
          description = "Captures authentic motion and natural fluidity, making photos feel alive.",
          bodyPositioning = "Take a natural stride forward. Keep torso upright and core engaged.",
          handPositioning = "Natural arm swing opposite to your leading foot.",
          headAngle = "Eyes locked on the camera lens, chin level.",
          suggestedExpression = "Bright, dynamic smile or joyful laugh.",
          commonMistakes = "Stiff robotic steps or staring down at your feet."
      ),
      PoseItem(
          id = "over_shoulder",
          title = "Looking Over Shoulder",
          category = "Portrait",
          description = "Mysterious and elegant pose that highlights facial features and necklines.",
          bodyPositioning = "Turn your back or side almost entirely away from the camera.",
          handPositioning = "Arms relaxed naturally by your sides or resting on a prop.",
          headAngle = "Rotate your head back to look directly over your shoulder at the lens.",
          suggestedExpression = "Soft, enigmatic gaze with parted lips.",
          commonMistakes = "Straining your neck too far; keep rotation comfortable."
      ),
      PoseItem(
          id = "sitting_naturally",
          title = "Sitting Naturally",
          category = "Casual",
          description = "Grounded and comfortable pose that reduces camera anxiety.",
          bodyPositioning = "Sit on the edge of a chair or bench. Keep torso leaning slightly forward.",
          handPositioning = "Hands resting gently on knees or interlaced in your lap.",
          headAngle = "Relaxed head position looking slightly to the side or at camera.",
          suggestedExpression = "Warm, friendly smile.",
          commonMistakes = "Slumping back into the chair, which rounds the shoulders."
      ),
      PoseItem(
          id = "leaning_against_wall",
          title = "Leaning Against Wall",
          category = "Casual",
          description = "Laid-back, cool aesthetic that works great in urban or indoor settings.",
          bodyPositioning = "Lightly lean your hip or shoulder against a wall. Cross one ankle over the other.",
          handPositioning = "Arms crossed loosely or one arm resting against the wall surface.",
          headAngle = "Relaxed tilt away from the wall.",
          suggestedExpression = "Casual, easygoing smirk or neutral calm.",
          commonMistakes = "Planting your whole back against the wall instead of a gentle hip lean."
      ),
      PoseItem(
          id = "fashion_pose",
          title = "Full-Body Fashion Pose",
          category = "Full Body",
          description = "Edgy, high-fashion stance emphasizing clean lines and structured angles.",
          bodyPositioning = "Asymmetrical weight distribution. One leg extended, shoulders offset.",
          handPositioning = "One hand near collarbone or framing jawline, other hanging loosely.",
          headAngle = "Strong chin tilt, piercing gaze past or at the lens.",
          suggestedExpression = "Intense, neutral fashion expression.",
          commonMistakes = "Looking too rigid or tense in the jaw."
      ),
      PoseItem(
          id = "closeup_portrait",
          title = "Close-Up Portrait",
          category = "Portrait",
          description = "Intimate and striking headshot focusing entirely on expression and eyes.",
          bodyPositioning = "Face angled slightly sideways to give depth to the jawline.",
          handPositioning = "Hands out of frame or lightly touching collar.",
          headAngle = "Chin tilted down slightly to emphasize eyes.",
          suggestedExpression = "Engaging eye contact with a soft smile.",
          commonMistakes = "Wide-eyed deer-in-headlights stare; relax your eyelids."
      ),
      PoseItem(
          id = "confident_hero",
          title = "Confident Hero Pose",
          category = "Confident",
          description = "Powerful posture designed to boost inner confidence and project authority.",
          bodyPositioning = "Stand tall with feet wide. Broaden your chest and stand upright.",
          handPositioning = "Hands on hips (power stance) or arms relaxed with fists lightly closed.",
          headAngle = "Chin level, direct unwavering eye contact with the lens.",
          suggestedExpression = "Resolute, confident, and welcoming.",
          commonMistakes = "Puffing chest out too aggressively like a bodybuilder."
      ),
      PoseItem(
          id = "natural_candid",
          title = "Natural Candid Pose",
          category = "Casual",
          description = "Simulates an unposed, spontaneous moment of joy or laughter.",
          bodyPositioning = "Loose, flexible stance with weight shifting between feet.",
          handPositioning = "Arms in natural gestures, touching hair or adjusting clothing.",
          headAngle = "Looking slightly away from the camera as if caught mid-laugh.",
          suggestedExpression = "Genuine smile or soft chuckle.",
          commonMistakes = "Forcing a fake laugh that doesn't reach your eyes."
      )
  )

  val expressions = listOf(
      ExpressionItem(
          id = "natural_smile",
          title = "Natural Smile",
          description = "A warm, genuine smile that lights up your eyes and feels inviting.",
          eyes = "Smile with your eyes (crinkle corners slightly, avoid dead stare).",
          eyebrows = "Relaxed in their natural resting position.",
          lips = "Parted slightly showing a hint of upper teeth without a forced grin.",
          jaw = "Relaxed and unclenching your teeth.",
          head = "Level or slightly tilted down."
      ),
      ExpressionItem(
          id = "confident_look",
          title = "Confident Look",
          description = "Strong, self-assured expression that projects authority and poise.",
          eyes = "Narrow your eyelids ever so slightly ('squinch') for intensity.",
          eyebrows = "Slightly lowered to show focus and determination.",
          lips = "Firmly closed in a calm, neutral line.",
          jaw = "Strong and steady.",
          head = "Chin level and firm."
      ),
      ExpressionItem(
          id = "friendly_expression",
          title = "Friendly Expression",
          description = "Welcoming and open look perfect for professional profiles and networking.",
          eyes = "Open and bright, looking directly into the camera lens.",
          eyebrows = "Slightly lifted to appear approachable and attentive.",
          lips = "Soft closed-mouth smile.",
          jaw = "Completely relaxed.",
          head = "Tilted slightly to one side."
      ),
      ExpressionItem(
          id = "serious_cinematic",
          title = "Serious Cinematic Look",
          description = "Moody, dramatic expression with deep cinematic character.",
          eyes = "Gazing intently past the lens or with deep focus.",
          eyebrows = "Neutral or very subtly drawn together.",
          lips = "Relaxed, slightly parted lips.",
          jaw = "Relaxed, no tension.",
          head = "Turned 30 degrees away from the camera."
      ),
      ExpressionItem(
          id = "soft_romantic",
          title = "Soft Romantic Expression",
          description = "Gentle, dreamy, and soft expression.",
          eyes = "Soft focus, eyelids relaxed.",
          eyebrows = "Completely relaxed.",
          lips = "Softly parted.",
          jaw = "Soft and relaxed.",
          head = "Tilted downward with a soft sideways gaze."
      ),
      ExpressionItem(
          id = "playful_expression",
          title = "Playful Expression",
          description = "Fun, cheeky expression full of personality.",
          eyes = "Bright and expressive.",
          eyebrows = "One eyebrow raised slightly.",
          lips = "Asymmetrical smirk or cheerful grin.",
          jaw = "Relaxed.",
          head = "Tilted playfully to the side."
      ),
      ExpressionItem(
          id = "relaxed_neutral",
          title = "Relaxed Neutral Face",
          description = "Calm, centered expression with zero facial tension.",
          eyes = "Steady, calm gaze at the lens.",
          eyebrows = "Completely neutral.",
          lips = "Closed naturally, teeth touching lightly.",
          jaw = "Dropped and relaxed.",
          head = "Dead center, level."
      )
  )

  val confidenceRoutine = listOf(
      ConfidenceStep(
          stepNumber = 1,
          title = "Relaxed Neutral Face",
          instruction = "Take a deep breath. Drop your jaw and release all tension in your forehead.",
          tip = "A calm mind creates a natural, serene expression."
      ),
      ConfidenceStep(
          stepNumber = 2,
          title = "Small Natural Smile",
          instruction = "Think of something pleasant and let the corners of your mouth curl up gently.",
          tip = "Remember to smile with your eyes, not just your lips."
      ),
      ConfidenceStep(
          stepNumber = 3,
          title = "Direct Lens Look",
          instruction = "Imagine the camera lens is a friendly person you are greeting.",
          tip = "Direct eye contact builds instant connection and trust."
      ),
      ConfidenceStep(
          stepNumber = 4,
          title = "Slight Head Turn",
          instruction = "Turn your face 15 degrees to one side to sculpt your jawline.",
          tip = "Angling away from the camera is universally flattering."
      ),
      ConfidenceStep(
          stepNumber = 5,
          title = "Relax Shoulders & Hands",
          instruction = "Roll your shoulders back and down. Shake out your hands to release stiffness.",
          tip = "Lower shoulders instantly make you look taller and more confident."
      ),
      ConfidenceStep(
          stepNumber = 6,
          title = "Complete Portrait Pose",
          instruction = "Combine your relaxed shoulders, gentle smile, and angled stance.",
          tip = "You are ready to look fantastic in front of any camera!"
      )
  )
}
