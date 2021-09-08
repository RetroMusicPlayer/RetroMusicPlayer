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
package code.name.monkey.retromusic.activities

import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.activities.tageditor.WriteTagsAsyncTask
import code.name.monkey.retromusic.databinding.ActivityLyricsBinding
import code.name.monkey.retromusic.databinding.FragmentNormalLyricsBinding
import code.name.monkey.retromusic.databinding.FragmentSyncedLyricsBinding
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.surfaceColor
import code.name.monkey.retromusic.extensions.textColorSecondary
import code.name.monkey.retromusic.fragments.base.AbsMusicServiceFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.lyrics.LrcView
import code.name.monkey.retromusic.model.LoadingInfo
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.LyricUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.input.input
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.coroutines.*
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import java.util.*

class LyricsActivity : AbsMusicServiceActivity() {

    private lateinit var binding: ActivityLyricsBinding
    private lateinit var song: Song

    private val lyricsSectionsAdapter = LyricsSectionsAdapter(this)

    private val googleSearchLrcUrl: String
        get() {
            var baseUrl = "http://www.google.com/search?"
            var query = song.title + "+" + song.artistName
            query = "q=" + query.replace(" ", "+") + " lyrics"
            baseUrl += query
            return baseUrl
        }
    private val syairSearchLrcUrl: String
        get() {
            var baseUrl = "https://www.syair.info/search?"
            var query = song.title + "+" + song.artistName
            query = "q=" + query.replace(" ", "+")
            baseUrl += query
            return baseUrl
        }

    private fun buildContainerTransform(): MaterialContainerTransform {
        val transform = MaterialContainerTransform()
        transform.setAllContainerColors(
            MaterialColors.getColor(findViewById(R.id.container), R.attr.colorSurface)
        )
        transform.addTarget(R.id.container)
        transform.duration = 300
        return transform
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLyricsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setTransitionName(binding.container, "lyrics")
        setStatusbarColorAuto()
        setTaskDescriptionColorAuto()
        setNavigationbarColorAuto()

        setupWakelock()

        binding.toolbar.setBackgroundColor(surfaceColor())
        binding.tabLyrics.setBackgroundColor(surfaceColor())
        binding.container.setBackgroundColor(surfaceColor())
        ToolbarContentTintHelper.colorBackButton(binding.toolbar)
        setSupportActionBar(binding.toolbar)
        setupViews()

    }


    private fun setupViews() {
        binding.lyricsPager.adapter = lyricsSectionsAdapter
        TabLayoutMediator(binding.tabLyrics, binding.lyricsPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Synced Lyrics"
                1 -> "Normal Lyrics"
                else -> ""
            }
        }.attach()
//        lyricsPager.isUserInputEnabled = false

