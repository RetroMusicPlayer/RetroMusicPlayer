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

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.view.MenuItem
import androidx.core.view.drawToBitmap
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.databinding.ActivityShareInstagramBinding
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.Share
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor

/**
 * Created by hemanths on 2020-02-02.
 */

class ShareInstagramStory : AbsBaseActivity() {

    private lateinit var binding: ActivityShareInstagramBinding

    companion object {
        const val EXTRA_SONG = "extra_song"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivityShareInstagramBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusbarColor(Color.TRANSPARENT)
        setNavigationbarColor(Color.BLACK)

        binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
        setSupportActionBar(binding.toolbar)

        val song = intent.extras?.getParcelable<Song>(EXTRA_SONG)
        song?.let { songFinal ->
            GlideApp.with(this)
                .asBitmapPalette()
                .songCoverOptions(songFinal)
                .load(RetroGlideExtension.getSongModel(songFinal))
                .into(object : RetroMusicColoredTarget(binding.image) {
                    override fun onColorReady(colors: MediaNotificationProcessor) {
                        val isColorLight = ColorUtil.isColorLight(colors.backgroundColor)
                        setColors(isColorLight, colors.backgroundColor)
                    }
                })

            binding.shareTitle.text = songFinal.title
            binding.shareText.text = songFinal.artistName
            binding.shareButton.setOnClickListener {
                val path: String = Media.insertImage(
                    contentResolver,
                    binding.mainContent.drawToBitmap(Bitmap.Config.ARGB_8888),
                    "Design", null
                )
                val uri = Uri.parse(path)
                Share.shareStoryToSocial(
                    this@ShareInstagramStory,
                    uri
                )
            }
        }
        binding.shareButton.setTextColor(
            MaterialValueHelper.getPrimaryTextColor(
                this,
                ColorUtil.isColorLight(ThemeStore.accentColor(this))
            )
        )
        binding.shareButton.backgroundTintList =
            ColorStateList.valueOf(ThemeStore.accentColor(this))
    }

    private fun setColors(colorLight: Boolean, color: Int) {
        setLightStatusbar(colorLight)
        binding.toolbar.setTitleTextColor(
            MaterialValueHelper.getPrimaryTextColor(
                this@ShareInstagramStory,
                colorLight
            )
        )
        binding.toolbar.navigationIcon?.setTintList(
            ColorStateList.valueOf(
                MaterialValueHelper.getPrimaryTextColor(
                    this@ShareInstagramStory,
                    colorLight
                )
            )
        )
        binding.mainContent.background =
            GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(color, Color.BLACK)
            )
    }
}
