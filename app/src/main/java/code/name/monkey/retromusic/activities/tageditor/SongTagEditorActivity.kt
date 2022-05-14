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

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.appHandleColor
import code.name.monkey.retromusic.repository.SongRepository
import kotlinx.android.synthetic.main.activity_song_tag_editor.*
import org.jaudiotagger.tag.FieldKey
import org.koin.android.ext.android.inject
import java.util.*

class SongTagEditorActivity : AbsTagEditorActivity(), TextWatcher {

    override val contentViewLayout: Int
        get() = R.layout.activity_song_tag_editor

    private val songRepository by inject<SongRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setNoImageMode()
        setUpViews()
        toolbar.setBackgroundColor(ATHUtil.resolveColor(this, R.attr.colorSurface))
        setSupportActionBar(toolbar)
    }

    private fun setUpViews() {
        fillViewsWithFileTags()
        MaterialUtil.setTint(songTextContainer, false)
        MaterialUtil.setTint(composerContainer, false)
        MaterialUtil.setTint(albumTextContainer, false)
        MaterialUtil.setTint(artistContainer, false)
        MaterialUtil.setTint(albumArtistContainer, false)
        MaterialUtil.setTint(yearContainer, false)
        MaterialUtil.setTint(genreContainer, false)
        MaterialUtil.setTint(trackNumberContainer, false)
        MaterialUtil.setTint(lyricsContainer, false)

        songText.appHandleColor().addTextChangedListener(this)
        albumText.appHandleColor().addTextChangedListener(this)
        albumArtistText.appHandleColor().addTextChangedListener(this)
        artistText.appHandleColor().addTextChangedListener(this)
        genreText.appHandleColor().addTextChangedListener(this)
        yearText.appHandleColor().addTextChangedListener(this)
        trackNumberText.appHandleColor().addTextChangedListener(this)
        lyricsText.appHandleColor().addTextChangedListener(this)
        songComposerText.appHandleColor().addTextChangedListener(this)
    }

    private fun fillViewsWithFileTags() {
        songText.setText(songTitle)
        albumArtistText.setText(albumArtist)
        albumText.setText(albumTitle)
        artistText.setText(artistName)
        genreText.setText(genreName)
        yearText.setText(songYear)
        trackNumberText.setText(trackNumber)
        lyricsText.setText(lyrics)
        songComposerText.setText(composer)
    }

    override fun loadCurrentImage() {
    }

    override fun searchImageOnWeb() {
    }

    override fun deleteImage() {
    }

    override fun save() {
        val fieldKeyValueMap = EnumMap<FieldKey, String>(FieldKey::class.java)
        fieldKeyValueMap[FieldKey.TITLE] = songText.text.toString()
        fieldKeyValueMap[FieldKey.ALBUM] = albumText.text.toString()
        fieldKeyValueMap[FieldKey.ARTIST] = artistText.text.toString()
        fieldKeyValueMap[FieldKey.GENRE] = genreText.text.toString()
        fieldKeyValueMap[FieldKey.YEAR] = yearText.text.toString()
        fieldKeyValueMap[FieldKey.TRACK] = trackNumberText.text.toString()
        fieldKeyValueMap[FieldKey.LYRICS] = lyricsText.text.toString()
        fieldKeyValueMap[FieldKey.ALBUM_ARTIST] = albumArtistText.text.toString()
        fieldKeyValueMap[FieldKey.COMPOSER] = songComposerText.text.toString()
        writeValuesToFiles(fieldKeyValueMap, null)
    }

    override fun getSongPaths(): List<String> = listOf(songRepository.song(id).data)

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
}
