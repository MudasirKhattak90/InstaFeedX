package com.example.instafeedx.screen

import android.window.SplashScreen
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val BgDark = Color(0xFF0D0D12)
private val AccentStart = Color(0xFF7B5EA7)
private val AccentEnd = Color(0xFF4F8EF7)
private val TextPrimary = Color(0xFFF1F1F5)
private val TextHint = Color(0xFF6B6B80)


@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(true) {
        // Logo animate karo
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 700,
                easing = EaseOutBack
            )
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )

        // 2 second ruko
        delay(1800)

        // Main screen pe jao
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
        contentAlignment = Alignment.Center
    ) {
        // Background glow
        Box(
            modifier = Modifier
                .size(350.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentStart.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ── Logo Box ─────────────────────────────────
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .size(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(AccentStart, AccentEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "IF",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── App Name ──────────────────────────────────
            Text(
                text = "InstaFeedX",
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Share your moments",
                color = TextHint,
                fontSize = 13.sp,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }

}