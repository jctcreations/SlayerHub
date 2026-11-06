package com.example.model

enum class TriviaDifficulty(
    val tier: Int,
    val title: String,
    val minStreak: Int,
    val xpReward: Int,
    val tokenReward: Int
) {
    NOVICE(
        tier = 1,
        title = "Scooby Initiate",
        minStreak = 0,
        xpReward = 25,
        tokenReward = 10
    ),
    SCOOBY(
        tier = 2,
        title = "Sunnydale Defender",
        minStreak = 3,
        xpReward = 45,
        tokenReward = 20
    ),
    WATCHER_TRAINEE(
        tier = 3,
        title = "Watcher Trainee",
        minStreak = 6,
        xpReward = 70,
        tokenReward = 35
    ),
    WATCHER_COUNCIL(
        tier = 4,
        title = "Watcher Council Scholar",
        minStreak = 9,
        xpReward = 100,
        tokenReward = 50
    ),
    HELLMOUTH_MASTER(
        tier = 5,
        title = "Hellmouth Lore Master",
        minStreak = 12,
        xpReward = 150,
        tokenReward = 80
    )
}

data class TriviaQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val difficulty: TriviaDifficulty
)

data class TriviaState(
    val currentQuestion: TriviaQuestion? = null,
    val selectedOptionIndex: Int? = null,
    val isAnswerRevealed: Boolean = false,
    val isCorrect: Boolean = false,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalAnswered: Int = 0,
    val totalCorrect: Int = 0,
    val currentTier: TriviaDifficulty = TriviaDifficulty.NOVICE
)
