package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameRepository
import com.example.data.NightPatrolEventFactory
import com.example.data.TriviaBank
import com.example.model.*
import com.example.util.SoundManager
import com.example.util.VibrationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class GameScreen {
    PATROL,
    STATUS,
    TRIVIA,
    LIBRARY,
    ARSENAL,
    BESTIARY
}

data class CombatState(
    val enemy: Enemy,
    val location: PatrolLocationType,
    val isCombatActive: Boolean = true,
    val isPlayerTurnBlocked: Boolean = false,
    val isDodgeWindowActive: Boolean = false,
    val comboStreak: Int = 0,
    val watcherResearchBuffActive: Boolean = false,
    val lastDamageDealt: Int? = null,
    val lastDamageReceived: Int? = null,
    val isDustedAnimation: Boolean = false,
    val isVictory: Boolean = false,
    val isDefeat: Boolean = false,
    val earnedXp: Int = 0,
    val earnedTokens: Int = 0,
    val civilianRescued: Boolean = false
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GameRepository(application)

    val playerStats: StateFlow<PlayerStats> = repository.playerStats
    val arsenal: StateFlow<List<ArsenalItem>> = repository.arsenal
    val bestiary: StateFlow<Map<EnemyType, BestiaryEntry>> = repository.bestiary

    private val _currentScreen = MutableStateFlow(GameScreen.PATROL)
    val currentScreen: StateFlow<GameScreen> = _currentScreen.asStateFlow()

    private val _combatState = MutableStateFlow<CombatState?>(null)
    val combatState: StateFlow<CombatState?> = _combatState.asStateFlow()

    private val _combatLogs = MutableStateFlow<List<CombatLogEntry>>(emptyList())
    val combatLogs: StateFlow<List<CombatLogEntry>> = _combatLogs.asStateFlow()

    // Ally cooldown timers in seconds
    private val _allyCooldowns = MutableStateFlow<Map<ScoobyAlly, Int>>(emptyMap())
    val allyCooldowns: StateFlow<Map<ScoobyAlly, Int>> = _allyCooldowns.asStateFlow()

    // Sound manager states
    val isSfxEnabled: StateFlow<Boolean> = SoundManager.isSfxEnabled
    val isAmbianceEnabled: StateFlow<Boolean> = SoundManager.isAmbianceEnabled

    // Trivia mode state
    private val _triviaState = MutableStateFlow(TriviaState())
    val triviaState: StateFlow<TriviaState> = _triviaState.asStateFlow()
    private val askedQuestionIds = mutableSetOf<Int>()

    // Night Patrol Event System
    private val _activeNightEvent = MutableStateFlow<NightPatrolEvent?>(null)
    val activeNightEvent: StateFlow<NightPatrolEvent?> = _activeNightEvent.asStateFlow()

    private val _eventResolution = MutableStateFlow<EventResolutionResult?>(null)
    val eventResolution: StateFlow<EventResolutionResult?> = _eventResolution.asStateFlow()

    // Slayer Rank Promotion Alert (Snackbar / Notification)
    private val _rankUpAlert = MutableStateFlow<RankUpAlert?>(null)
    val rankUpAlert: StateFlow<RankUpAlert?> = _rankUpAlert.asStateFlow()

    private var enemyAttackJob: Job? = null
    private var cooldownTickerJob: Job? = null
    private var staminaRegenJob: Job? = null

    private val buffyQuotes = listOf(
        "If the apocalypse comes, beep me.",
        "I may be dead, but I'm still pretty.",
        "You have a lot to learn about women. Also, about not getting dusted.",
        "What is your childhood trauma?!",
        "Strong, fast, cute... I'm a triple threat.",
        "Does anyone else want to tell me how to do my job before I stake you?",
        "Power. I have it. They don't.",
        "I'm the thing that monsters have nightmares about.",
        "Cemetery patrol on a Friday night... high school memories in the making!"
    )

    init {
        startAllyCooldownTicker()
        startStaminaRegenLoop()
        SoundManager.startAmbiance()
        loadNextTriviaQuestion()
    }

    fun setScreen(screen: GameScreen) {
        _currentScreen.value = screen
    }

    fun toggleSfx() {
        SoundManager.toggleSfx()
    }

    fun toggleAmbiance() {
        SoundManager.toggleAmbiance()
    }

    fun dismissRankUpAlert() {
        _rankUpAlert.value = null
    }

    private fun triggerRankUpAlert(alert: RankUpAlert) {
        _rankUpAlert.value = alert
        SoundManager.playLevelUp()
        VibrationHelper.vibrateCrit(getApplication())
    }

    private fun startStaminaRegenLoop() {
        staminaRegenJob?.cancel()
        staminaRegenJob = viewModelScope.launch {
            while (isActive) {
                delay(2500L)
                repository.modifyStamina(3)
            }
        }
    }

    // --- TRIVIA MODE (Escalating Difficulty) ---

    fun loadNextTriviaQuestion() {
        val currentStreak = playerStats.value.triviaStreak
        val question = TriviaBank.getQuestionForStreak(currentStreak, askedQuestionIds)
        askedQuestionIds.add(question.id)
        if (askedQuestionIds.size > 20) {
            askedQuestionIds.clear()
        }

        val targetTier = when {
            currentStreak >= TriviaDifficulty.HELLMOUTH_MASTER.minStreak -> TriviaDifficulty.HELLMOUTH_MASTER
            currentStreak >= TriviaDifficulty.WATCHER_COUNCIL.minStreak -> TriviaDifficulty.WATCHER_COUNCIL
            currentStreak >= TriviaDifficulty.WATCHER_TRAINEE.minStreak -> TriviaDifficulty.WATCHER_TRAINEE
            currentStreak >= TriviaDifficulty.SCOOBY.minStreak -> TriviaDifficulty.SCOOBY
            else -> TriviaDifficulty.NOVICE
        }

        _triviaState.update { current ->
            current.copy(
                currentQuestion = question,
                selectedOptionIndex = null,
                isAnswerRevealed = false,
                isCorrect = false,
                currentStreak = currentStreak,
                bestStreak = playerStats.value.bestTriviaStreak,
                currentTier = targetTier
            )
        }
    }

    fun submitTriviaAnswer(selectedIndex: Int) {
        val current = _triviaState.value
        val question = current.currentQuestion ?: return
        if (current.isAnswerRevealed) return

        val isRight = selectedIndex == question.correctIndex
        val xpGain = if (isRight) question.difficulty.xpReward else 0
        val tokenGain = if (isRight) question.difficulty.tokenReward else 0

        if (isRight) {
            SoundManager.playTriviaCorrect()
            VibrationHelper.vibrateCrit(getApplication())
        } else {
            SoundManager.playTriviaWrong()
            VibrationHelper.vibrateHit(getApplication())
        }

        val alert = repository.recordTriviaResult(isRight, xpGain, tokenGain)
        if (alert != null) {
            triggerRankUpAlert(alert)
        }

        val updatedStats = playerStats.value
        _triviaState.update {
            it.copy(
                selectedOptionIndex = selectedIndex,
                isAnswerRevealed = true,
                isCorrect = isRight,
                currentStreak = updatedStats.triviaStreak,
                bestStreak = updatedStats.bestTriviaStreak,
                totalAnswered = it.totalAnswered + 1,
                totalCorrect = if (isRight) it.totalCorrect + 1 else it.totalCorrect
            )
        }
    }

    // --- NIGHT PATROL EVENTS ---

    fun triggerRandomNightEvent(specificType: NightEventType? = null) {
        val event = if (specificType != null) {
            NightPatrolEventFactory.createEvent(specificType)
        } else {
            NightPatrolEventFactory.generateRandomEvent()
        }
        _eventResolution.value = null
        _activeNightEvent.value = event
        SoundManager.playParry()
        VibrationHelper.vibrateHit(getApplication())
    }

    fun resolveNightEvent(option: TacticalOption) {
        val event = _activeNightEvent.value ?: return

        // Deduct stamina cost
        repository.modifyStamina(-option.staminaCost)

        val stats = playerStats.value
        // Factor in Slayer Agility and Martial attributes to success rate
        var rollChance = option.successRatePercent
        if (stats.agilityParryBonus > 8) rollChance += 6
        if (stats.martialPower > 14) rollChance += 6
        rollChance = rollChance.coerceIn(40, 98)

        val roll = Random.nextInt(100)
        val isSuccess = roll < rollChance

        if (isSuccess) {
            val earnedXp = (event.type.baseBonusXp * option.bonusRewardMultiplier).toInt()
            val earnedTokens = (event.type.baseBonusTokens * option.bonusRewardMultiplier).toInt()
            val alert = repository.addXpAndTokens(earnedXp, earnedTokens)
            if (alert != null) {
                triggerRankUpAlert(alert)
            }

            when (event.type) {
                NightEventType.GRAVEYARD_STAKEOUT, NightEventType.VAMPIRE_NEST_RAID -> {
                    repeat(3) { repository.recordEnemyDefeated(EnemyType.FLEDGED_VAMPIRE) }
                    SoundManager.playDusting()
                    VibrationHelper.vibrateDusted(getApplication())
                }
                NightEventType.HELLMOUTH_BREACH -> {
                    repeat(2) { repository.recordEnemyDefeated(EnemyType.HELLHOUND) }
                    SoundManager.playLevelUp()
                    VibrationHelper.vibrateCrit(getApplication())
                }
                NightEventType.BRONZE_ALLEY_AMBUSH -> {
                    repository.recordEnemyDefeated(EnemyType.VAMPIRE_BOUNCER)
                    repository.updatePlayerStats { it.copy(civiliansSaved = it.civiliansSaved + 3) }
                    SoundManager.playLevelUp()
                    VibrationHelper.vibrateCrit(getApplication())
                }
                NightEventType.OCCULT_RITUAL_SABOTAGE -> {
                    repository.recordEnemyDefeated(EnemyType.VAHRALL_DEMON)
                    SoundManager.playLevelUp()
                    VibrationHelper.vibrateCrit(getApplication())
                }
            }

            _eventResolution.value = EventResolutionResult(
                event = event,
                chosenOption = option,
                isSuccess = true,
                earnedXp = earnedXp,
                earnedTokens = earnedTokens,
                narrativeOutcome = option.outcomeVictoryText
            )
        } else {
            // Failure penalty & direct combat trigger
            val damage = Random.nextInt(14, 22)
            val newHp = (stats.currentHp - damage).coerceAtLeast(1)
            repository.updatePlayerStats { it.copy(currentHp = newHp) }
            SoundManager.playTriviaWrong()
            VibrationHelper.vibrateHit(getApplication())

            val fallbackEnemy = when (event.type) {
                NightEventType.GRAVEYARD_STAKEOUT -> EnemyType.VAMPIRE_BOUNCER
                NightEventType.HELLMOUTH_BREACH -> EnemyType.HELLHOUND
                NightEventType.BRONZE_ALLEY_AMBUSH -> EnemyType.VAMPIRE_BOUNCER
                NightEventType.OCCULT_RITUAL_SABOTAGE -> EnemyType.VAHRALL_DEMON
                NightEventType.VAMPIRE_NEST_RAID -> EnemyType.FLEDGED_VAMPIRE
            }

            _eventResolution.value = EventResolutionResult(
                event = event,
                chosenOption = option,
                isSuccess = false,
                earnedXp = 45,
                earnedTokens = 15,
                narrativeOutcome = "${option.outcomeFailureText} (Buffy suffered $damage damage in the chaotic ambush!)",
                triggersImmediateCombat = true,
                combatEnemyType = fallbackEnemy
            )
        }
    }

    fun dismissNightEvent() {
        _activeNightEvent.value = null
        _eventResolution.value = null
    }

    fun startCombatFromEvent(enemyType: EnemyType) {
        val event = _activeNightEvent.value
        val location = when (event?.type) {
            NightEventType.HELLMOUTH_BREACH -> PatrolLocationType.THE_HELLMOUTH
            NightEventType.BRONZE_ALLEY_AMBUSH -> PatrolLocationType.THE_BRONZE_ALLEY
            NightEventType.OCCULT_RITUAL_SABOTAGE -> PatrolLocationType.SUNNYDALE_SEWERS
            else -> PatrolLocationType.SUNNYDALE_CEMETERY
        }
        dismissNightEvent()
        startDirectCombat(enemyType, location)
    }

    private fun startDirectCombat(enemyType: EnemyType, location: PatrolLocationType) {
        val stats = playerStats.value
        val scaledHp = (enemyType.baseHp * (1.0 + (stats.level - 1) * 0.12)).toInt()

        val enemy = Enemy(
            id = "enemy_${System.currentTimeMillis()}",
            type = enemyType,
            currentHp = scaledHp,
            maxHp = scaledHp,
            attackProgress = 0f
        )

        _combatLogs.value = listOf(
            CombatLogEntry(text = "Emergency alert at ${location.displayName}!"),
            CombatLogEntry(
                text = "${enemyType.displayName}: \"${enemyType.spawnQuote}\"",
                isEnemy = true
            )
        )

        _combatState.value = CombatState(
            enemy = enemy,
            location = location,
            civilianRescued = false
        )

        startEnemyAttackLoop()
    }

    // --- COMBAT & PATROL ---

    fun startPatrolEncounter(location: PatrolLocationType) {
        // 40% chance of triggering an emergency Night Patrol event on district patrol!
        val rollForEvent = Random.nextFloat()
        if (rollForEvent < 0.40f) {
            val matchingEvent = when (location) {
                PatrolLocationType.SUNNYDALE_CEMETERY -> NightEventType.GRAVEYARD_STAKEOUT
                PatrolLocationType.THE_HELLMOUTH -> NightEventType.HELLMOUTH_BREACH
                PatrolLocationType.THE_BRONZE_ALLEY -> NightEventType.BRONZE_ALLEY_AMBUSH
                PatrolLocationType.SUNNYDALE_SEWERS -> NightEventType.OCCULT_RITUAL_SABOTAGE
            }
            triggerRandomNightEvent(matchingEvent)
            return
        }

        val enemyType = pickEnemyForLocation(location)
        val stats = playerStats.value
        val scaledHp = (enemyType.baseHp * (1.0 + (stats.level - 1) * 0.12)).toInt()

        val enemy = Enemy(
            id = "enemy_${System.currentTimeMillis()}",
            type = enemyType,
            currentHp = scaledHp,
            maxHp = scaledHp,
            attackProgress = 0f
        )

        val isCivilianRescue = location == PatrolLocationType.THE_BRONZE_ALLEY && Random.nextFloat() < 0.45f

        _combatLogs.value = listOf(
            CombatLogEntry(text = "Patrolling ${location.displayName}..."),
            CombatLogEntry(
                text = "${enemyType.displayName}: \"${enemyType.spawnQuote}\"",
                isEnemy = true
            )
        )

        if (isCivilianRescue) {
            addCombatLog("A Sunnydale High student is trapped in the alley! Slay this fiend to rescue them!", isCritical = true)
        }

        _combatState.value = CombatState(
            enemy = enemy,
            location = location,
            civilianRescued = isCivilianRescue
        )

        startEnemyAttackLoop()
    }

    private fun pickEnemyForLocation(location: PatrolLocationType): EnemyType {
        return when (location) {
            PatrolLocationType.SUNNYDALE_CEMETERY -> {
                if (Random.nextFloat() < 0.65f) EnemyType.FLEDGED_VAMPIRE else EnemyType.VAMPIRE_BOUNCER
            }
            PatrolLocationType.THE_BRONZE_ALLEY -> {
                val roll = Random.nextFloat()
                when {
                    roll < 0.45f -> EnemyType.VAMPIRE_BOUNCER
                    roll < 0.80f -> EnemyType.FLEDGED_VAMPIRE
                    else -> EnemyType.SPIKE
                }
            }
            PatrolLocationType.SUNNYDALE_SEWERS -> {
                val roll = Random.nextFloat()
                when {
                    roll < 0.50f -> EnemyType.POLGARA_DEMON
                    roll < 0.85f -> EnemyType.VAHRALL_DEMON
                    else -> EnemyType.HELLHOUND
                }
            }
            PatrolLocationType.THE_HELLMOUTH -> {
                val roll = Random.nextFloat()
                when {
                    roll < 0.40f -> EnemyType.HELLHOUND
                    roll < 0.70f -> EnemyType.VAHRALL_DEMON
                    roll < 0.90f -> EnemyType.SPIKE
                    else -> EnemyType.THE_MASTER
                }
            }
        }
    }

    private fun startEnemyAttackLoop() {
        enemyAttackJob?.cancel()
        enemyAttackJob = viewModelScope.launch {
            while (isActive) {
                delay(100L)
                val current = _combatState.value ?: break
                if (!current.isCombatActive || current.isVictory || current.isDefeat) break

                if (current.enemy.isStunned) {
                    delay(300L)
                    _combatState.update { state ->
                        state?.copy(enemy = state.enemy.copy(isStunned = false))
                    }
                    continue
                }

                val currentProg = current.enemy.attackProgress
                val step = 100f / current.enemy.type.attackIntervalMs
                val nextProg = currentProg + step

                val isDodgeWindow = nextProg in 0.55f..0.90f

                if (nextProg >= 1.0f) {
                    executeEnemyAttack()
                } else {
                    _combatState.update { state ->
                        state?.copy(
                            enemy = state.enemy.copy(attackProgress = nextProg),
                            isDodgeWindowActive = isDodgeWindow
                        )
                    }
                }
            }
        }
    }

    private fun executeEnemyAttack() {
        val current = _combatState.value ?: return
        val enemy = current.enemy
        val rawDamage = enemy.type.attackPower + Random.nextInt(-3, 4)
        val player = playerStats.value

        var actualDamage = rawDamage
        var newShield = player.shieldRemaining

        if (newShield > 0) {
            if (newShield >= rawDamage) {
                newShield -= rawDamage
                actualDamage = 0
                addCombatLog("Willow's magic shield absorbed the entire blow! ($newShield remaining)", isCritical = true)
            } else {
                actualDamage -= newShield
                newShield = 0
                addCombatLog("Willow's shield shattered, absorbing some damage!", isCritical = true)
            }
        }

        val nextHp = (player.currentHp - actualDamage).coerceAtLeast(0)
        repository.updatePlayerStats { it.copy(currentHp = nextHp, shieldRemaining = newShield) }

        VibrationHelper.vibrateHit(getApplication())
        SoundManager.playPunch()
        addCombatLog("${enemy.type.displayName} strikes Buffy for $actualDamage damage!", isEnemy = true)

        if (nextHp <= 0) {
            handleDefeat()
        } else {
            _combatState.update { state ->
                state?.copy(
                    enemy = enemy.copy(attackProgress = 0f),
                    lastDamageReceived = actualDamage,
                    isDodgeWindowActive = false,
                    comboStreak = 0
                )
            }
        }
    }

    fun onPlayerPunchCombo() {
        val current = _combatState.value ?: return
        if (!current.isCombatActive || current.isVictory || current.isDefeat) return

        val stats = playerStats.value
        // Consumes stamina
        val staminaCost = 6
        val hasStamina = stats.stamina >= staminaCost
        repository.modifyStamina(-staminaCost)

        val newCombo = current.comboStreak + 1
        val isCrit = Random.nextFloat() < (0.15f + (stats.agilityParryBonus * 0.01f)) || current.watcherResearchBuffActive
        val baseDamage = stats.martialPower + Random.nextInt(4, 9)
        val staminaMultiplier = if (hasStamina) 1.0f else 0.75f
        val critMultiplier = if (isCrit) 1.8f else (1.0f + (newCombo * 0.08f))
        val damage = (baseDamage * critMultiplier * staminaMultiplier).toInt()

        val nextEnemyHp = (current.enemy.currentHp - damage).coerceAtLeast(0)
        val earnedEnergy = (stats.energy + 12).coerceAtMost(100)
        repository.updatePlayerStats { it.copy(energy = earnedEnergy) }

        // Sound & haptic triggers
        SoundManager.playPunch()

        if (isCrit) {
            VibrationHelper.vibrateCrit(getApplication())
            addCombatLog("CRITICAL HIT! Buffy lands a spinning roundhouse for $damage DMG!", isCritical = true)
            if (Random.nextFloat() < 0.35f) {
                addCombatLog("Buffy: \"${buffyQuotes.random()}\"", isBuffyQuote = true)
            }
        } else {
            VibrationHelper.vibrateHit(getApplication())
            addCombatLog("Buffy attacks with Martial Arts combo ($newCombo hit): $damage DMG.")
        }

        if (nextEnemyHp <= 0) {
            handleEnemyDefeated(wasDusted = false)
        } else {
            _combatState.update { state ->
                state?.copy(
                    enemy = state.enemy.copy(currentHp = nextEnemyHp),
                    comboStreak = newCombo,
                    lastDamageDealt = damage,
                    watcherResearchBuffActive = false
                )
            }
        }
    }

    fun onPlayerStakeFinisher() {
        val current = _combatState.value ?: return
        if (!current.isCombatActive || current.isVictory || current.isDefeat) return

        val enemy = current.enemy
        val equippedStake = arsenal.value.find { it.type == ArsenalItemType.STAKE && it.isEquipped }
            ?: arsenal.value.first { it.type == ArsenalItemType.STAKE }

        val thresholdPercent = if (equippedStake.id == "mr_pointy") 0.55f else 0.40f
        val currentPercent = enemy.currentHp.toFloat() / enemy.maxHp

        if (!enemy.type.isVampire) {
            addCombatLog("Only vampires can be dusted with a wooden stake! Use weapons or martial combat on demons.")
            return
        }

        if (currentPercent > thresholdPercent && !enemy.isStunned) {
            addCombatLog("Vampire is moving too fast to stake directly! Weaken their HP below ${(thresholdPercent * 100).toInt()}% or stun them first!")
            return
        }

        // Staking SUCCESS! Trigger sound cue for slaying demon and dusting!
        SoundManager.playDusting()
        VibrationHelper.vibrateDusted(getApplication())
        addCombatLog("STAKE THROUGH THE HEART! ${equippedStake.name} strikes true!", isCritical = true)
        addCombatLog("${enemy.type.displayName}: \"${enemy.type.dustQuote}\"", isEnemy = true)
        addCombatLog("Buffy: \"${buffyQuotes.random()}\"", isBuffyQuote = true)

        _combatState.update { state ->
            state?.copy(isDustedAnimation = true)
        }

        viewModelScope.launch {
            delay(1200L)
            handleEnemyDefeated(wasDusted = true)
        }
    }

    fun onPlayerDodgeParry() {
        val current = _combatState.value ?: return
        if (!current.isCombatActive || current.isVictory || current.isDefeat) return

        val enemy = current.enemy
        if (current.isDodgeWindowActive || enemy.attackProgress >= 0.50f) {
            // Perfect Dodge & Counter!
            SoundManager.playParry()
            VibrationHelper.vibrateCrit(getApplication())
            val bonusEnergy = (playerStats.value.energy + 25).coerceAtMost(100)
            repository.updatePlayerStats { it.copy(energy = bonusEnergy) }

            val counterDamage = playerStats.value.martialPower + 10
            val nextEnemyHp = (enemy.currentHp - counterDamage).coerceAtLeast(0)

            addCombatLog("PERFECT DODGE! Buffy cartwheels under the claws and counters for $counterDamage DMG!", isCritical = true)

            if (nextEnemyHp <= 0) {
                handleEnemyDefeated(wasDusted = false)
            } else {
                _combatState.update { state ->
                    state?.copy(
                        enemy = enemy.copy(
                            currentHp = nextEnemyHp,
                            attackProgress = 0f,
                            isStunned = true
                        ),
                        isDodgeWindowActive = false,
                        comboStreak = state.comboStreak + 2
                    )
                }
            }
        } else {
            addCombatLog("Buffy dodges preemptively. Time your dodge when the enemy strike indicator turns red!")
        }
    }

    fun onUseArsenalItem(item: ArsenalItem) {
        val current = _combatState.value ?: return
        if (!current.isCombatActive || current.isVictory || current.isDefeat) return
        if (item.currentCharges <= 0) {
            addCombatLog("No charges remaining for ${item.name}! Restock in the Arsenal.")
            return
        }

        val used = repository.useArsenalItemCharge(item.id)
        if (!used) return

        when (item.type) {
            ArsenalItemType.MAGIC_HOLY -> {
                val damage = item.damage + 10
                val nextEnemyHp = (current.enemy.currentHp - damage).coerceAtLeast(0)
                SoundManager.playDusting()
                VibrationHelper.vibrateCrit(getApplication())
                addCombatLog("Buffy hurls ${item.name}! Sizzling holy water sears demon flesh for $damage DMG and stuns!", isCritical = true)

                if (nextEnemyHp <= 0) {
                    handleEnemyDefeated(wasDusted = false)
                } else {
                    _combatState.update { state ->
                        state?.copy(
                            enemy = state.enemy.copy(
                                currentHp = nextEnemyHp,
                                isStunned = true,
                                attackProgress = 0f
                            )
                        )
                    }
                }
            }
            ArsenalItemType.RANGED -> {
                val damage = item.damage + Random.nextInt(5, 15)
                val nextEnemyHp = (current.enemy.currentHp - damage).coerceAtLeast(0)
                SoundManager.playParry()
                VibrationHelper.vibrateHit(getApplication())
                addCombatLog("TWANG! Crossbow bolt impales ${current.enemy.type.displayName} for $damage piercing DMG!")

                if (nextEnemyHp <= 0) {
                    handleEnemyDefeated(wasDusted = false)
                } else {
                    _combatState.update { state ->
                        state?.copy(enemy = state.enemy.copy(currentHp = nextEnemyHp))
                    }
                }
            }
            ArsenalItemType.MEDICINE -> {
                val healAmount = 45
                val newHp = (playerStats.value.currentHp + healAmount).coerceAtMost(playerStats.value.maxHp)
                repository.updatePlayerStats { it.copy(currentHp = newHp) }
                repository.modifyStamina(30)
                SoundManager.playLevelUp()
                addCombatLog("Buffy applies bandages and disinfectant: +$healAmount HP and +30 Stamina restored.", isCritical = true)
            }
            ArsenalItemType.STAKE -> {
                val damage = item.damage + playerStats.value.martialPower
                val nextEnemyHp = (current.enemy.currentHp - damage).coerceAtLeast(0)
                SoundManager.playPunch()
                VibrationHelper.vibrateCrit(getApplication())
                addCombatLog("SWING! ${item.name} cleaves through dark demonic armor for $damage DMG!", isCritical = true)

                if (nextEnemyHp <= 0) {
                    handleEnemyDefeated(wasDusted = false)
                } else {
                    _combatState.update { state ->
                        state?.copy(enemy = state.enemy.copy(currentHp = nextEnemyHp))
                    }
                }
            }
        }
    }

    fun callScoobyAlly(ally: ScoobyAlly) {
        val currentCooldown = _allyCooldowns.value[ally] ?: 0
        if (currentCooldown > 0) {
            addCombatLog("${ally.displayName} is catching their breath! ($currentCooldown s remaining)")
            return
        }

        val current = _combatState.value ?: return
        if (!current.isCombatActive || current.isVictory || current.isDefeat) return

        _allyCooldowns.update { it + (ally to ally.cooldownSeconds) }
        SoundManager.playParry()
        addCombatLog("${ally.displayName}: \"${ally.quote}\"", isCritical = true)

        when (ally) {
            ScoobyAlly.GILES -> {
                _combatState.update { state ->
                    state?.copy(watcherResearchBuffActive = true)
                }
                addCombatLog("Watcher Research Active: ${ally.effectDescription}")
            }
            ScoobyAlly.WILLOW -> {
                repository.updatePlayerStats { it.copy(shieldRemaining = it.shieldRemaining + 45) }
                addCombatLog("Wiccan Ward Active: Shield of Tara granted! (+45 Shield)")
            }
            ScoobyAlly.XANDER -> {
                val healAmount = 25
                val newHp = (playerStats.value.currentHp + healAmount).coerceAtMost(playerStats.value.maxHp)
                repository.updatePlayerStats { it.copy(currentHp = newHp) }
                repository.modifyStamina(25)
                _combatState.update { state ->
                    state?.copy(
                        enemy = state.enemy.copy(isStunned = true, attackProgress = 0f)
                    )
                }
                addCombatLog("Xander successfully distracted ${current.enemy.type.displayName}! Enemy stunned, +$healAmount HP and +25 Stamina.")
            }
            ScoobyAlly.ANGEL -> {
                val damage = 65 + playerStats.value.level * 4
                val nextEnemyHp = (current.enemy.currentHp - damage).coerceAtLeast(0)
                SoundManager.playPunch()
                VibrationHelper.vibrateCrit(getApplication())
                addCombatLog("Angel emerges from shadows and delivers a bone-shattering blow for $damage DMG!", isCritical = true)

                if (nextEnemyHp <= 0) {
                    handleEnemyDefeated(wasDusted = false)
                } else {
                    _combatState.update { state ->
                        state?.copy(enemy = state.enemy.copy(currentHp = nextEnemyHp))
                    }
                }
            }
        }
    }

    private fun handleEnemyDefeated(wasDusted: Boolean) {
        enemyAttackJob?.cancel()
        val current = _combatState.value ?: return
        val enemy = current.enemy

        val baseEarnedXp = enemy.type.baseHp / 2 + (current.location.dangerStars * 15)
        val baseEarnedTokens = current.location.dangerStars * 12 + Random.nextInt(5, 15)

        SoundManager.playLevelUp()
        repository.recordEnemyDefeated(enemy.type)
        val alert = repository.addXpAndTokens(baseEarnedXp, baseEarnedTokens)
        if (alert != null) {
            triggerRankUpAlert(alert)
        }

        if (current.civilianRescued) {
            repository.updatePlayerStats { it.copy(civiliansSaved = it.civiliansSaved + 1) }
            addCombatLog("CIVILIAN RESCUED! \"Thank you Buffy, I thought I was dead!\" (+Bonus Slayer Karma)", isCritical = true)
        }

        _combatState.update { state ->
            state?.copy(
                isCombatActive = false,
                isVictory = true,
                earnedXp = baseEarnedXp,
                earnedTokens = baseEarnedTokens
            )
        }
    }

    private fun handleDefeat() {
        enemyAttackJob?.cancel()
        _combatState.update { state ->
            state?.copy(
                isCombatActive = false,
                isDefeat = true
            )
        }
        addCombatLog("Buffy was overwhelmed in the shadows! Retreating to the Library to recover...", isCritical = true)
    }

    fun exitCombatAndReturnToPatrol() {
        enemyAttackJob?.cancel()
        _combatState.value = null
        _currentScreen.value = GameScreen.PATROL
    }

    fun healAtLibrary() {
        repository.restoreFullHealth()
        SoundManager.playLevelUp()
        addCombatLog("Giles cleaned his glasses, poured English tea, and patched your wounds. HP and Stamina fully restored!")
    }

    fun unlockOrEquipItem(itemId: String) {
        val result = repository.unlockOrEquipArsenalItem(itemId)
        if (result) SoundManager.playParry()
    }

    fun restockItem(itemId: String, cost: Int) {
        val result = repository.restockConsumables(itemId, cost)
        if (result) SoundManager.playParry()
    }

    fun upgradeStat(statName: String, cost: Int) {
        val result = repository.upgradeStat(statName, cost)
        if (result) SoundManager.playLevelUp()
    }

    private fun addCombatLog(text: String, isCritical: Boolean = false, isBuffyQuote: Boolean = false, isEnemy: Boolean = false) {
        _combatLogs.update { current ->
            (listOf(CombatLogEntry(text = text, isCritical = isCritical, isBuffyQuote = isBuffyQuote, isEnemy = isEnemy)) + current).take(20)
        }
    }

    private fun startAllyCooldownTicker() {
        cooldownTickerJob?.cancel()
        cooldownTickerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _allyCooldowns.update { current ->
                    current.mapValues { (_, remaining) -> (remaining - 1).coerceAtLeast(0) }
                        .filterValues { it > 0 }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        enemyAttackJob?.cancel()
        cooldownTickerJob?.cancel()
        staminaRegenJob?.cancel()
        SoundManager.stopAmbiance()
    }
}
