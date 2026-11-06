package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun NightPatrolEventDialog(
    event: NightPatrolEvent,
    resolution: EventResolutionResult?,
    playerStats: PlayerStats,
    onChooseTactic: (TacticalOption) -> Unit,
    onDismiss: () -> Unit,
    onStartCombat: (EnemyType) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {
            if (resolution != null) onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val bgDrawable = if (event.type == NightEventType.HELLMOUTH_BREACH) {
            com.example.R.drawable.hellmouth_bg_1793562433564
        } else {
            com.example.R.drawable.cemetery_bg_1793562410209
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.88f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
                    .testTag("night_event_dialog"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SlayerCrimson)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Background Image
                    Image(
                        painter = painterResource(id = bgDrawable),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop,
                        alpha = 0.40f
                    )

                    // Gradient blend
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, SlayerSurfaceDark)
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Urgent Dispatch Banner
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(SlayerDarkRed)
                                    .border(1.dp, SlayerCrimson, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = SlayerAmberGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "⚡ URGENT PATROL DISPATCH",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp
                                )
                            }

                            if (resolution == null) {
                                IconButton(onClick = onDismiss) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Dismiss",
                                        tint = TextSecondaryDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Event Title & Subtitle
                        Text(
                            text = event.type.title.uppercase(),
                            color = SlayerAmberGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${event.type.subtitle} • High Danger Encounter",
                            color = SlayerParchment,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Narrative Briefing
                        Text(
                            text = event.type.narrative,
                            color = TextPrimaryDark,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Potential Rewards Badge
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SlayerSurfaceElevated)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Base Rewards on Clearance:",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "+${event.type.baseBonusXp} XP • +${event.type.baseBonusTokens} Tokens",
                                color = SlayerAmberGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tactical Choices or Resolution
                        if (resolution == null) {
                            Text(
                                text = "SELECT TACTICAL APPROACH",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(event.tacticalOptions) { option ->
                                    val canAffordStamina = playerStats.stamina >= option.staminaCost

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .clickable(enabled = canAffordStamina) { onChooseTactic(option) }
                                            .border(1.dp, if (canAffordStamina) SlayerCardBorder else Color.DarkGray, RoundedCornerShape(14.dp))
                                            .testTag("tactic_${option.type.name}"),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (canAffordStamina) SlayerSurfaceElevated else Color(0xFF1E1724)
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = when (option.type) {
                                                            TacticalApproachType.STEALTH_AMBUSH -> Icons.Default.VisibilityOff
                                                            TacticalApproachType.MYSTICAL_SEAL -> Icons.Default.AutoFixHigh
                                                            TacticalApproachType.WATCHER_DECRYPTION -> Icons.Default.MenuBook
                                                            TacticalApproachType.DISTRACTION_DECOY -> Icons.Default.Campaign
                                                            TacticalApproachType.HOLY_WARD_TRAP -> Icons.Default.LocalFireDepartment
                                                            TacticalApproachType.FULL_SLAYER_ASSAULT -> Icons.Default.SportsMartialArts
                                                        },
                                                        contentDescription = null,
                                                        tint = SlayerAmberGold,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = option.title,
                                                        color = if (canAffordStamina) TextPrimaryDark else Color.Gray,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(if (option.successRatePercent >= 85) Color(0xFF1B3B22) else SlayerDarkRed)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "${option.successRatePercent}% Est. Success",
                                                        color = if (option.successRatePercent >= 85) Color(0xFFA5D6A7) else SlayerParchment,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = option.description,
                                                color = TextSecondaryDark,
                                                fontSize = 11.sp,
                                                lineHeight = 15.sp
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Cost: ${option.requiredStat}",
                                                    color = if (canAffordStamina) SlayerWillowPurple else SlayerCrimson,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )

                                                Button(
                                                    onClick = { onChooseTactic(option) },
                                                    enabled = canAffordStamina,
                                                    colors = ButtonDefaults.buttonColors(containerColor = SlayerCrimson),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                                    modifier = Modifier.height(32.dp)
                                                ) {
                                                    Text(
                                                        text = if (canAffordStamina) "Execute Tactic" else "Exhausted",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Resolution Screen
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (resolution.isSuccess) Color(0xFF162D1C) else Color(0xFF3B151F)
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.5.dp,
                                        if (resolution.isSuccess) Color(0xFF4CAF50) else SlayerCrimson
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = if (resolution.isSuccess) "✨ TACTICAL SUCCESS!" else "⚠️ TACTICAL COMPLICATION!",
                                            color = if (resolution.isSuccess) Color(0xFFA5D6A7) else SlayerCrimson,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 16.sp
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = resolution.narrativeOutcome,
                                            color = TextPrimaryDark,
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(SlayerSurfaceDark)
                                                .padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("EARNED XP", color = TextSecondaryDark, fontSize = 10.sp)
                                                Text("+${resolution.earnedXp}", color = SlayerAmberGold, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                            }
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("EARNED TOKENS", color = TextSecondaryDark, fontSize = 10.sp)
                                                Text("+${resolution.earnedTokens}", color = SlayerAmberGold, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                if (resolution.triggersImmediateCombat && resolution.combatEnemyType != null) {
                                    Button(
                                        onClick = { onStartCombat(resolution.combatEnemyType) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(46.dp)
                                            .testTag("event_combat_button"),
                                        colors = ButtonDefaults.buttonColors(containerColor = SlayerCrimson),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.SportsMartialArts, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Engage Emergent Threat in Combat!", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                } else {
                                    Button(
                                        onClick = onDismiss,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(46.dp)
                                            .testTag("event_claim_rewards_button"),
                                        colors = ButtonDefaults.buttonColors(containerColor = SlayerAmberGold),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Claim Rewards & Return to Patrol", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
