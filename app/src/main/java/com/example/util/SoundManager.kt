package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

object SoundManager {
    private const val SAMPLE_RATE = 22050
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _isSfxEnabled = MutableStateFlow(true)
    val isSfxEnabled: StateFlow<Boolean> = _isSfxEnabled.asStateFlow()

    private val _isAmbianceEnabled = MutableStateFlow(true)
    val isAmbianceEnabled: StateFlow<Boolean> = _isAmbianceEnabled.asStateFlow()

    private var ambianceJob: Job? = null
    private var ambianceTrack: AudioTrack? = null

    fun toggleSfx() {
        _isSfxEnabled.value = !_isSfxEnabled.value
    }

    fun toggleAmbiance() {
        val next = !_isAmbianceEnabled.value
        _isAmbianceEnabled.value = next
        if (next) {
            startAmbiance()
        } else {
            stopAmbiance()
        }
    }

    fun startAmbiance() {
        if (!_isAmbianceEnabled.value || ambianceJob != null) return

        ambianceJob = scope.launch {
            try {
                val bufferDurationSec = 3.0
                val totalSamples = (SAMPLE_RATE * bufferDurationSec).toInt()
                val buffer = ShortArray(totalSamples)

                // Synthesize eerie low supernatural ambient drone (Sunnydale night wind & Hellmouth frequency)
                val freq1 = 55.0  // A1
                val freq2 = 82.4  // E2 (Fifth)
                val freq3 = 110.0 // A2

                for (i in 0 until totalSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    // Slow undulating wave
                    val lfo = 0.7 + 0.3 * sin(2.0 * PI * 0.25 * t)
                    val s1 = sin(2.0 * PI * freq1 * t)
                    val s2 = sin(2.0 * PI * freq2 * t + 0.5) * 0.6
                    val s3 = sin(2.0 * PI * freq3 * t) * 0.3
                    val windNoise = (Random.nextFloat() * 2.0 - 1.0) * 0.08

                    val mixed = (s1 + s2 + s3 + windNoise) * lfo * 0.15
                    val clamped = mixed.coerceIn(-1.0, 1.0)
                    buffer[i] = (clamped * Short.MAX_VALUE).toInt().toShort()
                }

                val minBufSize = AudioTrack.getMinBufferSize(
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(maxOf(minBufSize, buffer.size * 2))
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                ambianceTrack = track
                track.play()

                while (isActive && _isAmbianceEnabled.value) {
                    track.write(buffer, 0, buffer.size)
                }
            } catch (_: Exception) {
                // Silently handle if audio focus or device output unavailable
            }
        }
    }

    fun stopAmbiance() {
        ambianceJob?.cancel()
        ambianceJob = null
        try {
            ambianceTrack?.stop()
            ambianceTrack?.release()
        } catch (_: Exception) {}
        ambianceTrack = null
    }

    fun playPunch() {
        if (!_isSfxEnabled.value) return
        scope.launch {
            // Low thumping punch sound: 140Hz descending to 45Hz with quick decay
            val durationSec = 0.14
            val samples = (SAMPLE_RATE * durationSec).toInt()
            val buffer = ShortArray(samples)

            for (i in 0 until samples) {
                val t = i.toDouble() / SAMPLE_RATE
                val currentFreq = 140.0 - (95.0 * (i.toDouble() / samples))
                val env = exp(-t * 22.0)
                val sample = sin(2.0 * PI * currentFreq * t) * env * 0.9
                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
            }
            playRawBuffer(buffer)
        }
    }

    fun playDusting() {
        if (!_isSfxEnabled.value) return
        scope.launch {
            // Dramatic dusting sound: initial stake crack followed by prolonged sizzle and crumble
            val durationSec = 0.75
            val samples = (SAMPLE_RATE * durationSec).toInt()
            val buffer = ShortArray(samples)

            for (i in 0 until samples) {
                val t = i.toDouble() / SAMPLE_RATE
                // White noise shaped with crackle
                val noise = (Random.nextFloat() * 2.0 - 1.0)
                val stakeImpact = if (t < 0.08) sin(2.0 * PI * 220.0 * t) * exp(-t * 30.0) * 0.8 else 0.0
                val sizzle = noise * exp(-t * 3.8) * 0.65
                val mixed = (stakeImpact + sizzle).coerceIn(-1.0, 1.0)
                buffer[i] = (mixed * Short.MAX_VALUE).toInt().toShort()
            }
            playRawBuffer(buffer)
        }
    }

    fun playParry() {
        if (!_isSfxEnabled.value) return
        scope.launch {
            // High metallic clash: 980Hz and 1320Hz bell harmonics with fast ring
            val durationSec = 0.28
            val samples = (SAMPLE_RATE * durationSec).toInt()
            val buffer = ShortArray(samples)

            for (i in 0 until samples) {
                val t = i.toDouble() / SAMPLE_RATE
                val env = exp(-t * 14.0)
                val s1 = sin(2.0 * PI * 980.0 * t)
                val s2 = sin(2.0 * PI * 1320.0 * t) * 0.7
                val s3 = sin(2.0 * PI * 2200.0 * t) * 0.3
                val sample = (s1 + s2 + s3) * env * 0.55
                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
            }
            playRawBuffer(buffer)
        }
    }

    fun playTriviaCorrect() {
        if (!_isSfxEnabled.value) return
        scope.launch {
            // Triumphant ascending harmonic arpeggio: C5 (523Hz), E5 (659Hz), G5 (784Hz), C6 (1046Hz)
            val noteDuration = 0.085
            val totalNotes = 4
            val frequencies = doubleArrayOf(523.25, 659.25, 783.99, 1046.50)
            val samplesPerNote = (SAMPLE_RATE * noteDuration).toInt()
            val buffer = ShortArray(samplesPerNote * totalNotes)

            for (n in 0 until totalNotes) {
                val freq = frequencies[n]
                val offset = n * samplesPerNote
                for (i in 0 until samplesPerNote) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val env = exp(-t * 8.0) * (1.0 - (i.toDouble() / samplesPerNote * 0.2))
                    val sample = sin(2.0 * PI * freq * t) * env * 0.65
                    buffer[offset + i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
                }
            }
            playRawBuffer(buffer)
        }
    }

    fun playTriviaWrong() {
        if (!_isSfxEnabled.value) return
        scope.launch {
            // Low, buzzing dissonant tone: 130Hz + 175Hz with harsh square/sawtooth shape
            val durationSec = 0.32
            val samples = (SAMPLE_RATE * durationSec).toInt()
            val buffer = ShortArray(samples)

            for (i in 0 until samples) {
                val t = i.toDouble() / SAMPLE_RATE
                val env = exp(-t * 6.5)
                val wave1 = if (sin(2.0 * PI * 130.0 * t) > 0) 0.5 else -0.5
                val wave2 = if (sin(2.0 * PI * 175.0 * t) > 0) 0.4 else -0.4
                val sample = (wave1 + wave2) * env * 0.5
                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
            }
            playRawBuffer(buffer)
        }
    }

    fun playLevelUp() {
        if (!_isSfxEnabled.value) return
        scope.launch {
            // Fanfare chords
            val notes = doubleArrayOf(440.0, 554.37, 659.25, 880.0)
            val noteDuration = 0.12
            val samplesPerNote = (SAMPLE_RATE * noteDuration).toInt()
            val buffer = ShortArray(samplesPerNote * notes.size)

            for (n in notes.indices) {
                val freq = notes[n]
                val offset = n * samplesPerNote
                for (i in 0 until samplesPerNote) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val env = exp(-t * 5.0)
                    val sample = sin(2.0 * PI * freq * t) * env * 0.7
                    buffer[offset + i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
                }
            }
            playRawBuffer(buffer)
        }
    }

    private fun playRawBuffer(buffer: ShortArray) {
        try {
            val minBufSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(maxOf(minBufSize, buffer.size * 2))
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            scope.launch {
                val durationMs = (buffer.size * 1000L) / SAMPLE_RATE + 100L
                kotlinx.coroutines.delay(durationMs)
                try {
                    track.stop()
                    track.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
    }
}
