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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by hemanths on 2019-11-05.
 */

class ImageSaver(val context: Context) {
    private var external: Boolean = false
    private var directoryName: String = "RetroMusic"
    private var fileName: String = "profile.png"

    fun setFileName(fileName: String): ImageSaver {
        this.fileName = fileName
        return this
    }

    fun setDirectoryName(directoryName: String): ImageSaver {
        this.directoryName = directoryName
        return this
    }

    fun setStoreType(external: Boolean): ImageSaver {
        this.external = external
        return this
    }

    fun save(bitmap: Bitmap) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(createFile())
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (er: Exception) {
            println(er)
        } finally {
            try {
                fileOutputStream?.close()
            } catch (er: IOException) {
                println(er)
            }
        }
    }

    fun getFile(): File {
        return createFile()
    }

    private fun createFile(): File {
        val directory: File = if (external) {
            getFileStorePlace(directoryName)
        } else {
            context.getDir(directoryName, Context.MODE_PRIVATE)
        }
        if (!directory.exists() && !directory.mkdirs()) {
            println("Error in creating folders $directory")
        }
        println("Create file -> $directory/$fileName")
        return File(directory, fileName)
    }

    private fun getFileStorePlace(directoryName: String): File {
        return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), directoryName)
    }

    fun load(): Bitmap? {
        var inputStream: FileInputStream? = null
        return try {
            inputStream = FileInputStream(createFile())
            BitmapFactory.decodeStream(inputStream)
        } catch (er: Exception) {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            null
        }
    }
}