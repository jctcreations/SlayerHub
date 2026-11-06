package com.example.model

enum class EnemyCategory {
    VAMPIRE,
    DEMON,
    HELL_CREATURE,
    MASTER_VAMPIRE
}

enum class EnemyType(
    val displayName: String,
    val category: EnemyCategory,
    val isVampire: Boolean,
    val baseHp: Int,
    val attackPower: Int,
    val attackIntervalMs: Long,
    val weakness: String,
    val spawnQuote: String,
    val dustQuote: String,
    val lore: String
) {
    FLEDGED_VAMPIRE(
        displayName = "Fledged Vampire",
        category = EnemyCategory.VAMPIRE,
        isVampire = true,
        baseHp = 60,
        attackPower = 12,
        attackIntervalMs = 2800L,
        weakness = "Stake through heart, holy water, sunlight",
        spawnQuote = "Fresh blood... the Slayer herself!",
        dustQuote = "Aaargh! Into ash...",
        lore = "Newly turned vampire driven by insatiable hunger. Prone to reckless rushes."
    ),
    VAMPIRE_BOUNCER(
        displayName = "Vampire Enforcer",
        category = EnemyCategory.VAMPIRE,
        isVampire = true,
        baseHp = 100,
        attackPower = 18,
        attackIntervalMs = 2600L,
        weakness = "Heavy wooden stake, decapitation",
        spawnQuote = "Nobody gets past the velvet rope tonight, Summers.",
        dustQuote = "Not the jacket...!",
        lore = "A hulking undead hired by local vampire syndicates to control the alleys behind The Bronze."
    ),
    VAHRALL_DEMON(
        displayName = "Vahrall Demon",
        category = EnemyCategory.DEMON,
        isVampire = false,
        baseHp = 120,
        attackPower = 20,
        attackIntervalMs = 2400L,
        weakness = "Blunt force, Slayer scythe, fire spells",
        spawnQuote = "*Hiss* Your soul shall feed the dark rites!",
        dustQuote = "*Crumples with sulfurous screech*",
        lore = "Horned ritualistic demon known for dark sacrificial summons across Sunnydale."
    ),
    POLGARA_DEMON(
        displayName = "Polgara Skewer Demon",
        category = EnemyCategory.DEMON,
        isVampire = false,
        baseHp = 140,
        attackPower = 24,
        attackIntervalMs = 2200L,
        weakness = "Severing forearm appendages, holy water splash",
        spawnQuote = "*Extends razor-sharp arm bone* Time to carve!",
        dustQuote = "*Bone spikes shatter into smoking embers*",
        lore = "Subterranean predator with deadly protruding bone spurs and venomous saliva."
    ),
    HELLHOUND(
        displayName = "Hellmouth Hound",
        category = EnemyCategory.HELL_CREATURE,
        isVampire = false,
        baseHp = 90,
        attackPower = 22,
        attackIntervalMs = 1900L,
        weakness = "Agile dodges, silver bolts, Willow's wards",
        spawnQuote = "*Low, guttural demonic roar vibrates through the floor*",
        dustQuote = "*Dissolves into black hellfire smoke*",
        lore = "Ferocious demonic canine spawned directly from the Hellmouth's fissures."
    ),
    SPIKE(
        displayName = "Spike (William the Bloody)",
        category = EnemyCategory.MASTER_VAMPIRE,
        isVampire = true,
        baseHp = 220,
        attackPower = 28,
        attackIntervalMs = 2100L,
        weakness = "Fast combos, emotional taunts, heavy staking",
        spawnQuote = "Well, look what the cat dragged in. Ready for round two, Slayer?",
        dustQuote = "Bloody hell! You haven't seen the last of me, pet!",
        lore = "Notorious vampire who has killed two Slayers in his lifetime. Fast, witty, and extremely dangerous."
    ),
    THE_MASTER(
        displayName = "The Master (Heinrich Joseph)",
        category = EnemyCategory.MASTER_VAMPIRE,
        isVampire = true,
        baseHp = 320,
        attackPower = 34,
        attackIntervalMs = 2000L,
        weakness = "The Slayer prophecy, wooden cross, Mr. Pointy stake",
        spawnQuote = "Welcome to the harvest, child. The stars align for Sunnydale's ruin.",
        dustQuote = "The Hellmouth cannot be sealed forever...",
        lore = "Ancient vampire patriarch trapped in the subterranean church beneath Sunnydale."
    )
}

