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

package code.name.monkey.retromusic.glide

import android.graphics.drawable.Drawable

import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.util.Util

open class RetroSimpleTarget<T> @JvmOverloads constructor(private val width: Int = Target.SIZE_ORIGINAL, private val height: Int = Target.SIZE_ORIGINAL) : Target<T> {

    private var request: Request? = null

    override fun getRequest(): Request? {
        return request
    }

    override fun setRequest(request: Request?) {
        this.request = request
    }

    override fun onLoadStarted(placeholder: Drawable?) {

    }

    override fun onLoadFailed(errorDrawable: Drawable?) {

    }

    override fun onResourceReady(resource: T, transition: Transition<in T>?) {

    }

    override fun onLoadCleared(placeholder: Drawable?) {

    }

    override fun getSize(cb: SizeReadyCallback) {
        if (!Util.isValidDimensions(width, height)) {
            throw IllegalArgumentException(
                    "Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given" + " width: "
                            + width + " and height: " + height + ", either provide dimensions in the constructor"
                            + " or call override()")
        }
        cb.onSizeReady(width, height)
    }

    override fun removeCallback(cb: SizeReadyCallback) {

    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onDestroy() {

    }
}