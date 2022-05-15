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
package io.github.muntashirakon.music.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import io.github.muntashirakon.music.R
import com.bumptech.glide.Glide

/** @author Hemanth S (h4h13).
 */
class NetworkImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    CircularImageView(context, attrs, defStyleAttr) {

    init {
        context.withStyledAttributes(attrs, R.styleable.NetworkImageView, 0, 0) {
            val url = getString(R.styleable.NetworkImageView_url_link)
            setImageUrl(context, url!!)
        }
    }

    fun setImageUrl(imageUrl: String) {
        setImageUrl(context, imageUrl)
    }

    private fun setImageUrl(context: Context, imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .error(R.drawable.ic_account)
            .placeholder(R.drawable.ic_account)
            .into(this)
    }
}