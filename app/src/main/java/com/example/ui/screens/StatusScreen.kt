package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerStats
import com.example.model.SlayerRank
import com.example.ui.theme.*

@Composable
fun StatusScreen(
    playerStats: PlayerStats,
    isSfxEnabled: Boolean,
    isAmbianceEnabled: Boolean,
    onToggleSfx: () -> Unit,
    onToggleAmbiance: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlayerNightDark)
            .padding(horizontal = 16.dp)
            .testTag("status_screen"),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Slayer Profile Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("slayer_profile_card"),
                colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SlayerAmberGold)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(listOf(SlayerCrimson, SlayerDarkRed))
                                )
                                .border(2.dp, SlayerAmberGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "B",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 28.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "BUFFY SUMMERS",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "The Chosen One • Sunnydale High",
                                color = SlayerAmberGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SlayerDarkRed)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "LEVEL ${playerStats.level}",
                                        color = SlayerCrimson,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = playerStats.slayerRank.title,
                                    color = SlayerAmberGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${playerStats.watcherRank}",
                                    color = SlayerWillowPurple,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = SlayerCardBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "\"In every generation there is a chosen one. She alone will stand against the vampires, the demons, and the forces of darkness.\"",
                        color = SlayerParchment,
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Section: Slayer Rank & Council Standing
        item {
            val currentRank = playerStats.slayerRank
            val nextRank = SlayerRank.getNextRank(currentRank)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("status_slayer_rank_card"),
                colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SlayerAmberGold)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    SlayerDarkRed.copy(alpha = 0.25f),
                                    SlayerSurfaceDark
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(SlayerCrimson, SlayerDarkRed)
                                        )
                                    )
                                    .border(1.5.dp, SlayerAmberGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MilitaryTech,
                                    contentDescription = "Slayer Rank",
                                    tint = SlayerAmberGold,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "OFFICIAL SLAYER RANK",
                                    color = SlayerAmberGold,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = currentRank.title.uppercase(),
                                    color = TextPrimaryDark,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        // Total XP pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SlayerNightDark)
                                .border(1.dp, SlayerCardBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${playerStats.totalXp} Total XP",
                                color = XpGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "\"${currentRank.badgeQuote}\"",
                        color = SlayerParchment,
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = SlayerCardBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (nextRank != null) {
                        val rankStart = currentRank.requiredTotalXp
                        val rankEnd = nextRank.requiredTotalXp
                        val rankRange = (rankEnd - rankStart).coerceAtLeast(1)
                        val rankProgress = ((playerStats.totalXp - rankStart).toFloat() / rankRange).coerceIn(0f, 1f)
                        val remainingXp = (rankEnd - playerStats.totalXp).coerceAtLeast(0)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Next Rank: ${nextRank.title}",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "$remainingXp XP to promote",
                                color = SlayerAmberGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { rankProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = SlayerAmberGold,
                            trackColor = Color(0xFF2C2410)
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Next Promotion Reward: +${nextRank.bonusTokens} Occult Tokens",
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    } else {
                        Text(
                            text = "👑 MAXIMUM SLAYER RANK ACHIEVED • LEGEND OF THE FIRST SLAYER",
                            color = SlayerAmberGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Section: Live Slayer Vitals (Health, Stamina, XP)
        item {
            Text(
                text = "SLAYER VITALS & ENERGY METERS",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }

        // Health Card
        item {
            VitalBarCard(
                title = "Health Points (HP)",
                current = playerStats.currentHp,
                max = playerStats.maxHp,
                icon = Icons.Default.Favorite,
                iconTint = HealthRed,
                barColor = HealthRed,
                trackColor = Color(0xFF2C1620),
                subtitle = if (playerStats.currentHp >= playerStats.maxHp) "Fully Rested & Combat Ready" else "Injured • Rest with Giles in the Library to heal",
                tag = "status_health_card"
            )
        }

        // Stamina Card
        item {
            VitalBarCard(
                title = "Slayer Stamina & Exertion",
                current = playerStats.stamina,
                max = playerStats.maxStamina,
                icon = Icons.Default.DirectionsRun,
                iconTint = Color(0xFF4CAF50),
                barColor = Color(0xFF4CAF50),
                trackColor = Color(0xFF142917),
                subtitle = "Recharges automatically (+3 every 2.5s). Powers martial arts combos and acrobatics.",
                tag = "status_stamina_card"
            )
        }

        // Slayer Experience (XP) Card
        item {
            VitalBarCard(
                title = "Slayer Experience (XP)",
                current = playerStats.xp,
                max = playerStats.xpToNextLevel,
                icon = Icons.Default.Star,
                iconTint = XpGold,
                barColor = XpGold,
                trackColor = Color(0xFF2C2410),
                subtitle = "${playerStats.xpToNextLevel - playerStats.xp} XP needed to reach Level ${playerStats.level + 1} (Grants +15 Max HP & +3 Power)",
                tag = "status_xp_card"
            )
        }

        // Adrenaline Card
        item {
            VitalBarCard(
                title = "Combat Adrenaline",
                current = playerStats.energy,
                max = 100,
                icon = Icons.Default.FlashOn,
                iconTint = EnergyBlue,
                barColor = EnergyBlue,
                trackColor = Color(0xFF0D253D),
                subtitle = "Fills up when landing combos and dodging attacks. Triggers critical bursts.",
                tag = "status_energy_card"
            )
        }

        // Section: Combat Aptitude
        item {
            Text(
                text = "COMBAT ATTRIBUTES & GEAR",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("combat_attributes_card"),
                colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AttributeRow(
                        title = "Martial Strike Power",
                        value = "+${playerStats.martialPower} Base DMG",
                        description = "Determines raw punch, kick, and melee impact.",
                        icon = Icons.Default.SportsMartialArts
                    )
                    Divider(color = SlayerCardBorder)
                    AttributeRow(
                        title = "Slayer Reflexes & Parry",
                        value = "+${playerStats.agilityParryBonus}% Critical Chance",
                        description = "Increases dodge counter-attack damage and precision.",
                        icon = Icons.Default.Shield
                    )
                    Divider(color = SlayerCardBorder)
                    AttributeRow(
                        title = "Currencies & Relics",
                        value = "${playerStats.occultTokens} Occult Tokens",
                        description = "Used to purchase holy water, weapons, and training from Giles.",
                        icon = Icons.Default.Stars
                    )
                }
            }
        }

        // Section: Lifetime Career Statistics
        item {
            Text(
                text = "SUNNYDALE PATROL ACHIEVEMENTS",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("achievements_card"),
                colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatRow("Vampires Staked to Dust", "${playerStats.vampiresDusted}", SlayerCrimson)
                    StatRow("Hellmouth Demons Banished", "${playerStats.demonsVanquished}", SlayerAmberGold)
                    StatRow("Sunnydale Students Rescued", "${playerStats.civiliansSaved}", SlayerWillowPurple)
                    StatRow("Nights Survived in Sunnydale", "${playerStats.nightsSurvived}", TextPrimaryDark)
                    StatRow("Buffy Trivia Correct Answers", "${playerStats.triviaCorrectCount}", Color(0xFF81C784))
                    StatRow("Best Trivia Streak", "${playerStats.bestTriviaStreak} Streak", SlayerParchment)
                }
            }
        }

        // Section: Sound & Ambiance Controls
        item {
            Text(
                text = "AUDIO & AMBIANCE SETTINGS",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("audio_settings_card"),
                colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Combat & Trivia Sound Effects",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Staking, dusting, punch thuds, and trivia chimes",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                        Switch(
                            checked = isSfxEnabled,
                            onCheckedChange = { onToggleSfx() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SlayerAmberGold,
                                checkedTrackColor = SlayerDarkRed
                            ),
                            modifier = Modifier.testTag("toggle_sfx_switch")
                        )
                    }

                    Divider(color = SlayerCardBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Sunnydale Night Ambiance",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Atmospheric supernatural cemetery wind & low drone",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                        Switch(
                            checked = isAmbianceEnabled,
                            onCheckedChange = { onToggleAmbiance() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SlayerAmberGold,
                                checkedTrackColor = SlayerDarkRed
                            ),
                            modifier = Modifier.testTag("toggle_ambiance_switch")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VitalBarCard(
    title: String,
    current: Int,
    max: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    barColor: Color,
    trackColor: Color,
    subtitle: String,
    tag: String
) {
    val progress = (current.toFloat() / max.coerceAtLeast(1)).coerceIn(0f, 1f)
    val animatedProg by animateFloatAsState(targetValue = progress, label = "prog")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag),
        colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "$current / $max",
                    color = iconTint,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(trackColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProg)
                        .fillMaxHeight()
                        .background(barColor)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                color = TextSecondaryDark,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun AttributeRow(
    title: String,
    value: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SlayerSurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SlayerAmberGold,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = value,
                    color = SlayerAmberGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            Text(
                text = description,
                color = TextSecondaryDark,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
    valueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextSecondaryDark, fontSize = 12.sp)
        Text(text = value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
