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

package code.name.monkey.retromusic.preferences

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEDialogPreference
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.colorControlNormal
import code.name.monkey.retromusic.fragments.NowPlayingScreen
import code.name.monkey.retromusic.fragments.NowPlayingScreen.*
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NowPlayingScreenPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : ATEDialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    private val mLayoutRes = R.layout.preference_dialog_now_playing_screen

    override fun getDialogLayoutResource(): Int {
        return mLayoutRes
    }

    init {
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            context.colorControlNormal(),
            SRC_IN
        )
    }
}

class NowPlayingScreenPreferenceDialog : DialogFragment(), ViewPager.OnPageChangeListener {

    private var viewPagerPosition: Int = 0

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        this.viewPagerPosition = position
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.preference_dialog_now_playing_screen, null)
        val viewPager = view.findViewById<ViewPager>(R.id.now_playing_screen_view_pager)
            ?: throw  IllegalStateException("Dialog view must contain a ViewPager with id 'now_playing_screen_view_pager'")
        viewPager.adapter = NowPlayingScreenAdapter(requireContext())
        viewPager.addOnPageChangeListener(this)
        viewPager.pageMargin = ViewUtil.convertDpToPixel(32f, resources).toInt()
        viewPager.currentItem = PreferenceUtil.nowPlayingScreen.ordinal

        return MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
            .setTitle(R.string.pref_title_now_playing_screen_appearance)
            .setCancelable(false)
            .setPositiveButton(R.string.set) { _, _ ->
                val nowPlayingScreen = values()[viewPagerPosition]
                if (isNowPlayingThemes(nowPlayingScreen)) {
                    val result =
                        getString(nowPlayingScreen.titleRes) + " theme is Pro version feature."
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                    NavigationUtil.goToProVersion(requireContext())
                } else {
                    PreferenceUtil.nowPlayingScreen = nowPlayingScreen
                }
            }
            .setView(view)
            .create()
        /*.show {
            title(R.string.pref_title_now_playing_screen_appearance)
            positiveButton(R.string.set) {
                val nowPlayingScreen = values()[viewPagerPosition]
                if (isNowPlayingThemes(nowPlayingScreen)) {
                    val result =
                        getString(nowPlayingScreen.titleRes) + " theme is Pro version feature."
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                    NavigationUtil.goToProVersion(requireContext())
                } else {
                    PreferenceUtilKT.nowPlayingScreen = nowPlayingScreen
                }
            }

            negativeButton(android.R.string.cancel)
            customView(view = view, scrollable = false, noVerticalPadding = false)
        }*/
    }

    companion object {
        fun newInstance(): NowPlayingScreenPreferenceDialog {
            return NowPlayingScreenPreferenceDialog()
        }
    }
}

private class NowPlayingScreenAdapter(private val context: Context) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val nowPlayingScreen = values()[position]

        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(
            R.layout.preference_now_playing_screen_item,
            collection,
            false
        ) as ViewGroup
        collection.addView(layout)

        val image = layout.findViewById<ImageView>(R.id.image)
        val title = layout.findViewById<TextView>(R.id.title)
        val proText = layout.findViewById<TextView>(R.id.proText)
        Glide.with(context).load(nowPlayingScreen.drawableResId).into(image)
        title.setText(nowPlayingScreen.titleRes)
        if (isNowPlayingThemes(nowPlayingScreen)) {
            proText.setText(R.string.pro)
        } else {
            proText.setText(R.string.free)
        }
        return layout
    }

    override fun destroyItem(
        collection: ViewGroup,
        position: Int,
        view: Any
    ) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return values().size
    }

    override fun isViewFromObject(view: View, instance: Any): Boolean {
        return view === instance
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(values()[position].titleRes)
    }
}

private fun isNowPlayingThemes(screen: NowPlayingScreen): Boolean {
    return (screen == Full ||
            screen == Card ||
            screen == Plain ||
            screen == Blur ||
            screen == Color ||
            screen == Simple ||
            screen == BlurCard ||
            screen == Circle ||
            screen == Adaptive)
            && !App.isProVersion()
}