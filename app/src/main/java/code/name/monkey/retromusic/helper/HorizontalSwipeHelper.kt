/*
 * Copyright (c) 2020 Hemanth Savarla.
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
package code.name.monkey.retromusic.helper

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

/**
 * Simplified horizontal swipe detector that only detects swipe direction
 * without handling navigation logic directly
 */
class HorizontalSwipeHelper(
    context: Context,
    private val onSwipeDetected: (SwipeDirection) -> Unit
) : View.OnTouchListener {

    private val gestureDetector = GestureDetector(context, SwipeGestureListener())

    enum class SwipeDirection {
        LEFT, RIGHT
    }

    companion object {
        private const val SWIPE_THRESHOLD = 80 // Optimal balance
        private const val SWIPE_VELOCITY_THRESHOLD = 100 // Smooth but responsive
        private const val MAX_VERTICAL_DEVIATION = 150 // Reasonable vertical tolerance
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return event?.let { 
            gestureDetector.onTouchEvent(it)
            false // Don't consume the event
        } ?: false
    }

    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 == null) return false
            
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y
            
            // Only trigger on predominantly horizontal swipes
            if (abs(diffX) > abs(diffY) && 
                abs(diffX) > SWIPE_THRESHOLD && 
                abs(velocityX) > SWIPE_VELOCITY_THRESHOLD &&
                abs(diffY) < MAX_VERTICAL_DEVIATION) {
                
                val direction = if (diffX > 0) SwipeDirection.RIGHT else SwipeDirection.LEFT
                onSwipeDetected(direction)
                return true
            }
            return false
        }
    }
}