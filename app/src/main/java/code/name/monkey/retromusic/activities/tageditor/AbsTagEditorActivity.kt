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
package code.name.monkey.retromusic.activities.tageditor

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.hideSoftKeyboard
import code.name.monkey.retromusic.extensions.setTaskDescriptionColorAuto
import code.name.monkey.retromusic.model.ArtworkInfo
import code.name.monkey.retromusic.model.AudioTagInfo
import code.name.monkey.retromusic.repository.Repository
import code.name.monkey.retromusic.util.logD
import code.name.monkey.retromusic.util.logE
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.koin.android.ext.android.inject
import java.io.File

abstract class AbsTagEditorActivity<VB : ViewBinding> : AbsBaseActivity() {
    abstract val editorImage: ImageView
    val repository by inject<Repository>()

    lateinit var saveFab: MaterialButton
    protected var id: Long = 0
        private set
    private var paletteColorPrimary: Int = 0
    private var songPaths: List<String>? = null
    private var savedSongPaths: List<String>? = null
    private val currentSongPath: String? = null
    private var savedTags: Map<FieldKey, String>? = null
    private var savedArtworkInfo: ArtworkInfo? = null
    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!
    private var cacheFiles = listOf<File>()

    abstract val bindingInflater: (LayoutInflater) -> VB

    private lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>

    protected abstract fun loadImageFromFile(selectedFile: Uri?)

