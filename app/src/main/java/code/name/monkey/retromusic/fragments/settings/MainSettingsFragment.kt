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

package code.name.monkey.retromusic.fragments.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.SettingsActivity
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.util.NavigationUtil
import kotlinx.android.synthetic.main.fragment_main_settings.*


class MainSettingsFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.generalSettings -> inflateFragment(ThemeSettingsFragment(), R.string.general_settings_title)
            R.id.audioSettings -> inflateFragment(AudioSettings(), R.string.pref_header_audio)
            R.id.nowPlayingSettings -> inflateFragment(NowPlayingSettingsFragment(), R.string.now_playing)
            R.id.personalizeSettings -> inflateFragment(PersonaizeSettingsFragment(), R.string.personalize)
            R.id.imageSettings -> inflateFragment(ImageSettingFragment(), R.string.pref_header_images)
            R.id.notificationSettings -> inflateFragment(NotificationSettingsFragment(), R.string.notification)
            R.id.otherSettings -> inflateFragment(OtherSettingsFragment(), R.string.others)
            R.id.aboutSettings -> NavigationUtil.goToAbout(requireActivity())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generalSettings.setOnClickListener(this)
        audioSettings.setOnClickListener(this)
        nowPlayingSettings.setOnClickListener(this)
        personalizeSettings.setOnClickListener(this)
        imageSettings.setOnClickListener(this)
        notificationSettings.setOnClickListener(this)
        otherSettings.setOnClickListener(this)
        aboutSettings.setOnClickListener(this)

        buyProContainer.apply {
            if (!App.isProVersion()) show() else hide()
            setOnClickListener {
                NavigationUtil.goToProVersion(requireContext())
            }
        }
        buyPremium.setOnClickListener {
            NavigationUtil.goToProVersion(requireContext())
        }
        MaterialUtil.setTint(buyPremium)
        val primaryColor = MaterialValueHelper.getPrimaryTextColor(requireContext(), ColorUtil.isColorLight(ATHUtil.resolveColor(requireContext(), R.attr.colorPrimary)))
        text.setTextColor(ColorUtil.withAlpha(primaryColor, 0.75f))
        text2.setTextColor(primaryColor)
        text3.imageTintList = ColorStateList.valueOf(primaryColor)
    }

    private fun inflateFragment(fragment: Fragment, @StringRes title: Int) {
        (requireActivity() as SettingsActivity).setupFragment(fragment, title)
    }
}