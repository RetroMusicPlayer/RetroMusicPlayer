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
package io.github.muntashirakon.music.views

import android.content.Context
import android.util.AttributeSet
import io.github.muntashirakon.music.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel


class RetroShapeableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = -1
) : ShapeableImageView(context, attrs, defStyle) {


    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.RetroShapeableImageView, defStyle, -1)
        val cornerSize = typedArray.getDimension(R.styleable.RetroShapeableImageView_retroCornerSize, 0f)
        val circleShape = typedArray.getBoolean(R.styleable.RetroShapeableImageView_circleShape, false)
        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val radius = width / 2f
            shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(radius)
        }
        typedArray.recycle()
    }

    private fun updateCornerSize(cornerSize: Float) {
        shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setAllCorners(CornerFamily.ROUNDED, cornerSize)
            .build()
    }

    //For square
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}