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
package code.name.monkey.retromusic.util

import android.util.Log
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.model.lyrics.AbsSynchronizedLyrics
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.*

/**
 * Created by hefuyi on 2016/11/8.
 */
object LyricUtil {
    private val lrcRootPath =
        getExternalStorageDirectory().toString() + "/RetroMusic/lyrics/"
    private const val TAG = "LyricUtil"
    fun writeLrcToLoc(
        title: String, artist: String, lrcContext: String
    ): File? {
        var writer: FileWriter? = null
        return try {
            val file = File(getLrcPath(title, artist))
            if (file.parentFile?.exists() != true) {
                file.parentFile?.mkdirs()
            }
            writer = FileWriter(getLrcPath(title, artist))
            writer.write(lrcContext)
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    //So in Retro, Lrc file can be same folder as Music File or in RetroMusic Folder
    // In this case we pass location of the file and Contents to write to file
    fun writeLrc(song: Song, lrcContext: String) {
        var writer: FileWriter? = null
        val location: File?
        try {
            if (isLrcOriginalFileExist(song.data)) {
                location = getLocalLyricOriginalFile(song.data)
            } else if (isLrcFileExist(song.title, song.artistName)) {
                location = getLocalLyricFile(song.title, song.artistName)
            } else {
                location = File(getLrcPath(song.title, song.artistName))
                if (location.parentFile?.exists() != true) {
                    location.parentFile?.mkdirs()
                }
            }
            writer = FileWriter(location)
            writer.write(lrcContext)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun deleteLrcFile(title: String, artist: String): Boolean {
        val file = File(getLrcPath(title, artist))
        return file.delete()
    }

    private fun isLrcFileExist(title: String, artist: String): Boolean {
        val file = File(getLrcPath(title, artist))
        return file.exists()
    }

    private fun isLrcOriginalFileExist(path: String): Boolean {
        val file = File(getLrcOriginalPath(path))
        return file.exists()
    }

    private fun getLocalLyricFile(title: String, artist: String): File? {
        val file = File(getLrcPath(title, artist))
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

    private fun getLocalLyricOriginalFile(path: String): File? {
        val file = File(getLrcOriginalPath(path))
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

    private fun getLrcPath(title: String, artist: String): String {
        return "$lrcRootPath$title - $artist.lrc"
    }

    private fun getLrcOriginalPath(filePath: String): String {
        return filePath.replace(filePath.substring(filePath.lastIndexOf(".") + 1), "lrc")
    }

    @Throws(Exception::class)
    fun getStringFromFile(title: String, artist: String): String {
        val file = File(getLrcPath(title, artist))
        val fin = FileInputStream(file)
        val ret = convertStreamToString(fin)
        fin.close()
        return ret
    }

    @Throws(Exception::class)
    private fun convertStreamToString(inputStream: InputStream): String {
        return inputStream.bufferedReader().readLines().joinToString(separator = "\n")
    }

    fun getStringFromLrc(file: File?): String {
        try {
            val reader = BufferedReader(FileReader(file))
            return reader.readLines().joinToString(separator = "\n")
        } catch (e: Exception) {
            Log.i("Error", "Error Occurred")
        }
        return ""
    }

    fun getSyncedLyricsFile(song: Song): File? {
        return when {
            isLrcOriginalFileExist(song.data) -> {
                getLocalLyricOriginalFile(song.data)
            }
            isLrcFileExist(song.title, song.artistName) -> {
                getLocalLyricFile(song.title, song.artistName)
            }
            else -> {
                null
            }
        }
    }

    fun getEmbeddedSyncedLyrics(data: String): String? {
        val embeddedLyrics = try {
            AudioFileIO.read(File(data)).tagOrCreateDefault.getFirst(FieldKey.LYRICS)
        } catch (e: Exception) {
            return null
        }
        return if (AbsSynchronizedLyrics.isSynchronized(embeddedLyrics)) {
            embeddedLyrics
        } else {
            null
        }
    }
}