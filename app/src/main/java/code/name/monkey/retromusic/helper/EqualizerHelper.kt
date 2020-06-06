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

package code.name.monkey.retromusic.helper

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import android.util.Log

import code.name.monkey.retromusic.interfaces.EqualizerInterface

/**
 * @author Hemanth S (h4h13).
 */

class EqualizerHelper private constructor() : EqualizerInterface {
    override val equalizer: Equalizer
    override val bassBoost: BassBoost
    override val virtualizer: Virtualizer

    override val bandLevelHigh: Int
    override val bandLevelLow: Int
    override var isRunning = false

    override val numberOfBands: Int
        get() = equalizer.numberOfBands.toInt()

    override var isBassBoostEnabled: Boolean
        get() = bassBoost.enabled
        set(isEnabled) {
            bassBoost.enabled = isEnabled
        }

    override var bassBoostStrength: Int
        get() = bassBoost.roundedStrength.toInt()
        set(strength) = bassBoost.setStrength(strength.toShort())

    override var isVirtualizerEnabled: Boolean
        get() = virtualizer.enabled
        set(isEnabled) {
            virtualizer.enabled = isEnabled
        }

    override var virtualizerStrength: Int
        get() = virtualizer.roundedStrength.toInt()
        set(strength) = virtualizer.setStrength(strength.toShort())

    init {

        //Prevent form the reflection api.
        if (ourInstance != null) {
            throw RuntimeException("Use getInstance() method to get the single instance of this class.")
        }

        val i = MusicPlayerRemote.audioSessionId

        equalizer = Equalizer(100, i)

        equalizer.enabled = true
        bassBoost = BassBoost(100, i)
        virtualizer = Virtualizer(100, i)

        bandLevelHigh = equalizer.bandLevelRange[1].toInt()
        bandLevelLow = equalizer.bandLevelRange[0].toInt()

        Log.i(TAG, "onCreate: $bandLevelHigh $bandLevelLow")
        isRunning = true
    }

    //Make singleton from serialize and deserialize operation.
    protected fun readResolve(): EqualizerHelper? {
        return instance
    }

    override fun getCenterFreq(band: Int): Int {
        return equalizer.getCenterFreq(band.toShort())
    }


    override fun getBandLevel(band: Int): Int {
        return equalizer.getBandLevel(band.toShort()).toInt()
    }

    override fun setBandLevel(band: Int, level: Int) {
        equalizer.setBandLevel(band.toShort(), level.toShort())
    }

    companion object {
        private const val TAG = "EqualizerHelper"

        @Volatile
        private var ourInstance: EqualizerHelper? = null

        //Double check locking pattern
        //Check for the first time
        //Check for the second time.
        //if there is no instance available... create new one
        val instance: EqualizerHelper?
            get() {
                if (ourInstance == null) {

                    synchronized(EqualizerHelper::class.java) {
                        if (ourInstance == null) {
                            ourInstance = EqualizerHelper()
                        }
                    }
                }
                return ourInstance
            }
    }

}