data class Enemy(
    val id: String,
    val type: EnemyType,
    val currentHp: Int,
    val maxHp: Int,
    val attackProgress: Float = 0f, // 0.0 to 1.0 until attack triggers
    val isStunned: Boolean = false,
    val isDusted: Boolean = false
)

enum class PatrolLocationType(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val description: String,
    val dangerStars: Int,
    val bgImageName: String
) {
    SUNNYDALE_CEMETERY(
        id = "cemetery",
        displayName = "Sunnydale Cemetery",
        subtitle = "Restless Crypts & Grave Plots",
        description = "Mausoleums and open graves where vampires rise at dusk. High density of fledged bloodsuckers.",
        dangerStars = 2,
        bgImageName = "cemetery_bg"
    ),
    THE_BRONZE_ALLEY(
        id = "the_bronze",
        displayName = "The Bronze (Alleys)",
        subtitle = "Industrial Club District",
        description = "Teens party inside while bloodthirsty fiends stalk victims in the dimly lit rear alleys.",
        dangerStars = 3,
        bgImageName = "cemetery_bg"
    ),
    SUNNYDALE_SEWERS(
        id = "sewers",
        displayName = "Sunnydale Sewers",
        subtitle = "Flooded Sub-city Tunnels",
        description = "Dank, sulfurous waterways infested with Polgara demons and cultist ambushes.",
        dangerStars = 4,
        bgImageName = "cemetery_bg"
    ),
    THE_HELLMOUTH(
        id = "hellmouth",
        displayName = "The Hellmouth Crypt",
        subtitle = "Subterranean Focal Point",
        description = "Direct tear in the fabric between dimensions. Ancient demon lords and master vampires gather here.",
        dangerStars = 5,
        bgImageName = "hellmouth_bg"
    )
}

enum class ScoobyAlly(
    val displayName: String,
    val characterRole: String,
    val quote: String,
    val abilityName: String,
    val effectDescription: String,
    val cooldownSeconds: Int
) {
    GILES(
        displayName = "Rupert Giles",
        characterRole = "The Watcher",
        quote = "Buffy, according to the Pergamum Codex, this fiend has a profound aversion to blessed relics!",
        abilityName = "Watcher's Research",
        effectDescription = "+60% Slayer critical damage and reveals demon vulnerabilities.",
        cooldownSeconds = 18
    ),
    WILLOW(
        displayName = "Willow Rosenberg",
        characterRole = "Wiccan Prodigy",
        quote = "Terra, ignis, spiritus! Shield of protection!",
        abilityName = "Shield of Tara",
        effectDescription = "Absorbs up to 45 damage from the next enemy strikes.",
        cooldownSeconds = 22
    ),
    XANDER(
        displayName = "Xander Harris",
        characterRole = "The Distraction",
        quote = "Hey, fang-face! Your haircut is so last century!",
        abilityName = "Tactical Taunt",
        effectDescription = "Stuns enemy for 3.5 seconds and restores 20 HP with a candy bar.",
        cooldownSeconds = 16
    ),
    ANGEL(
        displayName = "Angel",
        characterRole = "Vampire with a Soul",
        quote = "You're not fighting alone, Buffy.",
        abilityName = "Shadow Strike",
        effectDescription = "Leaps in from darkness dealing 65 immediate kinetic damage.",
        cooldownSeconds = 30
    )
}

enum class ArsenalItemType {
    STAKE,
    RANGED,
    MAGIC_HOLY,
    MEDICINE
}

