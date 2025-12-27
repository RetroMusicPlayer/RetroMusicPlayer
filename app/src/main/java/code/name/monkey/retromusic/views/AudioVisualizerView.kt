/*
 * Copyright (c) 2025 RetroMusicPlayer Contributors
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.views

import android.content.Context
import android.graphics.Color
import android.media.audiofx.Visualizer
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import code.name.monkey.retromusic.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * GLSL-based audio visualizer view that displays frequency bars
 * Uses Android Visualizer API for audio data and OpenGL ES 2.0 for rendering
 */
class AudioVisualizerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs), GLSurfaceView.Renderer {

    private var visualizer: Visualizer? = null
    private var audioSessionId: Int = 0

    // Frequency bar data
    private val barData = FloatArray(NUM_BARS)
    private val smoothedBarData = FloatArray(NUM_BARS)

    // OpenGL resources
    private var shaderProgram: Int = 0
    private var vertexBuffer: FloatBuffer? = null
    private var colorBuffer: FloatBuffer? = null

    // Color configuration
    private var primaryColor: Int = Color.WHITE
    private var secondaryColor: Int = Color.WHITE

    // Frame timing
    private var lastFrameTime = 0L

    // State
    private var isInitialized = false
    private var isPaused = false

    companion object {
        private const val TAG = "AudioVisualizerView"
        private const val NUM_BARS = 32
        private const val SMOOTHING_FACTOR = 0.7f
        private const val FRAME_INTERVAL_MS = 16L // ~60 FPS
        private const val MIN_CAPTURE_RATE = 10000 // 10ms minimum

        fun isSupported(): Boolean {
            return try {
                // Try to get capture size range to check if Visualizer is available
                val captureSizeRange = Visualizer.getCaptureSizeRange()
                captureSizeRange != null && captureSizeRange.isNotEmpty()
            } catch (e: Exception) {
                Log.e(TAG, "Visualizer not supported", e)
                false
            } catch (e: UnsatisfiedLinkError) {
                Log.e(TAG, "Visualizer native library not found", e)
                false
            }
        }
    }

    init {
        Log.d(TAG, "AudioVisualizerView init block called")
        try {
            setEGLContextClientVersion(2)
            Log.d(TAG, "EGL context version set to 2")
            setRenderer(this)
            Log.d(TAG, "Renderer set")
            renderMode = RENDERMODE_CONTINUOUSLY
            Log.d(TAG, "Render mode set to CONTINUOUSLY")
        } catch (e: Exception) {
            Log.e(TAG, "Error in init block", e)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "onAttachedToWindow called, visibility=$visibility, width=$width, height=$height")
    }

    override fun onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow called")
        super.onDetachedFromWindow()
        release()

        // Clean up OpenGL resources
        if (shaderProgram != 0) {
            GLES20.glDeleteProgram(shaderProgram)
            shaderProgram = 0
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout called: changed=$changed, size=${right-left}x${bottom-top}, visibility=$visibility")
    }

    // ========== Public API ==========

    /**
     * Initialize visualizer with audio session ID
     */
    fun initialize(audioSessionId: Int) {
        Log.d(TAG, "initialize called with audioSessionId=$audioSessionId")
        if (isInitialized) {
            Log.d(TAG, "Already initialized, releasing first")
            release()
        }

        this.audioSessionId = audioSessionId

        try {
            // Check if visualizer is supported
            if (!isSupported()) {
                Log.w(TAG, "Visualizer not supported on this device")
                visibility = GONE
                return
            }

            visualizer = Visualizer(audioSessionId).apply {
                // Set capture size to maximum available
                val captureSizeRange = Visualizer.getCaptureSizeRange()
                captureSize = min(captureSizeRange[1], 1024)
                Log.d(TAG, "Visualizer captureSize set to: $captureSize")

                // Set data capture listener for FFT data
                val maxRate = Visualizer.getMaxCaptureRate()
                val captureRate = max(MIN_CAPTURE_RATE, maxRate / 2)
                Log.d(TAG, "Visualizer captureRate set to: $captureRate (max=$maxRate)")

                setDataCaptureListener(
                    captureListener,
                    captureRate,
                    false, // waveform
                    true   // FFT
                )

                enabled = true
                Log.d(TAG, "Visualizer enabled=${this.enabled}")
            }

            isInitialized = true
            isPaused = false
            Log.d(TAG, "Visualizer initialized successfully with session ID: $audioSessionId")

        } catch (e: IllegalStateException) {
            Log.e(TAG, "Failed to initialize visualizer: already in use", e)
            visibility = GONE
        } catch (e: RuntimeException) {
            Log.e(TAG, "Failed to initialize visualizer", e)
            visibility = GONE
        }
    }

    /**
     * Release visualizer resources
     */
    fun release() {
        try {
            visualizer?.apply {
                enabled = false
                release()
            }
            visualizer = null
            isInitialized = false
            Log.d(TAG, "Visualizer released")
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing visualizer", e)
        }
    }

