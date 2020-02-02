/*
 * Copyright (c) 2020 Hemanth Savarala.
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

package code.name.monkey.retromusic.activities

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import androidx.core.view.drawToBitmap
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.Share
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_share_instagram.image
import kotlinx.android.synthetic.main.activity_share_instagram.mainContent
import kotlinx.android.synthetic.main.activity_share_instagram.shareButton
import kotlinx.android.synthetic.main.activity_share_instagram.shareText
import kotlinx.android.synthetic.main.activity_share_instagram.shareTitle
import kotlinx.android.synthetic.main.activity_share_instagram.toolbar

/**
 * Created by hemanths on 2020-02-02.
 */

class ShareInstagramStory : AbsBaseActivity() {

    companion object {
        const val EXTRA_SONG = "extra_song"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_instagram)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setLightNavigationBar(true)

        val toolbarColor = ATHUtil.resolveColor(this, R.attr.colorSurface)
        toolbar.setBackgroundColor(toolbarColor)
        ToolbarContentTintHelper.colorBackButton(toolbar)
        setSupportActionBar(toolbar)

        val song = intent.extras?.getParcelable<Song>(EXTRA_SONG)
        song?.let { songFinal ->
            SongGlideRequest.Builder.from(Glide.with(this), songFinal)
                .asBitmap()
                .build()
                .into(image)

            shareTitle.text = songFinal.title
            shareText.text = songFinal.artistName
            shareButton.setOnClickListener {
                val path: String = Media.insertImage(
                    contentResolver,
                    mainContent.drawToBitmap(Bitmap.Config.ARGB_8888),
                    "Design", null
                )
                val uri = Uri.parse(path)
                Share.shareFileToInstagram(
                    this@ShareInstagramStory,
                    songFinal,
                    uri
                )
            }
        }
        shareButton.backgroundTintList = ColorStateList.valueOf(ThemeStore.accentColor(this))
    }
}
