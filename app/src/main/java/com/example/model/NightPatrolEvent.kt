package com.example.model

enum class NightEventType(
    val title: String,
    val subtitle: String,
    val narrative: String,
    val dangerStars: Int,
    val baseBonusXp: Int,
    val baseBonusTokens: Int,
    val bgImageName: String
) {
    GRAVEYARD_STAKEOUT(
        title = "Graveyard Stake-out",
        subtitle = "Cemetery Crypt Convergence",
        narrative = "A coven of fledged vampires is gathering around the Restless Souls crypt for a midnight blood ritual. Sneak in for an ambush, set blessed traps, or assault them head-on!",
        dangerStars = 3,
        baseBonusXp = 220,
        baseBonusTokens = 85,
        bgImageName = "cemetery_bg"
    ),
    HELLMOUTH_BREACH(
        title = "Hellmouth Breach",
        subtitle = "Dimensional Rupture Detected",
        narrative = "A subterranean fissure beneath Sunnydale High has cracked open! Extra-dimensional sulfurous demons are pouring into the corridors. Seal the rift before Sunnydale is consumed!",
        dangerStars = 5,
        baseBonusXp = 380,
        baseBonusTokens = 150,
        bgImageName = "hellmouth_bg"
    ),
    BRONZE_ALLEY_AMBUSH(
        title = "Bronze Alley Hostage Crisis",
        subtitle = "Civilians Under Siege",
        narrative = "A gang of ruthless vampire enforcers has trapped three Sunnydale High teenagers behind the Bronze dumpster alley. Save them before dawn!",
        dangerStars = 3,
        baseBonusXp = 260,
        baseBonusTokens = 95,
        bgImageName = "cemetery_bg"
    ),
    OCCULT_RITUAL_SABOTAGE(
        title = "Occult Ritual Sabotage",
        subtitle = "Sumerian Blood Altar",
        narrative = "Vahrall cultists in the flooded sewers are chanting over a pulsing demonic relic. Giles warns that finishing their chant will summon an ancient leviathan!",
        dangerStars = 4,
        baseBonusXp = 320,
        baseBonusTokens = 120,
        bgImageName = "cemetery_bg"
    ),
    VAMPIRE_NEST_RAID(
        title = "Underground Vampire Nest",
        subtitle = "Daylight Coffin Purge",
        narrative = "An underground mausoleum tunnel has been converted into a vampire nest. Over a dozen bloodsuckers are resting in stone sarcophagi.",
        dangerStars = 4,
        baseBonusXp = 290,
        baseBonusTokens = 110,
        bgImageName = "cemetery_bg"
    )
}

enum class TacticalApproachType {
    STEALTH_AMBUSH,
    MYSTICAL_SEAL,
    WATCHER_DECRYPTION,
    DISTRACTION_DECOY,
    HOLY_WARD_TRAP,
    FULL_SLAYER_ASSAULT
}

data class TacticalOption(
    val type: TacticalApproachType,
    val title: String,
    val description: String,
    val requiredStat: String, // e.g. "Agility >= 10", "Willow Ready", "Holy Water", "Stamina >= 30"
    val successRatePercent: Int,
    val bonusRewardMultiplier: Float,
    val staminaCost: Int = 15,
    val outcomeVictoryText: String,
    val outcomeFailureText: String
)

data class NightPatrolEvent(
    val id: String = "event_${System.currentTimeMillis()}",
    val type: NightEventType,
    val tacticalOptions: List<TacticalOption>,
    val expiresTimerSeconds: Int = 180, // Event urgency
    val isResolved: Boolean = false
)

data class EventResolutionResult(
    val event: NightPatrolEvent,
    val chosenOption: TacticalOption,
    val isSuccess: Boolean,
    val earnedXp: Int,
    val earnedTokens: Int,
    val narrativeOutcome: String,
    val triggersImmediateCombat: Boolean = false,
    val combatEnemyType: EnemyType? = null
)
