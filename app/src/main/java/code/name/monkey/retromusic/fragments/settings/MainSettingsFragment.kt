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

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.Constants.USER_PROFILE
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.SettingsActivity
import code.name.monkey.retromusic.util.Compressor
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main_settings.*
import java.io.File
import java.util.*


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
            R.id.aboutSettings -> NavigationUtil.goToAbout(activity!!)
        }
    }

    private val settingsIcons = arrayOf(R.id.general_settings_icon, R.id.audio_settings_icon, R.id.now_playing_settings_icon, R.id.personalize_settings_icon, R.id.image_settings_icon, R.id.notification_settings_icon, R.id.other_settings_icon)

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

        text.setTextColor(ThemeStore.textColorSecondary(context!!))
        text.text = PreferenceUtil.getInstance().userBio
        titleWelcome.setTextColor(ThemeStore.textColorPrimary(context!!))
        titleWelcome.text = String.format("%s %s!", getTimeOfTheDay(), PreferenceUtil.getInstance().userName)
        loadImageFromStorage()
        userInfoContainer.setOnClickListener { NavigationUtil.goToUserInfo(activity!!) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }

    private val disposable = CompositeDisposable()
    private fun loadImageFromStorage() {

        disposable.add(Compressor(context!!)
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(
                        File(PreferenceUtil.getInstance().profileImage, USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userImage.setImageBitmap(it) }, {
                    userImage.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_person_flat))
                }))
    }

    private fun inflateFragment(fragment: Fragment, @StringRes title: Int) {
        if (activity != null) {
            (activity as SettingsActivity).setupFragment(fragment, title)
        }
    }

    private fun getTimeOfTheDay(): String {
        var message = getString(R.string.title_good_day)
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        when (timeOfDay) {
            in 0..5 -> message = getString(R.string.title_good_night)
            in 6..11 -> message = getString(R.string.title_good_morning)
            in 12..15 -> message = getString(R.string.title_good_afternoon)
            in 16..19 -> message = getString(R.string.title_good_evening)
            in 20..23 -> message = getString(R.string.title_good_night)
        }
        return message
    }
}