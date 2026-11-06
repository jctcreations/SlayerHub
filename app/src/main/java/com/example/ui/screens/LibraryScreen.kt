package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerStats
import com.example.ui.theme.*

@Composable
fun LibraryScreen(
    playerStats: PlayerStats,
    onRestAndHeal: () -> Unit,
    onUpgradeStat: (String, Int) -> Unit,
    onOpenBestiary: () -> Unit = {},
    onOpenTrivia: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var toastMessage by remember { mutableStateOf<String?>(null) }

    val gilesLoreQuotes = remember {
        listOf(
            "Buffy, according to the Pergamum Codex, vampires become notably bolder when the harvest approaches. Keep your wooden stakes sharpened.",
            "I've cross-referenced three medieval Bavarian grimoires. Demons of the Polgara genus wield forearm bone skewers. Stay nimble.",
            "A cup of hot Earl Grey tea and proper stretching before night patrol. Discipline is as vital as raw Slayer strength.",
            "Willow has fortified the library perimeter with rowan wood and salt. We are safe here to research and train.",
            "Xander claims to have spotted odd cloaked figures near the school boiler room. Be vigilant on your rounds."
        )
    }

    val currentGilesQuote = remember { gilesLoreQuotes.random() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlayerNightDark)
            .padding(horizontal = 16.dp)
            .testTag("library_screen"),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Image: Giles' Library Sanctuary
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .testTag("library_hero_banner"),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = com.example.R.drawable.library_bg_1793562421611),
                        contentDescription = "Sunnydale High School Library",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, SlayerNightDark.copy(alpha = 0.90f))
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "GILES' RESEARCH SANCTUARY",
                            color = SlayerAmberGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Sunnydale High School Library • Watcher Headquarters",
                            color = SlayerParchment,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Giles' Dialogue & Rest Area
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("giles_dialogue_card"),
                colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3E2723))
                                .border(1.dp, SlayerAmberGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = SlayerAmberGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Rupert Giles (The Watcher)",
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "*cleans spectacles with handkerchief*",
                                color = TextSecondaryDark,
                                fontSize = 11.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "\"$currentGilesQuote\"",
                        color = SlayerParchment,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onRestAndHeal()
                            toastMessage = "Health fully restored! Tea and bandages applied."
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("heal_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalCafe,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Rest & Patch Wounds (Restore Full HP)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    if (toastMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = toastMessage ?: "",
                            color = Color(0xFF81C784),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onOpenBestiary,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("library_open_bestiary"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SlayerAmberGold),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlayerAmberGold)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Bestiary Tome", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onOpenTrivia,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("library_open_trivia"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SlayerWillowPurple),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlayerWillowPurple)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Watcher Trivia", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Slayer Training Section
        item {
            Text(
                text = "SLAYER COMBAT TRAINING",
                color = TextPrimaryDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // Stat Upgrades
        item {
            TrainingItemCard(
                title = "Martial Arts & Kickboxing",
                subtitle = "Current Power: +${playerStats.martialPower} DMG",
                description = "Increase raw strike damage on punches, roundhouses, and combos.",
                costTokens = 40,
                playerTokens = playerStats.occultTokens,
                onUpgrade = { onUpgradeStat("MARTIAL", 40) },
                icon = Icons.Default.SportsMartialArts,
                tag = "upgrade_martial"
            )
        }

        item {
            TrainingItemCard(
                title = "Slayer Reflexes & Dodging",
                subtitle = "Current Parry Bonus: +${playerStats.agilityParryBonus}% Crit",
                description = "Widen parry windows and increase critical hit chance when countering.",
                costTokens = 45,
                playerTokens = playerStats.occultTokens,
                onUpgrade = { onUpgradeStat("AGILITY", 45) },
                icon = Icons.Default.Shield,
                tag = "upgrade_agility"
            )
        }

        item {
            TrainingItemCard(
                title = "Chosen One Constitution",
                subtitle = "Current Max HP: ${playerStats.maxHp} HP",
                description = "Enhance the Slayer's supernatural physical endurance (+25 Max HP).",
                costTokens = 50,
                playerTokens = playerStats.occultTokens,
                onUpgrade = { onUpgradeStat("VITALITY", 50) },
                icon = Icons.Default.Favorite,
                tag = "upgrade_vitality"
            )
        }
    }
}

@Composable
private fun TrainingItemCard(
    title: String,
    subtitle: String,
    description: String,
    costTokens: Int,
    playerTokens: Int,
    onUpgrade: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tag: String
) {
    val canAfford = playerTokens >= costTokens

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag),
        colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(SlayerSurfaceElevated)
                    .border(1.dp, SlayerCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SlayerAmberGold,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = subtitle,
                    color = SlayerAmberGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onUpgrade,
                enabled = canAfford,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SlayerCrimson,
                    disabledContainerColor = Color(0xFF2B1D28)
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Train",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$costTokens tk",
                        fontSize = 10.sp,
                        color = if (canAfford) SlayerParchment else Color.Gray
                    )
                }
            }
        }
    }
}
