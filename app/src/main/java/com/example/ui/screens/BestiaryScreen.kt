package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BestiaryEntry
import com.example.model.EnemyCategory
import com.example.model.EnemyType
import com.example.ui.theme.*

@Composable
fun BestiaryScreen(
    bestiary: Map<EnemyType, BestiaryEntry>,
    onBackToLibrary: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlayerNightDark)
            .padding(horizontal = 16.dp)
            .testTag("bestiary_screen"),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bestiary_tome_header"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF261414)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SlayerAmberGold)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToLibrary,
                        modifier = Modifier.testTag("bestiary_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Library",
                            tint = SlayerAmberGold
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SlayerDarkRed)
                            .border(1.dp, SlayerAmberGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            tint = SlayerAmberGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "VAMPYR GRIMOIRE & BESTIARY",
                            color = SlayerAmberGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Watcher's catalog of Hellmouth creatures and occult weaknesses.",
                            color = SlayerParchment,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "SUNNYDALE CREATURE RECORD",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }

        items(EnemyType.values(), key = { it.name }) { type ->
            val entry = bestiary[type]
            BestiaryEntryCard(type = type, entry = entry)
        }
    }
}

@Composable
private fun BestiaryEntryCard(
    type: EnemyType,
    entry: BestiaryEntry?
) {
    val isEncountered = entry?.isEncountered == true
    val killCount = entry?.timesDefeated ?: 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bestiary_card_${type.name.lowercase()}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isEncountered) SlayerSurfaceDark else Color(0xFF140E1B)
        ),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isEncountered) SlayerCardBorder else Color(0xFF26192F)
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
                    Icon(
                        imageVector = if (type.isVampire) Icons.Default.Warning else Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = if (isEncountered) (if (type.isVampire) SlayerCrimson else SlayerAmberGold) else Color.DarkGray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEncountered) type.displayName else "??? [Unknown Fiend]",
                        color = if (isEncountered) TextPrimaryDark else Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                if (isEncountered) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (killCount > 0) SlayerDarkRed else SlayerSurfaceElevated)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$killCount Slain",
                            color = if (killCount > 0) SlayerAmberGold else TextSecondaryDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (isEncountered) {
                Text(
                    text = type.lore,
                    color = TextSecondaryDark,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Vulnerability: ${type.weakness}",
                    color = SlayerWillowPurple,
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic
                )
            } else {
                Text(
                    text = "Patrol deeper into the Hellmouth or Sunnydale districts to uncover this creature's lore.",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}