    protected val show: AlertDialog
        get() =
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.update_image)
                .setItems(items.toTypedArray()) { _, position ->
                    when (position) {
                        0 -> startImagePicker()
                        1 -> searchImageOnWeb()
                        2 -> deleteImage()
                    }
                }
                .setNegativeButton(R.string.action_cancel, null)
                .show()
                .colorButtons()

    internal val albumArtist: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.ALBUM_ARTIST)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val songTitle: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.TITLE)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }
    protected val composer: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.COMPOSER)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val albumTitle: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.ALBUM)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val artistName: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.ARTIST)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val albumArtistName: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.ALBUM_ARTIST)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val genreName: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.GENRE)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val songYear: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.YEAR)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val trackNumber: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.TRACK)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val discNumber: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.DISC_NO)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val lyrics: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.LYRICS)
            } catch (e: Exception) {
                logE(e)
                null
            }
        }

    protected val albumArt: Bitmap?
        get() {
            try {
                val artworkTag = getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.firstArtwork
                if (artworkTag != null) {
                    val artworkBinaryData = artworkTag.binaryData
                    return BitmapFactory.decodeByteArray(
                        artworkBinaryData,
                        0,
                        artworkBinaryData.size
                    )
                }
                return null
            } catch (e: Exception) {
                logE(e)
                return null
            }
        }

    private val pickArtworkImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            loadImageFromFile(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        setTaskDescriptionColorAuto()

        saveFab = findViewById(R.id.saveTags)
        getIntentExtras()

        songPaths = getSongPaths()
        logD(songPaths?.size)
        if (songPaths!!.isEmpty()) {
            finish()
        }
        setUpViews()
        launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                writeToFiles(getSongUris(), cacheFiles)
            }
        }
    }

    private fun setUpViews() {
        setUpFab()
        setUpImageView()
    }

    private lateinit var items: List<String>

    private fun setUpImageView() {
        loadCurrentImage()
        items = listOf(
            getString(R.string.pick_from_local_storage),
            getString(R.string.web_search),
            getString(R.string.remove_cover)
        )
        editorImage.setOnClickListener { show }
    }

    private fun startImagePicker() {
        pickArtworkImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    protected abstract fun loadCurrentImage()

    protected abstract fun searchImageOnWeb()

    protected abstract fun deleteImage()

    private fun setUpFab() {
        saveFab.accentColor()
        saveFab.apply {
            scaleX = 0f
            scaleY = 0f
            isEnabled = false
            setOnClickListener { save() }
        }
    }

    protected abstract fun save()

    private fun getIntentExtras() {
        val intentExtras = intent.extras
        if (intentExtras != null) {
            id = intentExtras.getLong(EXTRA_ID)
        }
    }

    protected abstract fun getSongPaths(): List<String>

    protected abstract fun getSongUris(): List<Uri>

    protected fun searchWebFor(vararg keys: String) {
        val stringBuilder = StringBuilder()
        for (key in keys) {
            stringBuilder.append(key)
            stringBuilder.append(" ")
        }
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra(SearchManager.QUERY, stringBuilder.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun dataChanged() {
        showFab()
    }

    private fun showFab() {
        saveFab.animate().setDuration(500).setInterpolator(OvershootInterpolator()).scaleX(1f)
            .scaleY(1f).start()
        saveFab.isEnabled = true
    }

    private fun hideFab() {
        saveFab.animate().setDuration(500).setInterpolator(OvershootInterpolator()).scaleX(0.0f)
            .scaleY(0.0f).start()
        saveFab.isEnabled = false
    }

    protected fun setImageBitmap(bitmap: Bitmap?, bgColor: Int) {
        if (bitmap == null) {
            editorImage.setImageResource(R.drawable.default_audio_art)
        } else {
            editorImage.setImageBitmap(bitmap)
        }
        setColors(bgColor)
    }

    protected open fun setColors(color: Int) {
        paletteColorPrimary = color
    }

    protected fun writeValuesToFiles(
        fieldKeyValueMap: Map<FieldKey, String>,
        artworkInfo: ArtworkInfo?
    ) {
        hideSoftKeyboard()

        hideFab()
        logD(fieldKeyValueMap)
        GlobalScope.launch {
            if (VersionUtils.hasR()) {
                cacheFiles = TagWriter.writeTagsToFilesR(
                    this@AbsTagEditorActivity, AudioTagInfo(
                        songPaths,
                        fieldKeyValueMap,
                        artworkInfo
                    )
                )

                if (cacheFiles.isNotEmpty()) {
                    val pendingIntent =
                        MediaStore.createWriteRequest(contentResolver, getSongUris())
                    launcher.launch(IntentSenderRequest.Builder(pendingIntent).build())
                }
            } else {
                TagWriter.writeTagsToFiles(
                    this@AbsTagEditorActivity, AudioTagInfo(
                        songPaths,
                        fieldKeyValueMap,
                        artworkInfo
                    )
                )
            }
        }
    }

    private fun writeTags(paths: List<String>?) {
        GlobalScope.launch {
            if (VersionUtils.hasR()) {
                cacheFiles = TagWriter.writeTagsToFilesR(
                    this@AbsTagEditorActivity, AudioTagInfo(
                        paths,
                        savedTags,
                        savedArtworkInfo
                    )
                )
                val pendingIntent = MediaStore.createWriteRequest(contentResolver, getSongUris())

                launcher.launch(IntentSenderRequest.Builder(pendingIntent).build())
            } else {
                TagWriter.writeTagsToFiles(
                    this@AbsTagEditorActivity, AudioTagInfo(
                        paths,
                        savedTags,
                        savedArtworkInfo
                    )
                )
            }
        }
    }

    private lateinit var audioFile: AudioFile

    private fun getAudioFile(path: String): AudioFile {
        return try {
            if (!this::audioFile.isInitialized) {
                audioFile = AudioFileIO.read(File(path))
            }
            audioFile
        } catch (e: Exception) {
            Log.e(TAG, "Could not read audio file $path", e)
            AudioFile()
        }
    }

    private fun writeToFiles(songUris: List<Uri>, cacheFiles: List<File>) {
        if (cacheFiles.size == songUris.size) {
            for (i in cacheFiles.indices) {
                contentResolver.openOutputStream(songUris[i])?.use { output ->
                    cacheFiles[i].inputStream().use { input ->
                        input.copyTo(output)
                    }
                }
            }
        }
        lifecycleScope.launch {
            TagWriter.scan(this@AbsTagEditorActivity, getSongPaths())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Delete Cache Files
        cacheFiles.forEach { file ->
            file.delete()
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_PALETTE = "extra_palette"
        private val TAG = AbsTagEditorActivity::class.java.simpleName
        private const val REQUEST_CODE_SELECT_IMAGE = 1000
    }
}
