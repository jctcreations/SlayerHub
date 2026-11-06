package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.DustingParticleOverlay
import com.example.ui.theme.*
import com.example.ui.viewmodel.CombatState

@Composable
fun CombatScreen(
    combatState: CombatState,
    combatLogs: List<CombatLogEntry>,
    arsenal: List<ArsenalItem>,
    allyCooldowns: Map<ScoobyAlly, Int>,
    onPunchCombo: () -> Unit,
    onStakeFinisher: () -> Unit,
    onDodgeParry: () -> Unit,
    onUseItem: (ArsenalItem) -> Unit,
    onCallAlly: (ScoobyAlly) -> Unit,
    onContinuePatrol: () -> Unit,
    onRetreat: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        if (!combatState.isCombatActive) {
            onContinuePatrol()
        } else {
            onRetreat()
        }
    }

    val enemy = combatState.enemy
    val isVampire = enemy.type.isVampire
    val hpPercent = enemy.currentHp.toFloat() / enemy.maxHp.coerceAtLeast(1)
    val canStakeNow = isVampire && (hpPercent <= 0.45f || enemy.isStunned)

    // Dodge button pulsing border when dodge window is active
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlayerNightDark)
            .testTag("combat_screen")
    ) {
        // Location Atmospheric Background
        val bgDrawable = if (combatState.location == PatrolLocationType.THE_HELLMOUTH) {
            com.example.R.drawable.hellmouth_bg_1793562433564
        } else {
            com.example.R.drawable.cemetery_bg_1793562410209
        }

        Image(
            painter = painterResource(id = bgDrawable),
            contentDescription = "Combat Location",
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentScale = ContentScale.Crop,
            alpha = 0.28f
        )

        // Gradient overlay for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SlayerNightDark.copy(alpha = 0.85f),
                            Color.Transparent,
                            SlayerNightDark.copy(alpha = 0.95f),
                            SlayerNightDark
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Header: Location & Retreat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onRetreat,
                    modifier = Modifier.testTag("retreat_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retreat to Patrol",
                        tint = TextSecondaryDark
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = combatState.location.displayName.uppercase(),
                        color = SlayerAmberGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )
                    if (combatState.civilianRescued && combatState.isCombatActive) {
                        Text(
                            text = "⚠️ CIVILIAN IN DANGER!",
                            color = SlayerCrimson,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                // Combo Streak indicator
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (combatState.comboStreak > 1) SlayerDarkRed else SlayerSurfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${combatState.comboStreak}x Combo",
                        color = if (combatState.comboStreak > 1) SlayerAmberGold else TextSecondaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Enemy Encounter Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("enemy_card"),
                colors = CardDefaults.cardColors(
                    containerColor = SlayerSurfaceDark.copy(alpha = 0.92f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (combatState.isDodgeWindowActive) SlayerCrimson else SlayerCardBorder
                )
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isVampire) SlayerDarkRed else Color(0xFF3E2723)
                                    )
                                    .border(1.dp, if (isVampire) SlayerCrimson else SlayerAmberGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isVampire) Icons.Default.Warning else Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = if (isVampire) SlayerCrimson else SlayerAmberGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = enemy.type.displayName,
                                    color = TextPrimaryDark,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "${enemy.type.category.name} • ${if (isVampire) "Can be Dusted" else "Banishing Required"}",
                                    color = TextSecondaryDark,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (enemy.isStunned) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SlayerAmberGold)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "STUNNED!",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Enemy HP Bar
                    val animatedEnemyHp by animateFloatAsState(
                        targetValue = hpPercent,
                        label = "enemy_hp"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Demon Health",
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${enemy.currentHp} / ${enemy.maxHp}",
                            color = SlayerCrimson,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFF2B1218))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedEnemyHp)
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(SlayerCrimson, Color(0xFFFF1744))
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Attack Incoming / Dodge Indicator
                    val attackProg = enemy.attackProgress.coerceIn(0f, 1f)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (combatState.isDodgeWindowActive) "⚠️ STRIKE INCOMING!" else "Attack Charge",
                            color = if (combatState.isDodgeWindowActive) SlayerCrimson else TextSecondaryDark,
                            fontSize = 11.sp,
                            fontWeight = if (combatState.isDodgeWindowActive) FontWeight.ExtraBold else FontWeight.Normal
                        )
                        if (combatState.isDodgeWindowActive) {
                            Text(
                                text = "PARRY WINDOW OPEN!",
                                color = SlayerAmberGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF261D12))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(attackProg)
                                .fillMaxHeight()
                                .background(
                                    if (combatState.isDodgeWindowActive) SlayerCrimson else SlayerAmberGold
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Weakness: ${enemy.type.weakness}",
                        color = SlayerWillowPurple,
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Combat Log Stream
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xCC11091C)
                ),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    reverseLayout = false
                ) {
                    itemsIndexed(combatLogs, key = { index, log -> "${log.id}_$index" }) { _, log ->
                        val textColor = when {
                            log.isCritical -> SlayerAmberGold
                            log.isBuffyQuote -> SlayerParchment
                            log.isEnemy -> SlayerCrimson
                            else -> TextPrimaryDark
                        }
                        Text(
                            text = log.text,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = if (log.isCritical) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Slayer Combat Action Deck
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Row 1: Primary Combos (Punch & Dodge/Parry)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onPunchCombo,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("punch_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SlayerCrimson
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsMartialArts,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Punch & Kick",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Dodge / Parry Button
                    val dodgeBorderModifier = if (combatState.isDodgeWindowActive) {
                        Modifier.border(
                            2.dp,
                            SlayerAmberGold.copy(alpha = pulseAlpha),
                            RoundedCornerShape(10.dp)
                        )
                    } else Modifier

                    Button(
                        onClick = onDodgeParry,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .then(dodgeBorderModifier)
                            .testTag("dodge_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (combatState.isDodgeWindowActive) SlayerAmberGold else SlayerSurfaceElevated
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = if (combatState.isDodgeWindowActive) Color.Black else SlayerAmberGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (combatState.isDodgeWindowActive) "PARRY NOW!" else "Slayer Dodge",
                            color = if (combatState.isDodgeWindowActive) Color.Black else TextPrimaryDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Row 2: Iconic Stake Heart Finisher (Mr. Pointy)
                if (isVampire) {
                    val stakeBg = if (canStakeNow) {
                        Brush.horizontalGradient(listOf(SlayerDarkRed, SlayerCrimson, SlayerDarkRed))
                    } else {
                        Brush.horizontalGradient(listOf(SlayerSurfaceElevated, SlayerSurfaceElevated))
                    }

                    Button(
                        onClick = onStakeFinisher,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .border(
                                width = if (canStakeNow) 1.5.dp else 1.dp,
                                color = if (canStakeNow) SlayerAmberGold else SlayerCardBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .testTag("stake_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(stakeBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Navigation,
                                    contentDescription = null,
                                    tint = if (canStakeNow) SlayerAmberGold else TextSecondaryDark,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (canStakeNow) "🔥 STAKE TO THE HEART (DUST HIM!)" else "Stake Heart (Weaken Vampire HP)",
                                    color = if (canStakeNow) Color.White else TextSecondaryDark,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                // Row 3: Arsenal Quick Belt (Holy water, Crossbow, Medkit, Scythe)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val quickItems = arsenal.filter { it.isUnlocked && it.type != ArsenalItemType.STAKE }
                    quickItems.forEach { item ->
                        val hasCharges = item.currentCharges > 0
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(enabled = hasCharges) { onUseItem(item) }
                                .border(
                                    1.dp,
                                    if (hasCharges) SlayerCardBorder else Color.DarkGray,
                                    RoundedCornerShape(8.dp)
                                )
                                .testTag("use_item_${item.id}"),
                            color = if (hasCharges) SlayerSurfaceElevated else Color(0xFF1E1E1E)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = item.name.substringBefore(" ("),
                                    color = if (hasCharges) TextPrimaryDark else TextSecondaryDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${item.currentCharges} left",
                                    color = if (hasCharges) SlayerAmberGold else Color.Gray,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }

                // Row 4: Scooby Gang Summon Strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ScoobyAlly.values().forEach { ally ->
                        val cooldown = allyCooldowns[ally] ?: 0
                        val isReady = cooldown == 0

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(enabled = isReady) { onCallAlly(ally) }
                                .border(
                                    1.dp,
                                    if (isReady) SlayerWillowPurple else Color.DarkGray,
                                    RoundedCornerShape(8.dp)
                                )
                                .testTag("ally_${ally.name.lowercase()}"),
                            color = if (isReady) Color(0xFF2D163F) else Color(0xFF191220)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isReady) ally.displayName.substringBefore(" ") else "${ally.displayName.substringBefore(" ")} (${cooldown}s)",
                                    color = if (isReady) SlayerParchment else Color.Gray,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Dusting Particle Overlay Animation
        if (combatState.isDustedAnimation) {
            DustingParticleOverlay()
        }

        // Victory Dialog / Overlay
        if (combatState.isVictory) {
            AlertDialog(
                onDismissRequest = onContinuePatrol,
                containerColor = SlayerSurfaceDark,
                titleContentColor = SlayerAmberGold,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (isVampire) "VAMPIRE DUSTED!" else "DEMON BANISHED!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${enemy.type.displayName} was sent back to the dark.",
                            color = TextPrimaryDark,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SlayerSurfaceElevated)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("XP GAINED", color = TextSecondaryDark, fontSize = 10.sp)
                                Text("+${combatState.earnedXp}", color = SlayerAmberGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("TOKENS", color = TextSecondaryDark, fontSize = 10.sp)
                                Text("+${combatState.earnedTokens}", color = SlayerAmberGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }

                        if (combatState.civilianRescued) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "🌟 Civilian student rescued successfully!",
                                color = SlayerWillowPurple,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = onContinuePatrol,
                        colors = ButtonDefaults.buttonColors(containerColor = SlayerCrimson),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("continue_patrol_button")
                    ) {
                        Text("Continue Sunnydale Patrol", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Defeat Dialog
        if (combatState.isDefeat) {
            AlertDialog(
                onDismissRequest = onContinuePatrol,
                containerColor = SlayerSurfaceDark,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Text(
                        text = "OVERWHELMED IN BATTLE",
                        color = SlayerCrimson,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                },
                text = {
                    Text(
                        text = "Buffy collapsed in the shadows. The Scooby Gang managed to rescue you and bring you back to the Sunnydale High library to heal.",
                        color = TextPrimaryDark,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = onContinuePatrol,
                        colors = ButtonDefaults.buttonColors(containerColor = SlayerWillowPurple),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("defeat_ok_button")
                    ) {
                        Text("Return to Giles' Library", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}
