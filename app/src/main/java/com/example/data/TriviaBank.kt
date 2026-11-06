package com.example.data

import com.example.model.TriviaDifficulty
import com.example.model.TriviaQuestion

object TriviaBank {
    val questions: List<TriviaQuestion> = listOf(
        // Tier 1: NOVICE (Streak 0-2)
        TriviaQuestion(
            id = 1,
            question = "What is the name of the popular teen nightclub and music venue in Sunnydale?",
            options = listOf("The Bronze", "The Hellmouth Lounge", "Sunnydale Tavern", "Vampire's Bite"),
            correctIndex = 0,
            explanation = "The Bronze is Sunnydale's local live-music club where Buffy and her friends spend their nights.",
            difficulty = TriviaDifficulty.NOVICE
        ),
        TriviaQuestion(
            id = 2,
            question = "What is the day job of Buffy's Watcher, Rupert Giles, at Sunnydale High?",
            options = listOf("School Librarian", "History Teacher", "Vice Principal", "Guidance Counselor"),
            correctIndex = 0,
            explanation = "Giles serves as the school librarian, perfectly positioned above the library's occult stacks.",
            difficulty = TriviaDifficulty.NOVICE
        ),
        TriviaQuestion(
            id = 3,
            question = "What is Faith's favorite trademark wooden stake called, which Buffy later uses?",
            options = listOf("Mr. Pointy", "Stake-o-Matic", "Woody", "The Fang Poker"),
            correctIndex = 0,
            explanation = "Faith affectionately named her favorite carved wooden stake 'Mr. Pointy'.",
            difficulty = TriviaDifficulty.NOVICE
        ),
        TriviaQuestion(
            id = 4,
            question = "Which vampire has a gypsy curse that restored his human soul and conscience?",
            options = listOf("Angel", "Spike", "The Master", "Harmony"),
            correctIndex = 0,
            explanation = "The Kalderash Romani clan cursed Angelus with a human soul so he would suffer remorse for his atrocities.",
            difficulty = TriviaDifficulty.NOVICE
        ),
        TriviaQuestion(
            id = 5,
            question = "What happens to vampires in the Buffyverse when they are staked through the heart?",
            options = listOf("They turn to ash / dust", "They turn into bats", "They bleed silver", "They freeze in stone"),
            correctIndex = 0,
            explanation = "Vampires in Buffy explode into a crumbling heap of glowing dust and ash.",
            difficulty = TriviaDifficulty.NOVICE
        ),

        // Tier 2: SCOOBY (Streak 3-5)
        TriviaQuestion(
            id = 6,
            question = "What irrational creature fear does the ex-vengeance demon Anya Jenkins have?",
            options = listOf("Bunnies", "Puppies", "Butterflies", "Squirrels"),
            correctIndex = 0,
            explanation = "Anya has a famous, hilarious phobia of rabbits ('Bunnies aren't just cute like everybody supposes!').",
            difficulty = TriviaDifficulty.SCOOBY
        ),
        TriviaQuestion(
            id = 7,
            question = "What was Rupert Giles' rebellious rebellious nickname during his dark youth in England?",
            options = listOf("Ripper", "Blackthorn", "Dark Watcher", "Grim Giles"),
            correctIndex = 0,
            explanation = "In his reckless youth dabbling in dark magic, Giles was known by his peers as 'Ripper'.",
            difficulty = TriviaDifficulty.SCOOBY
        ),
        TriviaQuestion(
            id = 8,
            question = "What animal did Daniel 'Oz' Osbourne transform into each full moon?",
            options = listOf("Werewolf", "Hellhound", "Vampire Bat", "Hyena"),
            correctIndex = 0,
            explanation = "Oz was a laid-back guitarist who transformed into a werewolf during three nights of the full moon.",
            difficulty = TriviaDifficulty.SCOOBY
        ),
        TriviaQuestion(
            id = 9,
            question = "Who was Sunnydale's seemingly wholesome mayor who attempted demonic Ascension at graduation?",
            options = listOf("Richard Wilkins III", "Quentin Travers", "Principal Snyder", "Ted Buchanan"),
            correctIndex = 0,
            explanation = "Mayor Richard Wilkins had governed Sunnydale for over a century, planning to ascend into the pure demon Olvikan.",
            difficulty = TriviaDifficulty.SCOOBY
        ),
        TriviaQuestion(
            id = 10,
            question = "What was the name of the magical shop Rupert Giles bought and operated in Season 5?",
            options = listOf("The Magic Box", "Ye Olde Curiosity Shoppe", "Sunnydale Spells & Tomes", "The Occult Emporium"),
            correctIndex = 0,
            explanation = "Giles bought and managed 'The Magic Box', which became the Scooby Gang's new meeting ground.",
            difficulty = TriviaDifficulty.SCOOBY
        ),

        // Tier 3: WATCHER_TRAINEE (Streak 6-8)
        TriviaQuestion(
            id = 11,
            question = "What is the secret military demon-hunting organization stationed beneath UC Sunnydale?",
            options = listOf("The Initiative", "Project Hellmouth", "Sector 7", "The Watchers Black Ops"),
            correctIndex = 0,
            explanation = "The Initiative was an undercover government operation researching and capturing subterranean demons.",
            difficulty = TriviaDifficulty.WATCHER_TRAINEE
        ),
        TriviaQuestion(
            id = 12,
            question = "Who was revealed to be a mystical 'Key' woven into human form as Buffy's younger sister?",
            options = listOf("Dawn Summers", "Tara Maclay", "Amy Madison", "Cordelia Chase"),
            correctIndex = 0,
            explanation = "Czech monks transformed an ancient living energy Key into Dawn, implanting memories in everyone.",
            difficulty = TriviaDifficulty.WATCHER_TRAINEE
        ),
        TriviaQuestion(
            id = 13,
            question = "What is the true identity and divine origin of the glorious villain Glorificus ('Glory')?",
            options = listOf("A Hell God banished from her dimension", "An ancient First Slayer", "The Queen of Vampires", "An archangel corrupted by darkness"),
            correctIndex = 0,
            explanation = "Glory was a petulant, immensely powerful Hell God banished to Earth and sharing a body with human Ben.",
            difficulty = TriviaDifficulty.WATCHER_TRAINEE
        ),
        TriviaQuestion(
            id = 14,
            question = "In the critically acclaimed Season 4 episode 'Hush', what did the Gentlemen steal from Sunnydale?",
            options = listOf("Everyone's voices", "Everyone's sight", "All reflections", "The town's shadows"),
            correctIndex = 0,
            explanation = "The Gentlemen locked all voices in a box so nobody could scream while they harvested human hearts.",
            difficulty = TriviaDifficulty.WATCHER_TRAINEE
        ),
        TriviaQuestion(
            id = 15,
            question = "Which powerful spellcaster and love of Willow's life helped her master benign wiccan arts before tragedy struck?",
            options = listOf("Tara Maclay", "Kennedy", "Amy Madison", "Cassie Newton"),
            correctIndex = 0,
            explanation = "Tara was a gifted witch whose gentle nature and genuine magical prowess anchored Willow.",
            difficulty = TriviaDifficulty.WATCHER_TRAINEE
        ),

        // Tier 4: WATCHER_COUNCIL (Streak 9-11)
        TriviaQuestion(
            id = 16,
            question = "What musical demon summoned the singing curse upon Sunnydale in 'Once More, with Feeling'?",
            options = listOf("Sweet", "Lorne", "Klaatu", "Balthazar"),
            correctIndex = 0,
            explanation = "Sweet, a dapper tap-dancing demon of jazz and flame, made everyone sing their deepest secrets.",
            difficulty = TriviaDifficulty.WATCHER_COUNCIL
        ),
        TriviaQuestion(
            id = 17,
            question = "What was the name of the ancient stone demon statue that Angelus attempted to awaken in Season 2 to suck the world into hell?",
            options = listOf("Acathla", "Illyria", "Olvikan", "D'Hoffryn"),
            correctIndex = 0,
            explanation = "Acathla could only be awakened by the blood of a worthy creature, requiring Buffy to run Angel through with a sword.",
            difficulty = TriviaDifficulty.WATCHER_COUNCIL
        ),
        TriviaQuestion(
            id = 18,
            question = "In the Season 3 episode 'The Wish', what alternate universe reality was created when Cordelia made a wish to Anyanka?",
            options = listOf("Buffy never came to Sunnydale, so vampires conquered the town", "Xander was the Slayer", "Giles died in high school", "Sunnydale had no night"),
            correctIndex = 0,
            explanation = "Cordelia wished Buffy had never come to Sunnydale, resulting in Vamp-Willow and Vamp-Xander ruling the night.",
            difficulty = TriviaDifficulty.WATCHER_COUNCIL
        ),
        TriviaQuestion(
            id = 19,
            question = "Who comprised the bumbling villain nerd collective known as 'The Trio' in Season 6?",
            options = listOf("Warren, Jonathan, and Andrew", "Tucker, Jonathan, and Pete", "Warren, Rack, and Larry", "Andrew, Clem, and Ethan"),
            correctIndex = 0,
            explanation = "Warren Mears, Jonathan Levinson, and Andrew Wells formed The Trio to conquer Sunnydale with gadgets and sorcery.",
            difficulty = TriviaDifficulty.WATCHER_COUNCIL
        ),
        TriviaQuestion(
            id = 20,
            question = "Where did Spike travel in Africa to endure trials and win back his soul for Buffy?",
            options = listOf("A subterranean demon cave with a fiery demon priest", "Mount Kilimanjaro", "An ancient crypt in Cairo", "The Temple of Isis"),
            correctIndex = 0,
            explanation = "Spike endured grueling trials in a demon cave in Africa, willingly demanding the restoration of his soul.",
            difficulty = TriviaDifficulty.WATCHER_COUNCIL
        ),

        // Tier 5: HELLMOUTH_MASTER (Streak 12+)
        TriviaQuestion(
            id = 21,
            question = "What ancient mystical weapon forged by the female guardians did Buffy unearth in a crypt to defeat Caleb and the First?",
            options = listOf("The Scythe (M#)", "The Sword of Acathla", "The Amulet of Alamar", "The Dagger of D'Hoffryn"),
            correctIndex = 0,
            explanation = "The Scythe was forged in secret by the Guardians for the Slayer, inscribed with ancient power.",
            difficulty = TriviaDifficulty.HELLMOUTH_MASTER
        ),
        TriviaQuestion(
            id = 22,
            question = "What was the street address of the Summers household in Sunnydale?",
            options = listOf("1630 Revello Drive", "1428 Elm Street", "1240 Crestview Lane", "1801 Sunnydale Boulevard"),
            correctIndex = 0,
            explanation = "Buffy, Joyce, and Dawn lived at 1630 Revello Drive throughout the series.",
            difficulty = TriviaDifficulty.HELLMOUTH_MASTER
        ),
        TriviaQuestion(
            id = 23,
            question = "What was the name of the demon lord of vengeance who originally turned Anya into Anyanka after her fiancé cheated on her?",
            options = listOf("D'Hoffryn", "Kulak of the Miquot", "Torg", "Lurconis"),
            correctIndex = 0,
            explanation = "D'Hoffryn offered Aud (Anya) the vengeance amulet after she turned her philandering lover Dobbler into a troll.",
            difficulty = TriviaDifficulty.HELLMOUTH_MASTER
        ),
        TriviaQuestion(
            id = 24,
            question = "Which ancient Sumerian codex did Giles frequently reference to translate apocalyptic prophecies?",
            options = listOf("The Pergamum Codex", "The Codex of Alhazred", "The Book of Tarakan", "The Chronicles of Aurelius"),
            correctIndex = 0,
            explanation = "The Pergamum Codex was a crucial ancient manuscript deciphered by Giles.",
            difficulty = TriviaDifficulty.HELLMOUTH_MASTER
        ),
        TriviaQuestion(
            id = 25,
            question = "In Season 7, who brought the mystical champion's Amulet to Sunnydale from Wolfram & Hart that Spike used to seal the Hellmouth?",
            options = listOf("Angel", "Wesley Wyndam-Pryce", "Gunn", "Lorne"),
            correctIndex = 0,
            explanation = "Angel brought the mystical amulet to Sunnydale, which Spike wore to channel sunlight and destroy the Turok-Han army.",
            difficulty = TriviaDifficulty.HELLMOUTH_MASTER
        )
    )

    fun getQuestionForStreak(streak: Int, alreadyAnsweredIds: Set<Int>): TriviaQuestion {
        val targetTier = when {
            streak >= TriviaDifficulty.HELLMOUTH_MASTER.minStreak -> TriviaDifficulty.HELLMOUTH_MASTER
            streak >= TriviaDifficulty.WATCHER_COUNCIL.minStreak -> TriviaDifficulty.WATCHER_COUNCIL
            streak >= TriviaDifficulty.WATCHER_TRAINEE.minStreak -> TriviaDifficulty.WATCHER_TRAINEE
            streak >= TriviaDifficulty.SCOOBY.minStreak -> TriviaDifficulty.SCOOBY
            else -> TriviaDifficulty.NOVICE
        }

        val pool = questions.filter { it.difficulty == targetTier && it.id !in alreadyAnsweredIds }
        return if (pool.isNotEmpty()) {
            pool.random()
        } else {
            // Fallback to any unasked or random in tier
            questions.filter { it.difficulty == targetTier }.randomOrNull() ?: questions.random()
        }
    }
}
