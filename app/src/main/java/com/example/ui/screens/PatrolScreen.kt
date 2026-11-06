package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PatrolLocationType
import com.example.model.PlayerStats
import com.example.ui.theme.*

@Composable
fun PatrolScreen(
    playerStats: PlayerStats,
    onSelectLocation: (PatrolLocationType) -> Unit,
    onOpenStatusScreen: () -> Unit = {},
    onTriggerEmergencyEvent: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlayerNightDark)
            .padding(horizontal = 16.dp)
            .testTag("patrol_screen"),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Banner Card: Cemetery at Night
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .testTag("patrol_hero_banner"),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = com.example.R.drawable.cemetery_bg_1793562410209),
                        contentDescription = "Sunnydale Cemetery at Dusk",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, SlayerNightDark.copy(alpha = 0.92f))
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "SUNNYDALE NIGHT PATROL",
                            color = SlayerAmberGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Darkness has fallen over California. Vampires and Hellmouth fiends stalk the innocent.",
                            color = TextPrimaryDark,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Live Slayer Status Display in Main Patrol UI (Health, Stamina, XP)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onOpenStatusScreen() }
                    .testTag("patrol_status_summary_card"),
                colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SlayerCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(SlayerDarkRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = SlayerAmberGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "SLAYER STATUS & CONDITION",
                                    color = TextPrimaryDark,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Level ${playerStats.level} • ${playerStats.slayerRank.title}",
                                    color = SlayerAmberGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        TextButton(
                            onClick = onOpenStatusScreen,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Full Dossier →",
                                color = SlayerParchment,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 1. Health Bar in Main Patrol UI
                    val hpProgress = (playerStats.currentHp.toFloat() / playerStats.maxHp.coerceAtLeast(1)).coerceIn(0f, 1f)
                    val animatedHp by animateFloatAsState(targetValue = hpProgress, label = "hp")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = HealthRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Health (HP)",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = "${playerStats.currentHp} / ${playerStats.maxHp}",
                            color = HealthRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
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
                                .background(HealthRed)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. Stamina Bar in Main Patrol UI
                    val staminaProgress = (playerStats.stamina.toFloat() / playerStats.maxStamina.coerceAtLeast(1)).coerceIn(0f, 1f)
                    val animatedStamina by animateFloatAsState(targetValue = staminaProgress, label = "stamina")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsRun,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Stamina Level",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = "${playerStats.stamina} / ${playerStats.maxStamina} (Regens)",
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF142917))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedStamina)
                                .fillMaxHeight()
                                .background(Color(0xFF4CAF50))
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. Slayer XP Progress in Main Patrol UI
                    val xpProgress = (playerStats.xp.toFloat() / playerStats.xpToNextLevel.coerceAtLeast(1)).coerceIn(0f, 1f)
                    val animatedXp by animateFloatAsState(targetValue = xpProgress, label = "xp")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = XpGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Slayer XP Progress",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = "${playerStats.xp} / ${playerStats.xpToNextLevel} XP",
                            color = XpGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
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

        // Slayer Patrol Record Strip
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SlayerSurfaceDark)
                    .border(1.dp, SlayerCardBorder, RoundedCornerShape(14.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${playerStats.vampiresDusted}",
                        color = SlayerCrimson,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Vamps Dusted",
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp),
                    color = SlayerCardBorder
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${playerStats.demonsVanquished}",
                        color = SlayerAmberGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Demons Slain",
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp),
                    color = SlayerCardBorder
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${playerStats.civiliansSaved}",
                        color = SlayerWillowPurple,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Students Saved",
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Emergency Night Events Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("urgent_patrol_events_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF26121C)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SlayerCrimson)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = SlayerAmberGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "NIGHT PATROL ENCOUNTERS",
                                color = SlayerAmberGold,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SlayerDarkRed)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "+BONUS XP",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Random tactical events like 'Graveyard Stake-out' and 'Hellmouth Breach' require distinct strategies (stealth staking, dimensional wards, or decoys) and grant huge XP bonuses!",
                        color = TextPrimaryDark,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onTriggerEmergencyEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("scout_emergency_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = SlayerCrimson),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radar,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Scout Urgent Night Encounter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SELECT PATROL DISTRICT",
                    color = TextPrimaryDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Hellmouth Active",
                    color = SlayerCrimson,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Patrol Location Cards
        items(PatrolLocationType.values(), key = { it.id }) { loc ->
            LocationCard(
                location = loc,
                onSelect = { onSelectLocation(loc) }
            )
        }
    }
}

@Composable
private fun LocationCard(
    location: PatrolLocationType,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("location_card_${location.id}"),
        colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = location.displayName,
                        color = TextPrimaryDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = location.subtitle,
                        color = SlayerAmberGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Danger Stars
                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < location.dangerStars) SlayerCrimson else Color(0xFF332030),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = location.description,
                color = TextSecondaryDark,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("patrol_button_${location.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (location == PatrolLocationType.THE_HELLMOUTH) SlayerDarkRed else SlayerCrimson
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (location == PatrolLocationType.THE_HELLMOUTH) "ENTER THE HELLMOUTH (BOSS)" else "PATROL THIS DISTRICT",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
