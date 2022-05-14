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

import android.os.Handler
import android.os.Looper
import android.os.Message
import code.name.monkey.retromusic.service.MusicService.SAVE_QUEUES
import java.lang.ref.WeakReference

internal class QueueSaveHandler(
    musicService: MusicService,
    looper: Looper
) : Handler(looper) {
    private val service: WeakReference<MusicService> = WeakReference(musicService)

    override fun handleMessage(msg: Message) {
        val service: MusicService? = service.get()
        if (msg.what == SAVE_QUEUES) {
            service?.saveQueuesImpl()
        }
    }
}
