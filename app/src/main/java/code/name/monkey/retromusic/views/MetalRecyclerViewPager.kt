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
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.util.RetroUtil

class MetalRecyclerViewPager : RecyclerView {
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private var itemMargin: Int = 0

    fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MetalRecyclerViewPager, 0, 0)
        itemMargin = typedArray.getDimension(R.styleable.MetalRecyclerViewPager_itemMargin, 0f).toInt()
        typedArray.recycle()

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is MetalAdapter) {
            adapter.setItemMargin(itemMargin)
            adapter.updateDisplayMetrics()
        } else {
            throw IllegalArgumentException("Only MetalAdapter is allowed here")
        }
        super.setAdapter(adapter)
    }

    abstract class MetalAdapter<VH : MetalViewHolder>(@NonNull val displayMetrics: DisplayMetrics) : RecyclerView.Adapter<VH>() {
        private var itemMargin: Int = 0
        private var itemWidth: Int = 0

        fun setItemMargin(itemMargin: Int) {
            this.itemMargin = itemMargin
        }

        fun updateDisplayMetrics() {
            itemWidth = if (RetroUtil.isTablet()) {
                displayMetrics.widthPixels / 2 - itemMargin * 3
            } else if (RetroUtil.isLandscape()) {
                ((displayMetrics.widthPixels / 1.5f) - itemMargin).toInt()
            } else {
                displayMetrics.widthPixels - itemMargin
            }
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val currentItemWidth = itemWidth
            if (position == 0) {
                //currentItemWidth += itemMargin;
                holder.rootLayout.setPadding(0, 0, 0, 0);
            } else if (position == itemCount - 1) {
                //currentItemWidth += itemMargin;
                holder.rootLayout.setPadding(0, 0, 0, 0);
            }

            val height = holder.rootLayout.layoutParams.height
            holder.rootLayout.layoutParams = ViewGroup.LayoutParams(currentItemWidth, height)
        }
    }

    abstract class MetalViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {
        var rootLayout: ViewGroup = itemView.findViewById(R.id.root_layout)
    }
}
