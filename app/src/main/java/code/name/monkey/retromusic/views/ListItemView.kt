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

package code.name.monkey.retromusic.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.ListItemViewNoCardBinding
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.show

/**
 * Created by hemanths on 2019-10-02.
 */
class ListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding =
        ListItemViewNoCardBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.ListItemView) {
            if (hasValue(R.styleable.ListItemView_listItemIcon)) {
                binding.icon.setImageDrawable(getDrawable(R.styleable.ListItemView_listItemIcon))
            } else {
                binding.icon.hide()
            }

            binding.title.text = getText(R.styleable.ListItemView_listItemTitle)
            if (hasValue(R.styleable.ListItemView_listItemSummary)) {
                binding.summary.text = getText(R.styleable.ListItemView_listItemSummary)
            } else {
                binding.summary.hide()
            }
        }
    }

    fun setSummary(appVersion: String) {
        binding.summary.show()
        binding.summary.text = appVersion
    }
}