data class ArsenalItem(
    val id: String,
    val name: String,
    val type: ArsenalItemType,
    val description: String,
    val damage: Int,
    val costTokens: Int,
    val maxCharges: Int,
    val currentCharges: Int,
    val isEquipped: Boolean = false,
    val isUnlocked: Boolean = true
)

enum class SlayerRank(
    val title: String,
    val requiredTotalXp: Int,
    val badgeQuote: String,
    val bonusTokens: Int
) {
    POTENTIAL(
        title = "Slayer Potential",
        requiredTotalXp = 0,
        badgeQuote = "One girl in all the world...",
        bonusTokens = 0
    ),
    SUNNYDALE_SLAYER(
        title = "Sunnydale Slayer",
        requiredTotalXp = 100,
        badgeQuote = "Dusting vampires and navigating high school drama.",
        bonusTokens = 30
    ),
    HELLMOUTH_DEFENDER(
        title = "Hellmouth Defender",
        requiredTotalXp = 250,
        badgeQuote = "Patrolling crypts and guarding the subterranean rift.",
        bonusTokens = 50
    ),
    VAMPYR_SCOURGE(
        title = "Vampyr Scourge",
        requiredTotalXp = 500,
        badgeQuote = "Feared across Sunnydale's alleys and crypts.",
        bonusTokens = 75
    ),
    WATCHER_COUNCIL_CHAMPION(
        title = "Watcher Council Champion",
        requiredTotalXp = 850,
        badgeQuote = "Recognized in London as the vanguard of the mortal plane.",
        bonusTokens = 100
    ),
    CHOSEN_ONE(
        title = "The Chosen One",
        requiredTotalXp = 1350,
        badgeQuote = "She alone will stand against the vampires and demons.",
        bonusTokens = 150
    ),
    FIRST_SLAYER_LEGEND(
        title = "Legend of the First Slayer",
        requiredTotalXp = 2000,
        badgeQuote = "Channeling the primordial strength of the first shadow.",
        bonusTokens = 250
    );

    companion object {
        fun getRankForTotalXp(totalXp: Int): SlayerRank {
            return values().filter { totalXp >= it.requiredTotalXp }.maxByOrNull { it.requiredTotalXp } ?: POTENTIAL
        }

        fun getNextRank(currentRank: SlayerRank): SlayerRank? {
            val idx = values().indexOf(currentRank)
            return if (idx < values().size - 1) values()[idx + 1] else null
        }
    }
}

data class RankUpAlert(
    val newRank: SlayerRank,
    val bonusTokens: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class PlayerStats(
    val level: Int = 1,
    val xp: Int = 0,
    val xpToNextLevel: Int = 100,
    val totalXp: Int = 0,
    val currentHp: Int = 100,
    val maxHp: Int = 100,
    val energy: Int = 40, // 0..100 Slayer Adrenaline
    val stamina: Int = 85, // 0..100 Stamina level
    val maxStamina: Int = 100,
    val occultTokens: Int = 60,
    val martialPower: Int = 10,
    val agilityParryBonus: Int = 5,
    val vampiresDusted: Int = 0,
    val demonsVanquished: Int = 0,
    val civiliansSaved: Int = 0,
    val nightsSurvived: Int = 1,
    val shieldRemaining: Int = 0,
    val triviaStreak: Int = 0,
    val bestTriviaStreak: Int = 0,
    val triviaCorrectCount: Int = 0,
    val watcherRank: String = "Scooby Initiate",
    val slayerRank: SlayerRank = SlayerRank.POTENTIAL
)

data class CombatLogEntry(
    val id: Long = nextCombatLogId(),
    val text: String,
    val isCritical: Boolean = false,
    val isBuffyQuote: Boolean = false,
    val isEnemy: Boolean = false
) {
    companion object {
        private val idCounter = java.util.concurrent.atomic.AtomicLong(System.currentTimeMillis())
        fun nextCombatLogId(): Long = idCounter.incrementAndGet()
    }
}

data class BestiaryEntry(
    val enemyType: EnemyType,
    val timesDefeated: Int,
    val isEncountered: Boolean
)

