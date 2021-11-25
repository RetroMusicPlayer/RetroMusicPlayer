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

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.ImageView
import code.name.monkey.retromusic.databinding.ActivitySongTagEditorBinding
import code.name.monkey.retromusic.extensions.appHandleColor
import code.name.monkey.retromusic.extensions.setTint
import code.name.monkey.retromusic.repository.SongRepository
import code.name.monkey.retromusic.util.MusicUtil
import com.google.android.material.shape.MaterialShapeDrawable
import org.jaudiotagger.tag.FieldKey
import org.koin.android.ext.android.inject
import java.util.*

class SongTagEditorActivity : AbsTagEditorActivity<ActivitySongTagEditorBinding>(), TextWatcher {

    override val bindingInflater: (LayoutInflater) -> ActivitySongTagEditorBinding =
        ActivitySongTagEditorBinding::inflate


    private val songRepository by inject<SongRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViews()
        setNoImageMode()
        setSupportActionBar(binding.toolbar)
        binding.appBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViews() {
        fillViewsWithFileTags()
        binding.songTextContainer.setTint(false)
        binding.composerContainer.setTint(false)
        binding.albumTextContainer.setTint(false)
        binding.artistContainer.setTint(false)
        binding.albumArtistContainer.setTint(false)
        binding.yearContainer.setTint(false)
        binding.genreContainer.setTint(false)
        binding.trackNumberContainer.setTint(false)
        binding.lyricsContainer.setTint(false)

        binding.songText.appHandleColor().addTextChangedListener(this)
        binding.albumText.appHandleColor().addTextChangedListener(this)
        binding.albumArtistText.appHandleColor().addTextChangedListener(this)
        binding.artistText.appHandleColor().addTextChangedListener(this)
        binding.genreText.appHandleColor().addTextChangedListener(this)
        binding.yearText.appHandleColor().addTextChangedListener(this)
        binding.trackNumberText.appHandleColor().addTextChangedListener(this)
        binding.lyricsText.appHandleColor().addTextChangedListener(this)
        binding.songComposerText.appHandleColor().addTextChangedListener(this)

        binding.lyricsText.setOnTouchListener { view, _ ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            return@setOnTouchListener false
        }
    }

    private fun fillViewsWithFileTags() {
        binding.songText.setText(songTitle)
        binding.albumArtistText.setText(albumArtist)
        binding.albumText.setText(albumTitle)
        binding.artistText.setText(artistName)
        binding.genreText.setText(genreName)
        binding.yearText.setText(songYear)
        binding.trackNumberText.setText(trackNumber)
        binding.lyricsText.setText(lyrics)
        binding.songComposerText.setText(composer)
        println(songTitle + songYear)
    }

    override fun loadCurrentImage() {}

    override fun searchImageOnWeb() {}

    override fun deleteImage() {}

    override fun save() {
        val fieldKeyValueMap = EnumMap<FieldKey, String>(FieldKey::class.java)
        fieldKeyValueMap[FieldKey.TITLE] = binding.songText.text.toString()
        fieldKeyValueMap[FieldKey.ALBUM] = binding.albumText.text.toString()
        fieldKeyValueMap[FieldKey.ARTIST] = binding.artistText.text.toString()
        fieldKeyValueMap[FieldKey.GENRE] = binding.genreText.text.toString()
        fieldKeyValueMap[FieldKey.YEAR] = binding.yearText.text.toString()
        fieldKeyValueMap[FieldKey.TRACK] = binding.trackNumberText.text.toString()
        fieldKeyValueMap[FieldKey.LYRICS] = binding.lyricsText.text.toString()
        fieldKeyValueMap[FieldKey.ALBUM_ARTIST] = binding.albumArtistText.text.toString()
        fieldKeyValueMap[FieldKey.COMPOSER] = binding.songComposerText.text.toString()
        writeValuesToFiles(fieldKeyValueMap, null)
    }

    override fun getSongPaths(): List<String> = listOf(songRepository.song(id).data)

    override fun getSongUris(): List<Uri> = listOf(MusicUtil.getSongFileUri(id))

    override fun loadImageFromFile(selectedFile: Uri?) {
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable) {
        dataChanged()
    }

    companion object {
        val TAG: String = SongTagEditorActivity::class.java.simpleName
    }

    override val editorImage: ImageView?
        get() = null
}