    /**
     * Set colors for visualizer bars
     */
    fun setColors(primary: Int, secondary: Int) {
        this.primaryColor = primary
        this.secondaryColor = secondary
        updateColorBuffer()
    }

    /**
     * Pause visualizer rendering
     */
    fun pause() {
        isPaused = true
        visualizer?.enabled = false
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    /**
     * Resume visualizer rendering
     */
    fun resume() {
        if (isInitialized) {
            isPaused = false
            visualizer?.enabled = true
            renderMode = RENDERMODE_CONTINUOUSLY
        }
    }

    // ========== Visualizer Data Capture ==========

    private val captureListener = object : Visualizer.OnDataCaptureListener {
        private var captureCount = 0

        override fun onWaveFormDataCapture(
            visualizer: Visualizer,
            waveform: ByteArray,
            samplingRate: Int
        ) {
            // Not used - we use FFT data
        }

        override fun onFftDataCapture(
            visualizer: Visualizer,
            fft: ByteArray,
            samplingRate: Int
        ) {
            captureCount++
            if (captureCount % 30 == 0) { // Log every 30 captures (~every 0.5 seconds)
                Log.d(TAG, "onFftDataCapture called (count=$captureCount), fft.size=${fft.size}, isPaused=$isPaused")
            }
            if (!isPaused) {
                updateBarData(fft)
            }
        }
    }

    /**
     * Process FFT data into frequency bars using logarithmic frequency scaling
     * Similar to standard equalizers (31Hz, 62Hz, 125Hz, 250Hz, 500Hz, 1kHz, 2kHz, 4kHz, 8kHz, 16kHz)
     */
    private fun updateBarData(fft: ByteArray) {
        // FFT data format: [real0, imag0, real1, imag1, ...]
        // Number of frequency bins in FFT (512 for 1024 byte array)
        val numBins = fft.size / 4

        // Assuming 44.1kHz sample rate, each bin represents ~43Hz (22050Hz / 512 bins)
        val sampleRate = 44100
        val maxFreq = sampleRate / 2.0 // Nyquist frequency (22050 Hz)
        val minFreq = 20.0 // Start from 20 Hz (human hearing range)

        // Calculate logarithmic base for frequency distribution
        val logBase = Math.pow(maxFreq / minFreq, 1.0 / NUM_BARS)

        var totalMagnitude = 0f
        for (i in 0 until NUM_BARS) {
            // Calculate frequency range for this bar using logarithmic scale
            val freqStart = minFreq * Math.pow(logBase, i.toDouble())
            val freqEnd = minFreq * Math.pow(logBase, (i + 1).toDouble())

            // Convert frequency to FFT bin index
            val binStart = ((freqStart / maxFreq) * numBins).toInt()
            val binEnd = min(((freqEnd / maxFreq) * numBins).toInt(), numBins - 1)

            // Convert bin index to byte array index (2 bytes per bin: real + imaginary)
            val startIdx = binStart * 2
            val endIdx = binEnd * 2

            // Calculate average magnitude in this frequency band
            var magnitude = 0f
            var count = 0
            var idx = startIdx
            while (idx <= endIdx && idx < fft.size - 1) {
                val real = fft[idx].toFloat()
                val imaginary = fft[idx + 1].toFloat()
                val current = sqrt(real * real + imaginary * imaginary)
                magnitude += current
                count++
                idx += 2
            }

            // Average the magnitude
            if (count > 0) {
                magnitude /= count
            }

            // Normalize to 0.0-1.0 range
            // Lower divisor for better sensitivity, boost low frequencies
            val divisor = if (i < NUM_BARS / 4) 50.0f else 80.0f // Boost bass
            val normalized = min(1.0f, magnitude / divisor)

            // Apply smoothing for fluid animation
            smoothedBarData[i] = smoothedBarData[i] * SMOOTHING_FACTOR +
                    normalized * (1 - SMOOTHING_FACTOR)

            totalMagnitude += smoothedBarData[i]
        }

        if (totalMagnitude > 0.1f && System.currentTimeMillis() % 1000 < 50) {
            Log.d(TAG, "updateBarData: totalMagnitude=$totalMagnitude, bar[0]=${smoothedBarData[0]}, bar[15]=${smoothedBarData[15]}, bar[31]=${smoothedBarData[31]}")
        }
    }

    // ========== OpenGL ES Rendering ==========

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        Log.d(TAG, "onSurfaceCreated called")
        // Set background to white for testing
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        // Load and compile shaders
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, R.raw.visualizer_vertex)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, R.raw.visualizer_fragment)
        Log.d(TAG, "Shaders loaded: vertex=$vertexShader, fragment=$fragmentShader")

        // Create shader program
        shaderProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)

            // Check for linking errors
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(it, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                val error = GLES20.glGetProgramInfoLog(it)
                Log.e(TAG, "Error linking shader program: $error")
                GLES20.glDeleteProgram(it)
            } else {
                Log.d(TAG, "Shader program linked successfully: $it")
            }
        }

        // Delete shaders as they're now linked into program
        GLES20.glDeleteShader(vertexShader)
        GLES20.glDeleteShader(fragmentShader)

        // Initialize buffers
        updateColorBuffer()
        Log.d(TAG, "onSurfaceCreated complete, shaderProgram=$shaderProgram")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    private var frameCount = 0

    override fun onDrawFrame(gl: GL10?) {
        frameCount++

        // Log every 60 frames (~1 second)
        if (frameCount % 60 == 0) {
            Log.e(TAG, "========== onDrawFrame called (frame=$frameCount) ==========")
        }

        // Frame rate limiting
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastFrameTime < FRAME_INTERVAL_MS) {
            return
        }
        lastFrameTime = currentTime

        if (frameCount % 60 == 0) {
            Log.d(TAG, "onDrawFrame: drawing frame, shaderProgram=$shaderProgram")
        }

        // Clear screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        if (shaderProgram == 0) {
            Log.e(TAG, "onDrawFrame: shaderProgram is 0, skipping")
            return
        }

        // Use shader program
        GLES20.glUseProgram(shaderProgram)

        // Enable blending for transparency
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        // Draw each frequency bar
        drawBars()

        if (frameCount % 60 == 0) {
            Log.d(TAG, "onDrawFrame: bars drawn")
        }

        GLES20.glDisable(GLES20.GL_BLEND)
    }

    /**
     * Draw all frequency bars
     */
    private fun drawBars() {
        val barWidth = 2.0f / NUM_BARS // Width in OpenGL coordinates (-1 to 1)
        val spacing = barWidth * 0.2f   // 20% spacing between bars
        val actualBarWidth = barWidth - spacing

        var totalHeight = 0f
        for (i in 0 until NUM_BARS) {
            // TEST: Make bars much taller and add minimum height
            val height = max(0.3f, smoothedBarData[i] * 2.0f) // At least 0.3 height, scaled to 2.0x
            val x = -1.0f + i * barWidth
            totalHeight += height

            // Draw from bottom of screen (-1.0) upward
            drawBar(x, -1.0f, actualBarWidth, height)
        }

        if (frameCount % 60 == 0) {
            Log.d(TAG, "drawBars: totalHeight=$totalHeight, bar[0] height=${smoothedBarData[0] * 2.0f}, min bars with 0.3 height")
        }
    }

    /**
     * Draw a single frequency bar
     */
    private fun drawBar(x: Float, y: Float, width: Float, height: Float) {
        // Create vertices for rectangle (2 triangles)
        val vertices = floatArrayOf(
            x, y,                    // Bottom left
            x + width, y,            // Bottom right
            x, y + height,           // Top left
            x + width, y + height    // Top right
        )

        // Create vertex buffer
        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertices)
                position(0)
            }
        }

        // Get attribute locations
        val positionHandle = GLES20.glGetAttribLocation(shaderProgram, "a_Position")
        val colorHandle = GLES20.glGetAttribLocation(shaderProgram, "a_Color")

        // Enable vertex array
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle, 2,
            GLES20.GL_FLOAT, false,
            0, vertexBuffer
        )

        // Set color
        if (colorBuffer != null) {
            GLES20.glEnableVertexAttribArray(colorHandle)
            GLES20.glVertexAttribPointer(
                colorHandle, 4,
                GLES20.GL_FLOAT, false,
                0, colorBuffer
            )
        }

        // Draw bar (triangle strip: 4 vertices make 2 triangles)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle)
        if (colorBuffer != null) {
            GLES20.glDisableVertexAttribArray(colorHandle)
        }
    }

    /**
     * Load shader from raw resource
     */
    private fun loadShader(type: Int, resourceId: Int): Int {
        val shaderCode = context.resources.openRawResource(resourceId)
            .bufferedReader().use { it.readText() }

        Log.d(TAG, "loadShader type=$type, resourceId=$resourceId, code length=${shaderCode.length}")

        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)

            // Check for compilation errors
            val compiled = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                val error = GLES20.glGetShaderInfoLog(shader)
                Log.e(TAG, "Error compiling shader type=$type: $error")
                GLES20.glDeleteShader(shader)
            } else {
                Log.d(TAG, "Shader type=$type compiled successfully: $shader")
            }
        }
    }

    /**
     * Update color buffer based on current colors
     */
    private fun updateColorBuffer() {
        // TEST: Use bright red color for debugging
        val r = 1.0f  // Red
        val g = 0.0f  // Green
        val b = 0.0f  // Blue
        val a = 1.0f  // Alpha (fully opaque)

        Log.d(TAG, "updateColorBuffer: color=($r, $g, $b, $a)")

        // Create color array (same color for all vertices)
        val colors = floatArrayOf(
            r, g, b, a, // Vertex 0
            r, g, b, a, // Vertex 1
            r, g, b, a, // Vertex 2
            r, g, b, a  // Vertex 3
        )

        colorBuffer = ByteBuffer.allocateDirect(colors.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(colors)
                position(0)
            }
        }
    }

}
