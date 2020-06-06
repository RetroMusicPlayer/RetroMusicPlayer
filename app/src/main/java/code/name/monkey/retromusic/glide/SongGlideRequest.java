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

package code.name.monkey.retromusic.glide;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.signature.MediaStoreSignature;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTranscoder;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * Created by hemanths on 2019-09-15.
 */
public class SongGlideRequest {

    private static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.NONE;
    private static final int DEFAULT_ERROR_IMAGE = R.drawable.default_audio_art;
    private static final int DEFAULT_ANIMATION = android.R.anim.fade_in;

    @NonNull
    private static DrawableTypeRequest createBaseRequest(@NonNull RequestManager requestManager,
                                                         @NonNull Song song,
                                                         boolean ignoreMediaStore) {
        if (ignoreMediaStore) {
            return requestManager.load(new AudioFileCover(song.getData()));
        } else {
            return requestManager.loadFromMediaStore(MusicUtil.getMediaStoreAlbumCoverUri(song.getAlbumId()));
        }
    }

    @NonNull
    private static Key createSignature(@NonNull Song song) {
        return new MediaStoreSignature("", song.getDateModified(), 0);
    }

    public static class Builder {
        final RequestManager requestManager;
        final Song song;
        boolean ignoreMediaStore;

        private Builder(@NonNull RequestManager requestManager, Song song) {
            this.requestManager = requestManager;
            this.song = song;
        }

        @NonNull
        public static Builder from(@NonNull RequestManager requestManager, Song song) {
            return new Builder(requestManager, song);
        }

        @NonNull
        public PaletteBuilder generatePalette(@NonNull Context context) {
            return new PaletteBuilder(this, context);
        }

        @NonNull
        public BitmapBuilder asBitmap() {
            return new BitmapBuilder(this);
        }

        @NonNull
        public Builder checkIgnoreMediaStore(@NonNull Context context) {
            return ignoreMediaStore(PreferenceUtil.INSTANCE.isIgnoreMediaStoreArtwork());
        }

        @NonNull
        public Builder ignoreMediaStore(boolean ignoreMediaStore) {
            this.ignoreMediaStore = ignoreMediaStore;
            return this;
        }

        @NonNull
        public DrawableRequestBuilder<GlideDrawable> build() {
            //noinspection unchecked
            return createBaseRequest(requestManager, song, ignoreMediaStore)
                    .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                    .placeholder(DEFAULT_ERROR_IMAGE)
                    .animate(DEFAULT_ANIMATION)
                    .signature(createSignature(song));
        }
    }

    public static class BitmapBuilder {
        private final Builder builder;

        BitmapBuilder(Builder builder) {
            this.builder = builder;
        }

        public BitmapRequestBuilder<?, Bitmap> build() {
            //noinspection unchecked
            return createBaseRequest(builder.requestManager, builder.song, builder.ignoreMediaStore)
                    .asBitmap()
                    .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                    .error(DEFAULT_ERROR_IMAGE)
                    .animate(DEFAULT_ANIMATION)
                    .signature(createSignature(builder.song));
        }
    }

    public static class PaletteBuilder {
        final Context context;
        private final Builder builder;

        PaletteBuilder(Builder builder, Context context) {
            this.builder = builder;
            this.context = context;
        }

        public BitmapRequestBuilder<?, BitmapPaletteWrapper> build() {
            //noinspection unchecked
            return createBaseRequest(builder.requestManager, builder.song, builder.ignoreMediaStore)
                    .asBitmap()
                    .transcode(new BitmapPaletteTranscoder(context), BitmapPaletteWrapper.class)
                    .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                    .error(DEFAULT_ERROR_IMAGE)
                    .animate(DEFAULT_ANIMATION)
                    .signature(createSignature(builder.song));
        }
    }
}
