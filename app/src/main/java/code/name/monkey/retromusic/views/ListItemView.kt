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
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.ListItemViewNoCardBinding
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.show

/**
 * Created by hemanths on 2019-10-02.
 */
class ListItemView : FrameLayout {

    private lateinit var binding: ListItemViewNoCardBinding

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = ListItemViewNoCardBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListItemView)
        if (typedArray.hasValue(R.styleable.ListItemView_listItemIcon)) {
            binding.icon.setImageDrawable(typedArray.getDrawable(R.styleable.ListItemView_listItemIcon))
        } else {
            binding.icon.hide()
        }

        binding.title.text = typedArray.getText(R.styleable.ListItemView_listItemTitle)
        if (typedArray.hasValue(R.styleable.ListItemView_listItemSummary)) {
            binding.summary.text = typedArray.getText(R.styleable.ListItemView_listItemSummary)
        } else {
            binding.summary.hide()
        }
        typedArray.recycle()
    }

    fun setSummary(appVersion: String) {
        binding.summary.show()
        binding.summary.text = appVersion
    }
}