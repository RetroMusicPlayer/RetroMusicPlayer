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

package code.name.monkey.retromusic.interfaces

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer

/**
 * @author Hemanth S (h4h13).
 */

interface EqualizerInterface {
    val bandLevelLow: Int

    val bandLevelHigh: Int

    val numberOfBands: Int

    var isBassBoostEnabled: Boolean

    var bassBoostStrength: Int

    var isVirtualizerEnabled: Boolean

    var virtualizerStrength: Int

    val isRunning: Boolean

    val equalizer: Equalizer

    val bassBoost: BassBoost

    val virtualizer: Virtualizer

    fun getCenterFreq(band: Int): Int

    fun getBandLevel(band: Int): Int

    fun setBandLevel(band: Int, level: Int)


}
