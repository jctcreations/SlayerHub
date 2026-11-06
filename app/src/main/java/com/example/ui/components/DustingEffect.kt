package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SlayerAmberGold
import com.example.ui.theme.SlayerAshSilver
import com.example.ui.theme.SlayerCrimson
import kotlin.random.Random

private data class AshParticle(
    val initialX: Float,
    val initialY: Float,
    val speedX: Float,
    val speedY: Float,
    val size: Float,
    val color: Color
)

@Composable
fun DustingParticleOverlay(
    modifier: Modifier = Modifier
) {
    val particles = remember {
        List(40) {
            AshParticle(
                initialX = Random.nextFloat(),
                initialY = Random.nextFloat(),
                speedX = (Random.nextFloat() - 0.5f) * 160f,
                speedY = -Random.nextFloat() * 220f - 80f,
                size = Random.nextFloat() * 7f + 3f,
                color = when (Random.nextInt(4)) {
                    0 -> SlayerAmberGold
                    1 -> SlayerAshSilver
                    2 -> SlayerCrimson
                    else -> Color(0xFFE0E0E0)
                }
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "ash_anim")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ash_prog"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            particles.forEach { particle ->
                val currentX = (particle.initialX * canvasWidth) + (particle.speedX * progress)
                val currentY = (particle.initialY * canvasHeight) + (particle.speedY * progress)
                val alpha = (1f - progress).coerceIn(0f, 1f)

                drawCircle(
                    color = particle.color.copy(alpha = alpha),
                    radius = particle.size * (1f - (progress * 0.3f)),
                    center = Offset(currentX, currentY)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "✨ DUSTED! ✨",
                color = SlayerAmberGold,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Turned to ash by the Slayer's stake",
                color = SlayerAshSilver,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
