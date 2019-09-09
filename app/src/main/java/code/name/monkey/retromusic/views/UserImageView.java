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

package code.name.monkey.retromusic.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.GlideApp;
import code.name.monkey.retromusic.util.PreferenceUtil;

import static code.name.monkey.retromusic.Constants.USER_PROFILE;

public class UserImageView extends CircularImageView implements SharedPreferences.OnSharedPreferenceChangeListener {
    public UserImageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public UserImageView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UserImageView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(@NonNull Context context) {
        resetImage(context);
    }

    private void resetImage(@NonNull Context context) {
        GlideApp.with(context)
                .asDrawable()
                .placeholder(R.drawable.ic_account_white_24dp)
                .fallback(R.drawable.ic_account_white_24dp)
                .load(new File(PreferenceUtil.getInstance(context).getProfileImage(), USER_PROFILE))
                .into(new Target<Drawable>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        setImageDrawable(placeholder);
                        setBackgroundColor(Color.TRANSPARENT);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        setImageDrawable(errorDrawable);
                        setBackgroundColor(Color.TRANSPARENT);

                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        setImageDrawable(resource);
                        setBackgroundColor(Color.TRANSPARENT);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {
                        cb.onSizeReady(96, 96);
                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, @NonNull String key) {
        if (key.equals(PreferenceUtil.PROFILE_IMAGE_PATH)) {
            resetImage(getContext());
        }
    }
}
