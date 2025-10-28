package com.irfandev.todo.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.unit.dp
import com.irfandev.todo.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // Load the custom font from resources
    val barlowFontFamily = FontFamily(Font(R.font.barlow_regular))

    // Setting up the typing effect: each letter appears one by one
    val textToShow = "Do"
    val currentIndex = remember { mutableStateOf(0) }  // Track which letter to show
    val alpha = remember { Animatable(0f) }  // Control alpha for typing effect

    // Launching the typing animation
    LaunchedEffect(Unit) {
        // Show each character of the text with a slight delay
        for (i in textToShow.indices) {
            delay(200)  // Delay between characters being typed
            currentIndex.value = i + 1  // Show next character

            // Fade in the character
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
            )

            delay(200)  // Pause after showing each character
        }

        // Wait after the typing animation is complete (for 1 second)
        delay(300)  // After typing, wait for 1 second

        // Fade out the text after typing (fade out over 800ms)
        alpha.animateTo(
            targetValue = 0f,  // Fade out
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )

        // Wait for fade-out to finish (800ms)
        delay(200)

        // Trigger the next screen transition (e.g., navigate to the main app screen)
        onSplashFinished()
    }

    // Layout for Splash Screen with background color and animated text
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5EB090)),  // Background color #5eb090
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textToShow.take(currentIndex.value),  // Show only the characters that have been revealed
            style = TextStyle(
                fontFamily = barlowFontFamily,
                fontSize = 100.sp,
                color = Color.White.copy(alpha = alpha.value)
            ),
            modifier = Modifier
                .background(Color.Transparent)
        )
    }
}
