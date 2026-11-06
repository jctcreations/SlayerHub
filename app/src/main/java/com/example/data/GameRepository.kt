package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("sunnydale_slayer_save", Context.MODE_PRIVATE)

    private val _playerStats = MutableStateFlow(loadPlayerStats())
    val playerStats: StateFlow<PlayerStats> = _playerStats.asStateFlow()

    private val _arsenal = MutableStateFlow(loadArsenal())
    val arsenal: StateFlow<List<ArsenalItem>> = _arsenal.asStateFlow()

    private val _bestiary = MutableStateFlow(loadBestiary())
    val bestiary: StateFlow<Map<EnemyType, BestiaryEntry>> = _bestiary.asStateFlow()

    private fun loadPlayerStats(): PlayerStats {
        val level = prefs.getInt("player_level", 1)
        val xp = prefs.getInt("player_xp", 0)
        val totalXp = prefs.getInt("player_total_xp", xp)
        val hp = prefs.getInt("player_hp", 100)
        val maxHp = prefs.getInt("player_max_hp", 100)
        val stamina = prefs.getInt("player_stamina", 85)
        val maxStamina = prefs.getInt("player_max_stamina", 100)
        val tokens = prefs.getInt("player_tokens", 80)
        val martial = prefs.getInt("player_martial", 12)
        val agility = prefs.getInt("player_agility", 8)
        val dusted = prefs.getInt("player_dusted", 0)
        val vanquished = prefs.getInt("player_vanquished", 0)
        val saved = prefs.getInt("player_saved", 0)
        val nights = prefs.getInt("player_nights", 1)
        val streak = prefs.getInt("trivia_streak", 0)
        val bestStreak = prefs.getInt("trivia_best_streak", 0)
        val triviaCount = prefs.getInt("trivia_correct_count", 0)

        val rank = when {
            bestStreak >= 12 -> "Hellmouth Lore Master"
            bestStreak >= 9 -> "Watcher Council Scholar"
            bestStreak >= 6 -> "Watcher Trainee"
            bestStreak >= 3 -> "Sunnydale Defender"
            else -> "Scooby Initiate"
        }
        val slayerRank = SlayerRank.getRankForTotalXp(totalXp)

        return PlayerStats(
            level = level,
            xp = xp,
            xpToNextLevel = level * 100,
            totalXp = totalXp,
            currentHp = hp,
            maxHp = maxHp,
            energy = 40,
            stamina = stamina,
            maxStamina = maxStamina,
            occultTokens = tokens,
            martialPower = martial,
            agilityParryBonus = agility,
            vampiresDusted = dusted,
            demonsVanquished = vanquished,
            civiliansSaved = saved,
            nightsSurvived = nights,
            triviaStreak = streak,
            bestTriviaStreak = bestStreak,
            triviaCorrectCount = triviaCount,
            watcherRank = rank,
            slayerRank = slayerRank
        )
    }

    private fun savePlayerStats(stats: PlayerStats) {
        prefs.edit().apply {
            putInt("player_level", stats.level)
            putInt("player_xp", stats.xp)
            putInt("player_total_xp", stats.totalXp)
            putInt("player_hp", stats.currentHp)
            putInt("player_max_hp", stats.maxHp)
            putInt("player_stamina", stats.stamina)
            putInt("player_max_stamina", stats.maxStamina)
            putInt("player_tokens", stats.occultTokens)
            putInt("player_martial", stats.martialPower)
            putInt("player_agility", stats.agilityParryBonus)
            putInt("player_dusted", stats.vampiresDusted)
            putInt("player_vanquished", stats.demonsVanquished)
            putInt("player_saved", stats.civiliansSaved)
            putInt("player_nights", stats.nightsSurvived)
            putInt("trivia_streak", stats.triviaStreak)
            putInt("trivia_best_streak", stats.bestTriviaStreak)
            putInt("trivia_correct_count", stats.triviaCorrectCount)
            apply()
        }
    }

    private fun loadArsenal(): List<ArsenalItem> {
        return listOf(
            ArsenalItem(
                id = "stake_oak",
                name = "Classic Oak Stake",
                type = ArsenalItemType.STAKE,
                description = "Standard carved wooden stake. Instant dusting on vampires below 40% health.",
                damage = 30,
                costTokens = 0,
                maxCharges = 999,
                currentCharges = 999,
                isEquipped = true,
                isUnlocked = true
            ),
            ArsenalItem(
                id = "mr_pointy",
                name = "Mr. Pointy (Faith's Stake)",
                type = ArsenalItemType.STAKE,
                description = "Smoothly balanced heirloom stake. Dusts vampires at up to 55% health and grants +20% critical chance.",
                damage = 45,
                costTokens = 75,
                maxCharges = 999,
                currentCharges = 999,
                isEquipped = false,
                isUnlocked = prefs.getBoolean("item_mr_pointy", false)
            ),
            ArsenalItem(
                id = "holy_water",
                name = "Holy Water Phial",
                type = ArsenalItemType.MAGIC_HOLY,
                description = "Blessed water that burns flesh of both vampires and demons. Stuns for 2 seconds and deals 40 burn damage.",
                damage = 40,
                costTokens = 30,
                maxCharges = 3,
                currentCharges = prefs.getInt("item_holy_water_charges", 3),
                isEquipped = true,
                isUnlocked = true
            ),
            ArsenalItem(
                id = "crossbow",
                name = "Sunnydale Crossbow",
                type = ArsenalItemType.RANGED,
                description = "Fires blessed wooden bolts from safe range. Deals 55 piercing damage.",
                damage = 55,
                costTokens = 60,
                maxCharges = 5,
                currentCharges = prefs.getInt("item_crossbow_charges", 5),
                isEquipped = false,
                isUnlocked = prefs.getBoolean("item_crossbow", false)
            ),
            ArsenalItem(
                id = "med_kit",
                name = "High School Nurse Kit",
                type = ArsenalItemType.MEDICINE,
                description = "Bandages, smelling salts, and aspirin. Restores 40 HP immediately.",
                damage = 0,
                costTokens = 25,
                maxCharges = 2,
                currentCharges = prefs.getInt("item_med_kit_charges", 2),
                isEquipped = true,
                isUnlocked = true
            ),
            ArsenalItem(
                id = "slayer_scythe",
                name = "Scythe of the First Slayer",
                type = ArsenalItemType.STAKE,
                description = "Legendary axe-stake forged in shadow and blood. Deals 90 massive cleave damage to any demon.",
                damage = 90,
                costTokens = 200,
                maxCharges = 999,
                currentCharges = 999,
                isEquipped = false,
                isUnlocked = prefs.getBoolean("item_slayer_scythe", false)
            )
        )
    }

    private fun loadBestiary(): Map<EnemyType, BestiaryEntry> {
        val map = mutableMapOf<EnemyType, BestiaryEntry>()
        EnemyType.values().forEach { type ->
            val count = prefs.getInt("bestiary_${type.name}_count", if (type == EnemyType.FLEDGED_VAMPIRE) 1 else 0)
            val encountered = prefs.getBoolean("bestiary_${type.name}_seen", type == EnemyType.FLEDGED_VAMPIRE)
            map[type] = BestiaryEntry(
                enemyType = type,
                timesDefeated = count,
                isEncountered = encountered
            )
        }
        return map
    }

    fun updatePlayerStats(transform: (PlayerStats) -> PlayerStats) {
        _playerStats.update { current ->
            val updated = transform(current)
            savePlayerStats(updated)
            updated
        }
    }

    fun addXpAndTokens(earnedXp: Int, earnedTokens: Int): RankUpAlert? {
        var alert: RankUpAlert? = null
        _playerStats.update { current ->
            val oldRank = current.slayerRank
            val updatedTotalXp = current.totalXp + earnedXp
            val newRank = SlayerRank.getRankForTotalXp(updatedTotalXp)

            var newXp = current.xp + earnedXp
            var newLevel = current.level
            var newMaxHp = current.maxHp
            var newHp = current.currentHp
            var newMartial = current.martialPower
            var xpThreshold = newLevel * 100

            while (newXp >= xpThreshold) {
                newXp -= xpThreshold
                newLevel += 1
                newMaxHp += 15
                newHp = newMaxHp // Full heal on level up
                newMartial += 3
                xpThreshold = newLevel * 100
            }

            var bonusTokens = 0
            if (newRank.ordinal > oldRank.ordinal) {
                bonusTokens = newRank.bonusTokens
                alert = RankUpAlert(
                    newRank = newRank,
                    bonusTokens = bonusTokens
                )
            }

            val updated = current.copy(
                level = newLevel,
                xp = newXp,
                xpToNextLevel = xpThreshold,
                totalXp = updatedTotalXp,
                slayerRank = newRank,
                currentHp = newHp.coerceAtMost(newMaxHp),
                maxHp = newMaxHp,
                martialPower = newMartial,
                occultTokens = current.occultTokens + earnedTokens + bonusTokens
            )
            savePlayerStats(updated)
            updated
        }
        return alert
    }

    fun recordEnemyDefeated(type: EnemyType) {
        _bestiary.update { current ->
            val entry = current[type] ?: BestiaryEntry(type, 0, true)
            val updatedEntry = entry.copy(
                timesDefeated = entry.timesDefeated + 1,
                isEncountered = true
            )
            prefs.edit().apply {
                putInt("bestiary_${type.name}_count", updatedEntry.timesDefeated)
                putBoolean("bestiary_${type.name}_seen", true)
                apply()
            }
            current + (type to updatedEntry)
        }

        _playerStats.update { current ->
            val isVamp = type.isVampire
            val updated = current.copy(
                vampiresDusted = if (isVamp) current.vampiresDusted + 1 else current.vampiresDusted,
                demonsVanquished = if (!isVamp) current.demonsVanquished + 1 else current.demonsVanquished
            )
            savePlayerStats(updated)
            updated
        }
    }

    fun unlockOrEquipArsenalItem(itemId: String): Boolean {
        val currentItems = _arsenal.value
        val item = currentItems.find { it.id == itemId } ?: return false

        if (!item.isUnlocked) {
            val stats = _playerStats.value
            if (stats.occultTokens < item.costTokens) return false

            // Deduct tokens
            updatePlayerStats { it.copy(occultTokens = it.occultTokens - item.costTokens) }
            prefs.edit().putBoolean("item_$itemId", true).apply()
        }

        // Toggle equipped
        val updated = currentItems.map {
            if (it.id == itemId) {
                it.copy(isUnlocked = true, isEquipped = !it.isEquipped)
            } else if (it.type == item.type && item.type == ArsenalItemType.STAKE) {
                // Only one stake equipped at once
                it.copy(isEquipped = false)
            } else {
                it
            }
        }
        _arsenal.value = updated
        return true
    }

    fun restockConsumables(itemId: String, cost: Int): Boolean {
        val stats = _playerStats.value
        if (stats.occultTokens < cost) return false

        _arsenal.update { list ->
            list.map {
                if (it.id == itemId) {
                    val newCharges = it.maxCharges
                    prefs.edit().putInt("item_${itemId}_charges", newCharges).apply()
                    it.copy(currentCharges = newCharges)
                } else it
            }
        }
        updatePlayerStats { it.copy(occultTokens = it.occultTokens - cost) }
        return true
    }

    fun useArsenalItemCharge(itemId: String): Boolean {
        var used = false
        _arsenal.update { list ->
            list.map {
                if (it.id == itemId && it.currentCharges > 0) {
                    val nextCharges = if (it.maxCharges >= 999) 999 else it.currentCharges - 1
                    prefs.edit().putInt("item_${itemId}_charges", nextCharges).apply()
                    used = true
                    it.copy(currentCharges = nextCharges)
                } else it
            }
        }
        return used
    }

    fun upgradeStat(statName: String, cost: Int): Boolean {
        val stats = _playerStats.value
        if (stats.occultTokens < cost) return false

        updatePlayerStats { current ->
            when (statName) {
                "MARTIAL" -> current.copy(
                    martialPower = current.martialPower + 4,
                    occultTokens = current.occultTokens - cost
                )
                "AGILITY" -> current.copy(
                    agilityParryBonus = current.agilityParryBonus + 3,
                    occultTokens = current.occultTokens - cost
                )
                "VITALITY" -> current.copy(
                    maxHp = current.maxHp + 25,
                    currentHp = current.currentHp + 25,
                    occultTokens = current.occultTokens - cost
                )
                else -> current
            }
        }
        return true
    }

    fun restoreFullHealth() {
        updatePlayerStats { it.copy(currentHp = it.maxHp, stamina = it.maxStamina) }
    }

    fun modifyStamina(amount: Int) {
        updatePlayerStats { current ->
            val newStamina = (current.stamina + amount).coerceIn(0, current.maxStamina)
            current.copy(stamina = newStamina)
        }
    }

    fun recordTriviaResult(isCorrect: Boolean, xpReward: Int, tokenReward: Int): RankUpAlert? {
        updatePlayerStats { current ->
            if (isCorrect) {
                val newStreak = current.triviaStreak + 1
                val newBest = maxOf(newStreak, current.bestTriviaStreak)
                val newTotal = current.triviaCorrectCount + 1

                val newRank = when {
                    newBest >= 12 -> "Hellmouth Lore Master"
                    newBest >= 9 -> "Watcher Council Scholar"
                    newBest >= 6 -> "Watcher Trainee"
                    newBest >= 3 -> "Sunnydale Defender"
                    else -> "Scooby Initiate"
                }

                current.copy(
                    triviaStreak = newStreak,
                    bestTriviaStreak = newBest,
                    triviaCorrectCount = newTotal,
                    watcherRank = newRank
                )
            } else {
                current.copy(triviaStreak = 0)
            }
        }
        var alert: RankUpAlert? = null
        if (isCorrect && (xpReward > 0 || tokenReward > 0)) {
            alert = addXpAndTokens(xpReward, tokenReward)
        }
        return alert
    }
}
