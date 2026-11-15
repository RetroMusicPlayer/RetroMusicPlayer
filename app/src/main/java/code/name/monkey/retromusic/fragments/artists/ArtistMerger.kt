/*
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package code.name.monkey.retromusic.fragments.artists

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.tageditor.TagWriter
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.model.AudioTagInfo
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jaudiotagger.tag.FieldKey
import java.io.File

/**
 * Writes a new artist name into the tags of a list of songs, so that two artists that are really
 * the same one (different spelling, "feat." variants) end up as a single artist in the library.
 * Create it as a property of the fragment: it registers an activity result launcher.
 */
class ArtistMerger(private val fragment: Fragment) {

    private var cacheFiles: List<File> = emptyList()
    private var uris: List<Uri> = emptyList()
    private var paths: List<String> = emptyList()
    private var onDone: (() -> Unit)? = null

    private val launcher = fragment.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            copyBack()
        } else {
            cleanUp()
        }
    }

    fun merge(songs: List<Song>, newName: String, done: () -> Unit) {
        val context = fragment.context?.applicationContext ?: return
        val name = newName.trim()
        if (name.isEmpty() || songs.isEmpty()) return
        onDone = done
        paths = songs.map { it.data }
        uris = songs.map { MusicUtil.getSongFileUri(it.id) }
        val tags = mapOf(FieldKey.ARTIST to name, FieldKey.ALBUM_ARTIST to name)
        fragment.lifecycleScope.launch {
            if (VersionUtils.hasR()) {
                cacheFiles = TagWriter.writeTagsToFilesR(context, AudioTagInfo(paths, tags, null))
                if (cacheFiles.isEmpty()) {
                    return@launch
                }
                val request = MediaStore.createWriteRequest(context.contentResolver, uris)
                launcher.launch(IntentSenderRequest.Builder(request).build())
            } else {
                TagWriter.writeTagsToFiles(context, AudioTagInfo(paths, tags, null))
                finish()
            }
        }
    }

    private fun copyBack() {
        val context = fragment.context?.applicationContext ?: return
        fragment.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (cacheFiles.size == uris.size) {
                    for (i in cacheFiles.indices) {
                        context.contentResolver.openOutputStream(uris[i])?.use { output ->
                            cacheFiles[i].inputStream().use { it.copyTo(output) }
                        }
                    }
                }
            }
            TagWriter.scan(context, paths)
            cleanUp()
            finish()
        }
    }

    private fun cleanUp() {
        cacheFiles.forEach { it.delete() }
        cacheFiles = emptyList()
    }

    private fun finish() {
        fragment.context?.showToast(R.string.merge_artist_done)
        onDone?.invoke()
        onDone = null
    }
}
