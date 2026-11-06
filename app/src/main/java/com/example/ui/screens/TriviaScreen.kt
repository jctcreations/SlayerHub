package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerStats
import com.example.model.TriviaDifficulty
import com.example.model.TriviaState
import com.example.ui.theme.*

@Composable
fun TriviaScreen(
    triviaState: TriviaState,
    playerStats: PlayerStats,
    onSelectOption: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val question = triviaState.currentQuestion

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlayerNightDark)
            .padding(horizontal = 16.dp)
            .testTag("trivia_screen"),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Trivia Header with Escalating Difficulty Status
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("trivia_header_card"),
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "WATCHER'S OCCULT EXAM",
                                color = SlayerAmberGold,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Buffy Lore Trivia • Escalating Challenge",
                                color = TextSecondaryDark,
                                fontSize = 12.sp
                            )
                        }

                        // Streak Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (triviaState.currentStreak > 0) SlayerDarkRed else SlayerSurfaceElevated)
                                .border(1.dp, if (triviaState.currentStreak > 0) SlayerCrimson else SlayerCardBorder, RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔥 ${triviaState.currentStreak} Streak",
                                color = if (triviaState.currentStreak > 0) SlayerAmberGold else TextSecondaryDark,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Escalating Tier Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SlayerSurfaceElevated)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Difficulty Tier:",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${triviaState.currentTier.title} (Tier ${triviaState.currentTier.tier})",
                                color = when (triviaState.currentTier) {
                                    TriviaDifficulty.HELLMOUTH_MASTER -> SlayerCrimson
                                    TriviaDifficulty.WATCHER_COUNCIL -> SlayerAmberGold
                                    TriviaDifficulty.WATCHER_TRAINEE -> SlayerWillowPurple
                                    TriviaDifficulty.SCOOBY -> Color(0xFF64B5F6)
                                    TriviaDifficulty.NOVICE -> Color(0xFF81C784)
                                },
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Per Correct Answer:",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "+${triviaState.currentTier.xpReward} XP • +${triviaState.currentTier.tokenReward} Tokens",
                                color = SlayerAmberGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Questions get progressively deeper and harder with every correct streak!",
                        color = SlayerParchment,
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }

        // Active Question Card
        if (question != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("question_card"),
                    colors = CardDefaults.cardColors(containerColor = SlayerSurfaceDark),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlayerCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "QUESTION #${question.id}",
                                color = TextSecondaryDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = question.difficulty.name.replace("_", " "),
                                color = SlayerWillowPurple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = question.question,
                            color = TextPrimaryDark,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 4 Answer Options
                        question.options.forEachIndexed { index, option ->
                            val isSelected = triviaState.selectedOptionIndex == index
                            val isCorrectAnswer = index == question.correctIndex
                            val isRevealed = triviaState.isAnswerRevealed

                            val optionBorder = when {
                                isRevealed && isCorrectAnswer -> Color(0xFF4CAF50)
                                isRevealed && isSelected && !isCorrectAnswer -> SlayerCrimson
                                isSelected -> SlayerAmberGold
                                else -> SlayerCardBorder
                            }

                            val optionBg = when {
                                isRevealed && isCorrectAnswer -> Color(0xFF1B3B22)
                                isRevealed && isSelected && !isCorrectAnswer -> Color(0xFF3E171E)
                                isSelected -> SlayerDarkRed
                                else -> SlayerSurfaceElevated
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(enabled = !isRevealed) { onSelectOption(index) }
                                    .border(1.5.dp, optionBorder, RoundedCornerShape(10.dp))
                                    .testTag("option_$index"),
                                color = optionBg
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF221730)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = when (index) {
                                                0 -> "A"
                                                1 -> "B"
                                                2 -> "C"
                                                else -> "D"
                                            },
                                            color = SlayerParchment,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = option,
                                        color = TextPrimaryDark,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (isRevealed) {
                                        if (isCorrectAnswer) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Correct",
                                                tint = Color(0xFF4CAF50),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Cancel,
                                                contentDescription = "Incorrect",
                                                tint = SlayerCrimson,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Explanation after answering
                        AnimatedVisibility(visible = triviaState.isAnswerRevealed) {
                            Column(modifier = Modifier.padding(top = 14.dp)) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (triviaState.isCorrect) Color(0xFF1B3B22) else Color(0xFF331B2B)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = if (triviaState.isCorrect) "✨ SPLENDID RECALL, BUFFY!" else "❌ NOT QUITE ACCURATE",
                                            color = if (triviaState.isCorrect) Color(0xFFA5D6A7) else SlayerCrimson,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 12.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = question.explanation,
                                            color = TextPrimaryDark,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = onNextQuestion,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("next_question_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = SlayerCrimson),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (triviaState.isCorrect) "Next Question (Difficulty Escales)" else "Next Question (Try Again)",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
