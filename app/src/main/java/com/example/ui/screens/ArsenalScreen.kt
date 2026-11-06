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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ArsenalItem
import com.example.model.ArsenalItemType
import com.example.model.PlayerStats
import com.example.ui.theme.*

@Composable
fun ArsenalScreen(
    playerStats: PlayerStats,
    arsenal: List<ArsenalItem>,
    onToggleEquipOrUnlock: (String) -> Unit,
    onRestock: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlayerNightDark)
            .padding(horizontal = 16.dp)
            .testTag("arsenal_screen"),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("magic_box_header"),
                colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                shape = RoundedCornerShape(16.dp),
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SlayerDarkRed)
                            .border(1.dp, SlayerAmberGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = SlayerAmberGold,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "THE MAGIC BOX & SLAYER TRUNK",
                            color = SlayerAmberGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Equip weapons, stakes, and blessed relics for night patrol.",
                            color = TextSecondaryDark,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "WEAPONS & WOODEN STAKES",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }

        val stakesAndWeapons = arsenal.filter { it.type == ArsenalItemType.STAKE || it.type == ArsenalItemType.RANGED }
        items(stakesAndWeapons, key = { it.id }) { item ->
            ArsenalItemCard(
                item = item,
                playerTokens = playerStats.occultTokens,
                onToggleEquipOrUnlock = { onToggleEquipOrUnlock(item.id) },
                onRestock = { onRestock(item.id, 15) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "CONSUMABLES & MEDICINE",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }

        val consumables = arsenal.filter { it.type == ArsenalItemType.MAGIC_HOLY || it.type == ArsenalItemType.MEDICINE }
        items(consumables, key = { it.id }) { item ->
            ArsenalItemCard(
                item = item,
                playerTokens = playerStats.occultTokens,
                onToggleEquipOrUnlock = { onToggleEquipOrUnlock(item.id) },
                onRestock = { onRestock(item.id, 20) }
            )
        }
    }
}

@Composable
private fun ArsenalItemCard(
    item: ArsenalItem,
    playerTokens: Int,
    onToggleEquipOrUnlock: () -> Unit,
    onRestock: () -> Unit
) {
    val canAffordUnlock = playerTokens >= item.costTokens

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("item_card_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isEquipped) Color(0xFF281530) else SlayerSurfaceDark
        ),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (item.isEquipped) SlayerAmberGold else SlayerCardBorder
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
                        imageVector = when (item.type) {
                            ArsenalItemType.STAKE -> Icons.Default.Navigation
                            ArsenalItemType.RANGED -> Icons.Default.Send
                            ArsenalItemType.MAGIC_HOLY -> Icons.Default.LocalFireDepartment
                            ArsenalItemType.MEDICINE -> Icons.Default.Medication
                        },
                        contentDescription = null,
                        tint = if (item.isEquipped) SlayerAmberGold else SlayerCrimson,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.name,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                if (item.isUnlocked) {
                    if (item.type == ArsenalItemType.STAKE) {
                        Text(
                            text = if (item.isEquipped) "EQUIPPED" else "READY",
                            color = if (item.isEquipped) SlayerAmberGold else TextSecondaryDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "${item.currentCharges}/${item.maxCharges} Charges",
                            color = if (item.currentCharges > 0) SlayerAmberGold else SlayerCrimson,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.description,
                color = TextSecondaryDark,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!item.isUnlocked) {
                    Button(
                        onClick = onToggleEquipOrUnlock,
                        enabled = canAffordUnlock,
                        colors = ButtonDefaults.buttonColors(containerColor = SlayerCrimson),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("unlock_${item.id}")
                    ) {
                        Text(
                            text = "Unlock (${item.costTokens} tk)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    if (item.maxCharges < 999 && item.currentCharges < item.maxCharges) {
                        OutlinedButton(
                            onClick = onRestock,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SlayerAmberGold),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("restock_${item.id}")
                        ) {
                            Text("Restock (20 tk)", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    if (item.type == ArsenalItemType.STAKE) {
                        Button(
                            onClick = onToggleEquipOrUnlock,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (item.isEquipped) SlayerSurfaceElevated else SlayerCrimson
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("equip_${item.id}")
                        ) {
                            Text(
                                text = if (item.isEquipped) "Unequip" else "Equip Stake",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
