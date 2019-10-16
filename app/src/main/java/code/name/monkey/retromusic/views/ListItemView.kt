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
import android.view.View
import android.widget.FrameLayout
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.hide
import kotlinx.android.synthetic.main.list_item_view.view.*

/**
 * Created by hemanths on 2019-10-02.
 */
class ListItemView : FrameLayout {
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.list_item_view, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListItemView)
        appCompatImageView.setImageDrawable(typedArray.getDrawable(R.styleable.ListItemView_listItemIcon))
        title.text = typedArray.getText(R.styleable.ListItemView_listItemTitle)
        if (typedArray.hasValue(R.styleable.ListItemView_listItemSummary)) {
            summary.text = typedArray.getText(R.styleable.ListItemView_listItemSummary)
        } else {
            summary.hide()
        }
        typedArray.recycle()
    }
}
