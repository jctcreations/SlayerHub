package com.example.data

import com.example.model.*
import kotlin.random.Random

object NightPatrolEventFactory {

    fun generateRandomEvent(): NightPatrolEvent {
        val types = NightEventType.values()
        val pickedType = types.random()
        return createEvent(pickedType)
    }

    fun createEvent(type: NightEventType): NightPatrolEvent {
        val options = when (type) {
            NightEventType.GRAVEYARD_STAKEOUT -> listOf(
                TacticalOption(
                    type = TacticalApproachType.STEALTH_AMBUSH,
                    title = "Stealth Shadow Staking",
                    description = "Crawl silently behind the mossy tombstones and drive Mr. Pointy into the lead vampire's back.",
                    requiredStat = "Agility >= 8 • 15 Stamina",
                    successRatePercent = 78,
                    bonusRewardMultiplier = 1.35f,
                    staminaCost = 15,
                    outcomeVictoryText = "SUCCESS! Buffy glided like a shadow. Before the lead vampire could turn, Mr. Pointy found his heart—exploding into a shower of glowing ash! The remaining vamps scattered into the night.",
                    outcomeFailureText = "A twig snapped beneath Buffy's boot! The vampire whirled around with bared fangs—you're locked in a frantic close-quarters skirmish!"
                ),
                TacticalOption(
                    type = TacticalApproachType.HOLY_WARD_TRAP,
                    title = "Consecrated Perimeter Traps",
                    description = "Set blessed holy water runes and garlic stakes around crypt exits, burning the coven as they emerge.",
                    requiredStat = "Holy Water or 10 Tokens",
                    successRatePercent = 88,
                    bonusRewardMultiplier = 1.20f,
                    staminaCost = 10,
                    outcomeVictoryText = "BRILLIANT TACTICS! The fleeing vampires stepped directly into the holy water perimeter. Blue flames and searing steam dissolved the nest in seconds!",
                    outcomeFailureText = "One of the younger vampires knocked over the vial before the rune activated! Direct combat engaged!"
                ),
                TacticalOption(
                    type = TacticalApproachType.FULL_SLAYER_ASSAULT,
                    title = "Frontal Slayer Blitz",
                    description = "Kick down the iron crypt gates and unleash a whirlwind martial arts assault!",
                    requiredStat = "Martial Power >= 10 • 20 Stamina",
                    successRatePercent = 95,
                    bonusRewardMultiplier = 1.10f,
                    staminaCost = 20,
                    outcomeVictoryText = "SAVAGE DISPLAY! Buffy crashed through the iron gates with a spinning roundhouse, sending the vampire enforcers reeling before dusting them in succession!",
                    outcomeFailureText = "The vampires swarmed in tight quarters, landing several heavy claws before you banished them!"
                )
            )

            NightEventType.HELLMOUTH_BREACH -> listOf(
                TacticalOption(
                    type = TacticalApproachType.MYSTICAL_SEAL,
                    title = "Willow's Wiccan Dimensional Ward",
                    description = "Protect Willow while she channels an ancient banishing circle to close the subterranean fissure.",
                    requiredStat = "Willow Ready • 25 Stamina",
                    successRatePercent = 82,
                    bonusRewardMultiplier = 1.50f,
                    staminaCost = 25,
                    outcomeVictoryText = "MAGICAL TRIUMPH! 'Terra, ignis, spiritus!' Willow's glowing purple barrier sealed the dimensional rift shut, banishing the Hellmouth entities back into the void!",
                    outcomeFailureText = "A surging sulfur blast disrupted Willow's incantation! An elite demon emerged from the portal to duel you!"
                ),
                TacticalOption(
                    type = TacticalApproachType.WATCHER_DECRYPTION,
                    title = "Giles' Pergamum Binding Chant",
                    description = "Recite the ancient Sumerian binding rites to invert the dimensional vortex.",
                    requiredStat = "Occult Wisdom (Tier 3+) • 15 Stamina",
                    successRatePercent = 85,
                    bonusRewardMultiplier = 1.35f,
                    staminaCost = 15,
                    outcomeVictoryText = "ANCIENT WISDOM! Chanting the Sumerian phrases from the Pergamum Codex reversed the vortex, sucking the hell creatures back into their nether-realm!",
                    outcomeFailureText = "A translation error in the ancient dialect caused a backlash of brimstone! Direct combat triggered!"
                ),
                TacticalOption(
                    type = TacticalApproachType.FULL_SLAYER_ASSAULT,
                    title = "Slayer Scythe Portal Charge",
                    description = "Leap into the swirling sulfurous portal chamber and decapitate the dimensional breach guardian!",
                    requiredStat = "Martial Power >= 14 • 30 Stamina",
                    successRatePercent = 75,
                    bonusRewardMultiplier = 1.60f,
                    staminaCost = 30,
                    outcomeVictoryText = "LEGENDARY FEAT! Buffy vaulted over the flaming chasm, driving her weapon deep into the rift core. The shockwave slammed the portal shut in a burst of hellfire embers!",
                    outcomeFailureText = "The portal guardian caught the strike and hurled Buffy into the stone pillars! Boss encounter triggered!"
                )
            )

            NightEventType.BRONZE_ALLEY_AMBUSH -> listOf(
                TacticalOption(
                    type = TacticalApproachType.DISTRACTION_DECOY,
                    title = "Xander's Firecracker Decoy",
                    description = "Have Xander ignite a box of bootleg Roman candles to disorient the vampires while teens flee.",
                    requiredStat = "Xander Ready • 10 Stamina",
                    successRatePercent = 90,
                    bonusRewardMultiplier = 1.25f,
                    staminaCost = 10,
                    outcomeVictoryText = "PERFECT RESCUE! 'Hey bat-face, catch!' Xander's pyrotechnics lit up the alley in screaming colors. The teens escaped unharmed while Buffy dusted the stunned vampires!",
                    outcomeFailureText = "Xander tripped over a trash can! Buffy had to leap into the fray to intercept the vampire claws!"
                ),
                TacticalOption(
                    type = TacticalApproachType.STEALTH_AMBUSH,
                    title = "Fire Escape Acrobatic Drop",
                    description = "Scale the brick fire escape and drop down onto the vampire lieutenant with an airborne stake strike.",
                    requiredStat = "Agility >= 10 • 18 Stamina",
                    successRatePercent = 84,
                    bonusRewardMultiplier = 1.35f,
                    staminaCost = 18,
                    outcomeVictoryText = "CINEMATIC TAKEDOWN! Buffy plunged from 15 feet up, driving her oak stake cleanly through the lead vampire's ribcage. He turned to ash before he hit the asphalt!",
                    outcomeFailureText = "The fire escape ladder squeaked loudly. The vampires grabbed a student as a shield!"
                ),
                TacticalOption(
                    type = TacticalApproachType.FULL_SLAYER_ASSAULT,
                    title = "Direct Slayer Showdown",
                    description = "Step between the vampires and the students: 'If you touch them, you won't even leave ashes.'",
                    requiredStat = "Level 2+ • 15 Stamina",
                    successRatePercent = 92,
                    bonusRewardMultiplier = 1.15f,
                    staminaCost = 15,
                    outcomeVictoryText = "HEROIC STAND! Buffy shielded the terrified teens and dismantled the three vampires with lightning-fast kicks and dual stakes. Students rescued safely!",
                    outcomeFailureText = "The brawl was chaotic. Buffy took a glancing blow to protect the students before staking the monsters."
                )
            )

            NightEventType.OCCULT_RITUAL_SABOTAGE -> listOf(
                TacticalOption(
                    type = TacticalApproachType.MYSTICAL_SEAL,
                    title = "Disrupt Blood Sigil Altar",
                    description = "Smash the dark obsidian focus stone before the chant reaches the crescendo.",
                    requiredStat = "20 Stamina • Agility >= 9",
                    successRatePercent = 82,
                    bonusRewardMultiplier = 1.30f,
                    staminaCost = 20,
                    outcomeVictoryText = "RITUAL SHATTERED! Buffy's roundhouse shattered the obsidian altar into glowing shards. The dark magic imploded, consuming the cultists in their own sulfur!",
                    outcomeFailureText = "The blood orb pulsed with dark kinetic energy, blowing Buffy back as the demon emerged!"
                ),
                TacticalOption(
                    type = TacticalApproachType.STEALTH_AMBUSH,
                    title = "Angel's Subterranean Flank",
                    description = "Coordinate with Angel to ambush the lead summoner from the sewer shadows.",
                    requiredStat = "Angel Ready • 15 Stamina",
                    successRatePercent = 88,
                    bonusRewardMultiplier = 1.40f,
                    staminaCost = 15,
                    outcomeVictoryText = "DEVASTATING PINSER! Angel dropped from the sewer rafters while Buffy swept the flanks. The demon acolytes stood zero chance against the dual attack!",
                    outcomeFailureText = "The acolytes detected Angel's undead heartbeat and cast a dark snare spell!"
                ),
                TacticalOption(
                    type = TacticalApproachType.HOLY_WARD_TRAP,
                    title = "Holy Flare Flashbang",
                    description = "Throw a blessed magnesium flare into the sewer chamber to blind the dark vision of the demons.",
                    requiredStat = "15 Tokens or Holy Water",
                    successRatePercent = 92,
                    bonusRewardMultiplier = 1.25f,
                    staminaCost = 10,
                    outcomeVictoryText = "SEARING BLINDNESS! Searing holy light flooded the underground tunnel. The demons shrieked in agony, dropping their ritual daggers as Buffy dusted them!",
                    outcomeFailureText = "The dense sewer fog muffled the flare's radius, leaving two cultists armed!"
                )
            )

            NightEventType.VAMPIRE_NEST_RAID -> listOf(
                TacticalOption(
                    type = TacticalApproachType.STEALTH_AMBUSH,
                    title = "Rapid Multi-Staking Sprint",
                    description = "Sprint along the sarcophagus rows staking the bloodsuckers before they rise.",
                    requiredStat = "Agility >= 12 • 25 Stamina",
                    successRatePercent = 80,
                    bonusRewardMultiplier = 1.45f,
                    staminaCost = 25,
                    outcomeVictoryText = "STAKING FRENZY! Stake, dust, stake, dust! Buffy dusted four vampires in four seconds before they could even open their eyes!",
                    outcomeFailureText = "A stone coffin lid jammed! The noise woke the rest of the nest!"
                ),
                TacticalOption(
                    type = TacticalApproachType.HOLY_WARD_TRAP,
                    title = "Sunlight Mirror Array",
                    description = "Rig Giles' polished silver mirrors to funnel early morning dawn rays down the crypt shaft.",
                    requiredStat = "Giles Advice • 15 Stamina",
                    successRatePercent = 94,
                    bonusRewardMultiplier = 1.30f,
                    staminaCost = 15,
                    outcomeVictoryText = "DAWN'S PURGE! A beam of pure Californian sunlight ricocheted down the mirrors, igniting the nest in a blinding burst of cleansing fire!",
                    outcomeFailureText = "Cloud cover blocked the sunbeam at the critical second!"
                ),
                TacticalOption(
                    type = TacticalApproachType.FULL_SLAYER_ASSAULT,
                    title = "Heavy Scythe Sweep",
                    description = "Smash open the sarcophagi and fight the entire brood in hand-to-hand combat.",
                    requiredStat = "Martial Power >= 12 • 20 Stamina",
                    successRatePercent = 85,
                    bonusRewardMultiplier = 1.20f,
                    staminaCost = 20,
                    outcomeVictoryText = "EPIC MELEE! Buffy cleared the entire underground chamber with sweeping scythe strikes and roundhouse kicks, leaving only a thick layer of dust!",
                    outcomeFailureText = "The sheer numbers overwhelmed your defenses, but you clawed your way out with heavy bruises."
                )
            )
        }

        return NightPatrolEvent(
            type = type,
            tacticalOptions = options,
            expiresTimerSeconds = 180
        )
    }
}
