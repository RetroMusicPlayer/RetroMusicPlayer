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

import android.content.Context
import code.name.monkey.retromusic.model.Playlist
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

object M3UWriter : M3UConstants {
    @JvmStatic
    @Throws(IOException::class)
    fun write(
        context: Context,
        dir: File,
        playlist: Playlist
    ): File? {
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, playlist.name + "." + M3UConstants.EXTENSION)
        val songs = playlist.getSongs(context)
        if (songs.size > 0) {
            val bw = BufferedWriter(FileWriter(file))
            bw.write(M3UConstants.HEADER)
            for (song in songs) {
                bw.newLine()
                bw.write(M3UConstants.ENTRY + song.duration + M3UConstants.DURATION_SEPARATOR + song.artistName + " - " + song.title)
                bw.newLine()
                bw.write(song.data)
            }
            bw.close()
        }
        return file
    }
}