        binding.tabLyrics.setSelectedTabIndicatorColor(ThemeStore.accentColor(this))
        binding.tabLyrics.setTabTextColors(textColorSecondary(), accentColor())
    }


    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateTitleSong()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateTitleSong()
    }

    private fun updateTitleSong() {
        song = MusicPlayerRemote.currentSong
        binding.toolbar.title = song.title
        binding.toolbar.subtitle = song.artistName
    }

    private fun setupWakelock() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.action_search) {
            RetroUtil.openUrl(
                this, when (binding.lyricsPager.currentItem) {
                    0 -> syairSearchLrcUrl
                    1 -> googleSearchLrcUrl
                    else -> googleSearchLrcUrl
                }
            )
        } else if (item.itemId == R.id.action_edit) {
            when (binding.lyricsPager.currentItem) {
                0 -> {
                    editSyncedLyrics()
                }
                1 -> {
                    editNormalLyrics()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun editNormalLyrics() {
        var content = ""
        val file = File(MusicPlayerRemote.currentSong.data)
        try {
            content = AudioFileIO.read(file).tagOrCreateDefault.getFirst(FieldKey.LYRICS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(res = R.string.edit_normal_lyrics)
            input(
                hintRes = R.string.paste_lyrics_here,
                prefill = content,
                inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
            ) { _, input ->
                val fieldKeyValueMap = EnumMap<FieldKey, String>(FieldKey::class.java)
                fieldKeyValueMap[FieldKey.LYRICS] = input.toString()
                WriteTagsAsyncTask(this@LyricsActivity).execute(
                    LoadingInfo(
                        listOf(song.data), fieldKeyValueMap, null
                    )
                )
            }
            positiveButton(res = R.string.save) {
                (lyricsSectionsAdapter.fragments[1].first as NormalLyrics).loadNormalLyrics()
            }
            negativeButton(res = android.R.string.cancel)
        }
    }


    private fun editSyncedLyrics() {
        var lrcFile: File? = null
        if (LyricUtil.isLrcOriginalFileExist(song.data)) {
            lrcFile = LyricUtil.getLocalLyricOriginalFile(song.data)
        } else if (LyricUtil.isLrcFileExist(song.title, song.artistName)) {
            lrcFile = LyricUtil.getLocalLyricFile(song.title, song.artistName)
        }
        val content: String = LyricUtil.getStringFromLrc(lrcFile)

        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(res = R.string.edit_synced_lyrics)
            input(
                hintRes = R.string.paste_timeframe_lyrics_here,
                prefill = content,
                inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
            ) { _, input ->
                LyricUtil.writeLrc(song, input.toString())
            }
            positiveButton(res = R.string.save) {
                (lyricsSectionsAdapter.fragments[0].first as SyncedLyrics).loadLRCLyrics()
            }
            negativeButton(res = android.R.string.cancel)
        }
    }

    class LyricsSectionsAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        val fragments = listOf(
            Pair(SyncedLyrics(), R.string.synced_lyrics),
            Pair(NormalLyrics(), R.string.normal_lyrics)
        )


        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position].first
        }
    }

    class NormalLyrics : AbsMusicServiceFragment(R.layout.fragment_normal_lyrics) {

        private var _binding: FragmentNormalLyricsBinding? = null
        private val binding get() = _binding!!

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            _binding = FragmentNormalLyricsBinding.bind(view)
            loadNormalLyrics()
            super.onViewCreated(view, savedInstanceState)
        }

        fun loadNormalLyrics() {
            var lyrics: String? = null
            val file = File(MusicPlayerRemote.currentSong.data)
            try {
                lyrics = AudioFileIO.read(file).tagOrCreateDefault.getFirst(FieldKey.LYRICS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (lyrics.isNullOrEmpty()) {
                binding.noLyricsFound.visibility = View.VISIBLE
            } else {
                binding.noLyricsFound.visibility = View.GONE
            }
            binding.normalLyrics.text = lyrics
        }

        override fun onPlayingMetaChanged() {
            super.onPlayingMetaChanged()
            loadNormalLyrics()
        }

        override fun onServiceConnected() {
            super.onServiceConnected()
            loadNormalLyrics()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

    class SyncedLyrics : AbsMusicServiceFragment(R.layout.fragment_synced_lyrics),
        MusicProgressViewUpdateHelper.Callback {

        private var _binding: FragmentSyncedLyricsBinding? = null
        private val binding get() = _binding!!
        private lateinit var updateHelper: MusicProgressViewUpdateHelper

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            updateHelper = MusicProgressViewUpdateHelper(this, 500, 1000)
            _binding = FragmentSyncedLyricsBinding.bind(view)
            setupLyricsView()
            loadLRCLyrics()
            super.onViewCreated(view, savedInstanceState)
        }

        fun loadLRCLyrics() {
            binding.lyricsView.setLabel("Empty")
            val song = MusicPlayerRemote.currentSong
            if (LyricUtil.isLrcOriginalFileExist(song.data)) {
                binding.lyricsView.loadLrc(LyricUtil.getLocalLyricOriginalFile(song.data))
            } else if (LyricUtil.isLrcFileExist(song.title, song.artistName)) {
                binding.lyricsView.loadLrc(LyricUtil.getLocalLyricFile(song.title, song.artistName))
            }
        }

        private fun setupLyricsView() {
            binding.lyricsView.apply {
                setCurrentColor(ThemeStore.accentColor(context))
                setTimeTextColor(ThemeStore.accentColor(context))
                setTimelineColor(ThemeStore.accentColor(context))
                setTimelineTextColor(ThemeStore.accentColor(context))
                setDraggable(true, LrcView.OnPlayClickListener {
                    MusicPlayerRemote.seekTo(it.toInt())
                    return@OnPlayClickListener true
                })
            }
        }

        override fun onUpdateProgressViews(progress: Int, total: Int) {
            binding.lyricsView.updateTime(progress.toLong())
        }

        override fun onPlayingMetaChanged() {
            super.onPlayingMetaChanged()
            loadLRCLyrics()
        }

        override fun onServiceConnected() {
            super.onServiceConnected()
            loadLRCLyrics()
        }

        override fun onResume() {
            super.onResume()
            updateHelper.start()
        }

        override fun onPause() {
            super.onPause()
            updateHelper.stop()
        }
    }


}
