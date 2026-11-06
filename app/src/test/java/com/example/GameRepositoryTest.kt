package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.GameRepository
import com.example.data.NightPatrolEventFactory
import com.example.data.TriviaBank
import com.example.model.EnemyType
import com.example.model.NightEventType
import com.example.model.TacticalApproachType
import com.example.model.TriviaDifficulty
import com.example.util.SoundManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GameRepositoryTest {
    private lateinit var repository: GameRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        repository = GameRepository(context)
    }

    @Test
    fun testLevelProgressionAndXp() {
        val initialLevel = repository.playerStats.value.level
        repository.addXpAndTokens(earnedXp = 200, earnedTokens = 50)
        assertTrue(repository.playerStats.value.level >= initialLevel)
        assertTrue(repository.playerStats.value.occultTokens >= 50)
    }

    @Test
    fun testRecordDefeatedEnemy() {
        val initialDusted = repository.playerStats.value.vampiresDusted
        repository.recordEnemyDefeated(EnemyType.FLEDGED_VAMPIRE)
        assertEquals(initialDusted + 1, repository.playerStats.value.vampiresDusted)
    }

    @Test
    fun testStaminaModificationAndCaps() {
        repository.restoreFullHealth()
        assertEquals(100, repository.playerStats.value.stamina)

        repository.modifyStamina(-30)
        assertEquals(70, repository.playerStats.value.stamina)

        repository.modifyStamina(-100)
        assertEquals(0, repository.playerStats.value.stamina)

        repository.modifyStamina(50)
        assertEquals(50, repository.playerStats.value.stamina)
    }

    @Test
    fun testTriviaEscalatingStreakAndRewards() {
        repository.recordTriviaResult(isCorrect = true, xpReward = 25, tokenReward = 10)
        assertEquals(1, repository.playerStats.value.triviaStreak)
        assertEquals(1, repository.playerStats.value.triviaCorrectCount)

        // Reset on wrong answer
        repository.recordTriviaResult(isCorrect = false, xpReward = 0, tokenReward = 0)
        assertEquals(0, repository.playerStats.value.triviaStreak)
        assertEquals(1, repository.playerStats.value.bestTriviaStreak)
    }

    @Test
    fun testTriviaBankEscalation() {
        val noviceQuestion = TriviaBank.getQuestionForStreak(0, emptySet())
        assertEquals(TriviaDifficulty.NOVICE, noviceQuestion.difficulty)

        val watcherQuestion = TriviaBank.getQuestionForStreak(9, emptySet())
        assertEquals(TriviaDifficulty.WATCHER_COUNCIL, watcherQuestion.difficulty)

        val masterQuestion = TriviaBank.getQuestionForStreak(14, emptySet())
        assertEquals(TriviaDifficulty.HELLMOUTH_MASTER, masterQuestion.difficulty)
    }

    @Test
    fun testSoundManagerToggles() {
        assertTrue(SoundManager.isSfxEnabled.value)
        SoundManager.toggleSfx()
        assertFalse(SoundManager.isSfxEnabled.value)
        SoundManager.toggleSfx()
        assertTrue(SoundManager.isSfxEnabled.value)
    }

    @Test
    fun testGraveyardStakeoutEventCreation() {
        val event = NightPatrolEventFactory.createEvent(NightEventType.GRAVEYARD_STAKEOUT)
        assertEquals(NightEventType.GRAVEYARD_STAKEOUT, event.type)
        assertEquals(3, event.tacticalOptions.size)
        assertTrue(event.type.baseBonusXp >= 200)

        val stealthTactic = event.tacticalOptions.find { it.type == TacticalApproachType.STEALTH_AMBUSH }
        assertNotNull(stealthTactic)
        assertTrue(stealthTactic!!.bonusRewardMultiplier > 1.0f)
    }

    @Test
    fun testHellmouthBreachEventCreation() {
        val event = NightPatrolEventFactory.createEvent(NightEventType.HELLMOUTH_BREACH)
        assertEquals(NightEventType.HELLMOUTH_BREACH, event.type)
        assertEquals(5, event.type.dangerStars)
        assertTrue(event.type.baseBonusXp >= 350)

        val mysticalSeal = event.tacticalOptions.find { it.type == TacticalApproachType.MYSTICAL_SEAL }
        assertNotNull(mysticalSeal)
        assertTrue(mysticalSeal!!.successRatePercent > 70)
    }

    @Test
    fun testSlayerRankPromotionAndBonusTokens() {
        assertEquals(com.example.model.SlayerRank.POTENTIAL, repository.playerStats.value.slayerRank)
        assertEquals(0, repository.playerStats.value.totalXp)

        // Award 120 XP -> crosses SUNNYDALE_SLAYER threshold (100 XP)
        val initialTokens = repository.playerStats.value.occultTokens
        val alert = repository.addXpAndTokens(earnedXp = 120, earnedTokens = 15)

        assertNotNull(alert)
        assertEquals(com.example.model.SlayerRank.SUNNYDALE_SLAYER, alert!!.newRank)
        assertEquals(30, alert.bonusTokens)
        assertEquals(com.example.model.SlayerRank.SUNNYDALE_SLAYER, repository.playerStats.value.slayerRank)
        assertEquals(120, repository.playerStats.value.totalXp)
        // Check tokens include earned (15) + bonus (30)
        assertEquals(initialTokens + 15 + 30, repository.playerStats.value.occultTokens)

        // Adding small XP (30 XP) within the same tier (150 total XP) should NOT trigger a new alert
        val subAlert = repository.addXpAndTokens(earnedXp = 30, earnedTokens = 5)
        assertNull(subAlert)
        assertEquals(com.example.model.SlayerRank.SUNNYDALE_SLAYER, repository.playerStats.value.slayerRank)
        assertEquals(150, repository.playerStats.value.totalXp)

        // Adding 110 XP -> 260 total XP -> crosses HELLMOUTH_DEFENDER threshold (250 XP)
        val secondAlert = repository.addXpAndTokens(earnedXp = 110, earnedTokens = 10)
        assertNotNull(secondAlert)
        assertEquals(com.example.model.SlayerRank.HELLMOUTH_DEFENDER, secondAlert!!.newRank)
        assertEquals(50, secondAlert.bonusTokens)
        assertEquals(com.example.model.SlayerRank.HELLMOUTH_DEFENDER, repository.playerStats.value.slayerRank)
    }

    @Test
    fun testTriviaResultTriggersRankUpAlert() {
        // Awarding enough XP from trivia to pass threshold triggers RankUpAlert
        val alert = repository.recordTriviaResult(isCorrect = true, xpReward = 150, tokenReward = 20)
        assertNotNull(alert)
        assertEquals(com.example.model.SlayerRank.SUNNYDALE_SLAYER, alert!!.newRank)
    }
}

