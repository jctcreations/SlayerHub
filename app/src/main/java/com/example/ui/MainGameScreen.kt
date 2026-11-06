package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.NightPatrolEventDialog
import com.example.ui.components.SlayerHeader
import com.example.ui.components.SlayerRankUpSnackbar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.GameScreen
import com.example.ui.viewmodel.GameViewModel

@Composable
fun MainGameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val playerStats by viewModel.playerStats.collectAsStateWithLifecycle()
    val combatState by viewModel.combatState.collectAsStateWithLifecycle()
    val combatLogs by viewModel.combatLogs.collectAsStateWithLifecycle()
    val arsenal by viewModel.arsenal.collectAsStateWithLifecycle()
    val bestiary by viewModel.bestiary.collectAsStateWithLifecycle()
    val allyCooldowns by viewModel.allyCooldowns.collectAsStateWithLifecycle()
    val isSfxEnabled by viewModel.isSfxEnabled.collectAsStateWithLifecycle()
    val isAmbianceEnabled by viewModel.isAmbianceEnabled.collectAsStateWithLifecycle()
    val triviaState by viewModel.triviaState.collectAsStateWithLifecycle()
    val activeNightEvent by viewModel.activeNightEvent.collectAsStateWithLifecycle()
    val eventResolution by viewModel.eventResolution.collectAsStateWithLifecycle()
    val rankUpAlert by viewModel.rankUpAlert.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlayerNightDark)
    ) {
        if (combatState != null) {
            // Full combat interface
            CombatScreen(
                combatState = combatState!!,
                combatLogs = combatLogs,
                arsenal = arsenal,
                allyCooldowns = allyCooldowns,
                onPunchCombo = viewModel::onPlayerPunchCombo,
                onStakeFinisher = viewModel::onPlayerStakeFinisher,
                onDodgeParry = viewModel::onPlayerDodgeParry,
                onUseItem = viewModel::onUseArsenalItem,
                onCallAlly = viewModel::callScoobyAlly,
                onContinuePatrol = viewModel::exitCombatAndReturnToPatrol,
                onRetreat = viewModel::exitCombatAndReturnToPatrol,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            )
        } else {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SlayerNightDark),
                topBar = {
                    SlayerHeader(
                        playerStats = playerStats,
                        isSfxEnabled = isSfxEnabled,
                        isAmbianceEnabled = isAmbianceEnabled,
                        onToggleSfx = viewModel::toggleSfx,
                        onToggleAmbiance = viewModel::toggleAmbiance,
                        modifier = Modifier.statusBarsPadding()
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = SlayerNightDark,
                        contentColor = TextPrimaryDark,
                        tonalElevation = 8.dp,
                        windowInsets = WindowInsets.navigationBars,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        NavigationBarItem(
                            selected = currentScreen == GameScreen.PATROL,
                            onClick = { viewModel.setScreen(GameScreen.PATROL) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = "Patrol"
                                )
                            },
                            label = { Text("Patrol") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SlayerAmberGold,
                                selectedTextColor = SlayerAmberGold,
                                indicatorColor = SlayerDarkRed,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("nav_patrol")
                        )

                        NavigationBarItem(
                            selected = currentScreen == GameScreen.STATUS,
                            onClick = { viewModel.setScreen(GameScreen.STATUS) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Status"
                                )
                            },
                            label = { Text("Status") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SlayerAmberGold,
                                selectedTextColor = SlayerAmberGold,
                                indicatorColor = SlayerDarkRed,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("nav_status")
                        )

                        NavigationBarItem(
                            selected = currentScreen == GameScreen.TRIVIA,
                            onClick = { viewModel.setScreen(GameScreen.TRIVIA) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "Trivia"
                                )
                            },
                            label = { Text("Trivia") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SlayerAmberGold,
                                selectedTextColor = SlayerAmberGold,
                                indicatorColor = SlayerDarkRed,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("nav_trivia")
                        )

                        NavigationBarItem(
                            selected = currentScreen == GameScreen.LIBRARY || currentScreen == GameScreen.BESTIARY,
                            onClick = { viewModel.setScreen(GameScreen.LIBRARY) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = "Library"
                                )
                            },
                            label = { Text("Library") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SlayerAmberGold,
                                selectedTextColor = SlayerAmberGold,
                                indicatorColor = SlayerDarkRed,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("nav_library")
                        )

                        NavigationBarItem(
                            selected = currentScreen == GameScreen.ARSENAL,
                            onClick = { viewModel.setScreen(GameScreen.ARSENAL) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Inventory2,
                                    contentDescription = "Arsenal"
                                )
                            },
                            label = { Text("Arsenal") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SlayerAmberGold,
                                selectedTextColor = SlayerAmberGold,
                                indicatorColor = SlayerDarkRed,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("nav_arsenal")
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(SlayerNightDark)
                ) {
                    when (currentScreen) {
                        GameScreen.PATROL -> {
                            PatrolScreen(
                                playerStats = playerStats,
                                onSelectLocation = viewModel::startPatrolEncounter,
                                onOpenStatusScreen = { viewModel.setScreen(GameScreen.STATUS) },
                                onTriggerEmergencyEvent = { viewModel.triggerRandomNightEvent() }
                            )
                        }
                        GameScreen.STATUS -> {
                            StatusScreen(
                                playerStats = playerStats,
                                isSfxEnabled = isSfxEnabled,
                                isAmbianceEnabled = isAmbianceEnabled,
                                onToggleSfx = viewModel::toggleSfx,
                                onToggleAmbiance = viewModel::toggleAmbiance
                            )
                        }
                        GameScreen.TRIVIA -> {
                            TriviaScreen(
                                triviaState = triviaState,
                                playerStats = playerStats,
                                onSelectOption = viewModel::submitTriviaAnswer,
                                onNextQuestion = viewModel::loadNextTriviaQuestion
                            )
                        }
                        GameScreen.LIBRARY -> {
                            LibraryScreen(
                                playerStats = playerStats,
                                onRestAndHeal = viewModel::healAtLibrary,
                                onUpgradeStat = viewModel::upgradeStat,
                                onOpenBestiary = { viewModel.setScreen(GameScreen.BESTIARY) },
                                onOpenTrivia = { viewModel.setScreen(GameScreen.TRIVIA) }
                            )
                        }
                        GameScreen.ARSENAL -> {
                            ArsenalScreen(
                                playerStats = playerStats,
                                arsenal = arsenal,
                                onToggleEquipOrUnlock = viewModel::unlockOrEquipItem,
                                onRestock = viewModel::restockItem
                            )
                        }
                        GameScreen.BESTIARY -> {
                            BestiaryScreen(
                                bestiary = bestiary,
                                onBackToLibrary = { viewModel.setScreen(GameScreen.LIBRARY) }
                            )
                        }
                    }
                }
            }

            // Night Patrol Event Modal Dialog
            activeNightEvent?.let { event ->
                BackHandler {
                    viewModel.dismissNightEvent()
                }
                NightPatrolEventDialog(
                    event = event,
                    resolution = eventResolution,
                    playerStats = playerStats,
                    onChooseTactic = viewModel::resolveNightEvent,
                    onDismiss = viewModel::dismissNightEvent,
                    onStartCombat = viewModel::startCombatFromEvent
                )
            }

            // System Back navigation for secondary screens
            if (activeNightEvent == null && currentScreen != GameScreen.PATROL) {
                BackHandler {
                    if (currentScreen == GameScreen.BESTIARY) {
                        viewModel.setScreen(GameScreen.LIBRARY)
                    } else {
                        viewModel.setScreen(GameScreen.PATROL)
                    }
                }
            }
        }

        // Top-level Floating Slayer Rank Promotion Alert Snackbar
        SlayerRankUpSnackbar(
            alert = rankUpAlert,
            onViewStatus = {
                if (combatState != null) {
                    viewModel.exitCombatAndReturnToPatrol()
                }
                viewModel.setScreen(GameScreen.STATUS)
            },
            onDismiss = viewModel::dismissRankUpAlert,
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopCenter)
        )
    }
}
