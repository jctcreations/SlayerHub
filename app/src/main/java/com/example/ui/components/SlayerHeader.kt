package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerStats
import com.example.ui.theme.*

@Composable
fun SlayerHeader(
    playerStats: PlayerStats,
    isSfxEnabled: Boolean = true,
    isAmbianceEnabled: Boolean = true,
    onToggleSfx: () -> Unit = {},
    onToggleAmbiance: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("slayer_header"),
        color = SlayerNightDark,
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Top Row: Character Title & Quick Audio / Currency controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(SlayerCrimson, SlayerDarkRed)
                                )
                            )
                            .border(1.5.dp, SlayerAmberGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "B",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "BUFFY SUMMERS",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SlayerDarkRed)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "LVL ${playerStats.level}",
                                    color = SlayerCrimson,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Text(
                            text = "Night ${playerStats.nightsSurvived} • ${playerStats.slayerRank.title}",
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    }
                }

                // Audio Toggles & Currency Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleAmbiance,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("quick_toggle_ambiance")
                    ) {
                        Icon(
                            imageVector = if (isAmbianceEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                            contentDescription = "Ambiance",
                            tint = if (isAmbianceEnabled) SlayerAmberGold else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleSfx,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("quick_toggle_sfx")
                    ) {
                        Icon(
                            imageVector = if (isSfxEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Sound FX",
                            tint = if (isSfxEnabled) SlayerAmberGold else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SlayerSurfaceElevated)
                            .border(1.dp, SlayerCardBorder, RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "Occult Tokens",
                            tint = SlayerAmberGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${playerStats.occultTokens}",
                            color = SlayerAmberGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Health Bar
            val animatedHp by animateFloatAsState(
                targetValue = playerStats.currentHp.toFloat() / playerStats.maxHp.coerceAtLeast(1),
                label = "hp_bar"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Health",
                    tint = HealthRed,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "HP ${playerStats.currentHp}/${playerStats.maxHp}",
                            color = TextPrimaryDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (playerStats.shieldRemaining > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = SlayerWillowPurple,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "+${playerStats.shieldRemaining} Shield",
                                    color = SlayerWillowPurple,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF2C1620))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedHp)
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(HealthRed, Color(0xFFFF6E6E))
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Stamina & XP Bar Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Stamina Level
                val animatedStamina by animateFloatAsState(
                    targetValue = playerStats.stamina.toFloat() / playerStats.maxStamina.coerceAtLeast(1),
                    label = "stamina_bar"
                )
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsRun,
                                contentDescription = "Stamina",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Stamina",
                                color = TextSecondaryDark,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = "${playerStats.stamina}%",
                            color = Color(0xFF4CAF50),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF142917))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedStamina)
                                .fillMaxHeight()
                                .background(Color(0xFF4CAF50))
                        )
                    }
                }

                // XP to next level
                val animatedXp by animateFloatAsState(
                    targetValue = (playerStats.xp.toFloat() / playerStats.xpToNextLevel.coerceAtLeast(1)).coerceIn(0f, 1f),
                    label = "xp_bar"
                )
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "XP Progress",
                            color = TextSecondaryDark,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "${playerStats.xp}/${playerStats.xpToNextLevel}",
                            color = SlayerAmberGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF2C2410))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedXp)
                                .fillMaxHeight()
                                .background(XpGold)
                        )
                    }
                }
            }
        }
    }
}
