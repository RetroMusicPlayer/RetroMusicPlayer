/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */


package code.name.monkey.retromusic.service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.service.MusicService.*


/**
 * Used to control headset playback.
 * Single press: pause/resume
 * Double press: actionNext track
 * Triple press: previous track
 */
class MediaButtonIntentReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (DEBUG) Log.v(TAG, "Received intent: $intent")
        if (handleIntent(context, intent) && isOrderedBroadcast) {
            abortBroadcast()
        }
    }

    companion object {
        val TAG: String = MediaButtonIntentReceiver::class.java.simpleName
        private val DEBUG = BuildConfig.DEBUG
        private const val MSG_HEADSET_DOUBLE_CLICK_TIMEOUT = 2

        private const val DOUBLE_CLICK = 400

        private var wakeLock: WakeLock? = null
        private var mClickCounter = 0
        private var mLastClickTime: Long = 0

        @SuppressLint("HandlerLeak") // false alarm, handler is already static
        private val mHandler = object : Handler() {

            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MSG_HEADSET_DOUBLE_CLICK_TIMEOUT -> {
                        val clickCount = msg.arg1
                        val command: String?

                        if (DEBUG) Log.v(TAG, "Handling headset click, count = $clickCount")
                        when (clickCount) {
                            1 -> command = ACTION_TOGGLE_PAUSE
                            2 -> command = ACTION_SKIP
                            3 -> command = ACTION_REWIND
                            else -> command = null
                        }

                        if (command != null) {
                            val context = msg.obj as Context
                            startService(context, command)
                        }
                    }
                }
                releaseWakeLockIfHandlerIdle()
            }
        }

        fun handleIntent(context: Context, intent: Intent): Boolean {
            val intentAction = intent.action
            if (Intent.ACTION_MEDIA_BUTTON == intentAction) {
                val event = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
                        ?: return false

                val keycode = event.keyCode
                val action = event.action
                val eventTime = if (event.eventTime != 0L)
                    event.eventTime
                else
                    System.currentTimeMillis()

                var command: String? = null
                when (keycode) {
                    KeyEvent.KEYCODE_MEDIA_STOP -> command = ACTION_STOP
                    KeyEvent.KEYCODE_HEADSETHOOK, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> command = ACTION_TOGGLE_PAUSE
                    KeyEvent.KEYCODE_MEDIA_NEXT -> command = ACTION_SKIP
                    KeyEvent.KEYCODE_MEDIA_PREVIOUS -> command = ACTION_REWIND
                    KeyEvent.KEYCODE_MEDIA_PAUSE -> command = ACTION_PAUSE
                    KeyEvent.KEYCODE_MEDIA_PLAY -> command = ACTION_PLAY
                }
                if (command != null) {
                    if (action == KeyEvent.ACTION_DOWN) {
                        if (event.repeatCount == 0) {
                            // Only consider the first event in a sequence, not the repeat events,
                            // so that we don't trigger in cases where the first event went to
                            // a different app (e.g. when the user ends a phone call by
                            // long pressing the headset button)

                            // The service may or may not be running, but we need to send it
                            // a command.
                            if (keycode == KeyEvent.KEYCODE_HEADSETHOOK || keycode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                                if (eventTime - mLastClickTime >= DOUBLE_CLICK) {
                                    mClickCounter = 0
                                }

                                mClickCounter++
                                if (DEBUG) Log.v(TAG, "Got headset click, count = $mClickCounter")
                                mHandler.removeMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT)

                                val msg = mHandler.obtainMessage(
                                        MSG_HEADSET_DOUBLE_CLICK_TIMEOUT, mClickCounter, 0, context)

                                val delay = (if (mClickCounter < 3) DOUBLE_CLICK else 0).toLong()
                                if (mClickCounter >= 3) {
                                    mClickCounter = 0
                                }
                                mLastClickTime = eventTime
                                acquireWakeLockAndSendMessage(context, msg, delay)
                            } else {
                                startService(context, command)
                            }
                            return true
                        }
                    }
                }
            }
            return false
        }

        private fun startService(context: Context, command: String?) {
            val intent = Intent(context, MusicService::class.java)
            intent.action = command
            try {
                // IMPORTANT NOTE: (kind of a hack)
                // on Android O and above the following crashes when the app is not running
                // there is no good way to check whether the app is running so we catch the exception
                // we do not always want to use startForegroundService() because then one gets an ANR
                // if no notification is displayed via startForeground()
                // according to Play analytics this happens a lot, I suppose for example if command = PAUSE
                context.startService(intent)
            } catch (ignored: IllegalStateException) {
                ContextCompat.startForegroundService(context, intent)
            }
        }

        private fun acquireWakeLockAndSendMessage(context: Context, msg: Message, delay: Long) {
            if (wakeLock == null) {
                val appContext = context.applicationContext
                val pm = appContext.getSystemService(Context.POWER_SERVICE) as PowerManager
                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RetroMusicApp:Wakelock headset button")
                wakeLock!!.setReferenceCounted(false)
            }
            if (DEBUG) Log.v(TAG, "Acquiring wake lock and sending " + msg.what)
            // Make sure we don't indefinitely hold the wake lock under any circumstances
            wakeLock!!.acquire(10000)

            mHandler.sendMessageDelayed(msg, delay)
        }

        private fun releaseWakeLockIfHandlerIdle() {
            if (mHandler.hasMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT)) {
                if (DEBUG) Log.v(TAG, "Handler still has messages pending, not releasing wake lock")
                return
            }

            if (wakeLock != null) {
                if (DEBUG) Log.v(TAG, "Releasing wake lock")
                wakeLock!!.release()
                wakeLock = null
            }
        }
    }
}