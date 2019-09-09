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

import androidx.annotation.NonNull;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.annotation.GlideType;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;

import code.name.monkey.retromusic.App;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.artistimage.ArtistImage;
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.ArtistSignatureUtil;
import code.name.monkey.retromusic.util.CustomArtistImageUtil;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;

@GlideExtension
public final class RetroGlideExtension {
    private RetroGlideExtension() {
    }

    @NonNull
    @GlideType(BitmapPaletteWrapper.class)
    public static void asBitmapPalette(@NonNull RequestBuilder<BitmapPaletteWrapper> requestBuilder) {
    }

    @NonNull
    @GlideOption
    public static RequestOptions artistOptions(@NonNull RequestOptions requestOptions, @NonNull Artist artist) {
        return requestOptions
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.default_artist_art)
                .placeholder(R.drawable.default_artist_art)
                .priority(Priority.LOW)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .signature(createSignature(artist));
    }

    @GlideOption
    @NonNull
    public static RequestOptions songOptions(@NonNull RequestOptions requestOptions, @NonNull Song song) {
        return requestOptions
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.default_album_art)
                //.placeholder(R.drawable.default_album_art)
                .signature(createSignature(song));
    }

    @NonNull
    public static Key createSignature(@NonNull Artist artist) {
        return ArtistSignatureUtil.getInstance().getArtistSignature(artist.getName());
    }

    @NonNull
    public static Key createSignature(@NonNull Song song) {
        return new MediaStoreSignature("", song.getDateModified(), 0);
    }

    @NonNull
    public static Object getArtistModel(@NonNull Artist artist) {
        return getArtistModel(artist, CustomArtistImageUtil.Companion.getInstance(App.Companion.getContext()).hasCustomArtistImage(artist), false);
    }

    @NonNull
    public static Object getArtistModel(@NonNull Artist artist, boolean forceDownload) {
        return getArtistModel(artist, CustomArtistImageUtil.Companion.getInstance(App.Companion.getContext()).hasCustomArtistImage(artist), forceDownload);
    }

    @NonNull
    public static Object getArtistModel(@NonNull Artist artist, boolean hasCustomImage, boolean forceDownload) {
        if (!hasCustomImage) {
            return new ArtistImage(artist.getName(), forceDownload);
        } else {
            return CustomArtistImageUtil.getFile(artist);
        }
    }

    @NonNull
    public static Object getSongModel(@NonNull Song song) {
        return getSongModel(song, PreferenceUtil.getInstance(App.Companion.getContext()).ignoreMediaStoreArtwork());
    }

    @NonNull
    public static Object getSongModel(@NonNull Song song, boolean ignoreMediaStore) {
        if (ignoreMediaStore) {
            return new AudioFileCover(song.getData());
        } else {
            return MusicUtil.getMediaStoreAlbumCoverUri(song.getAlbumId());
        }
    }

    @NonNull
    public static <TranscodeType> GenericTransitionOptions<TranscodeType> getDefaultTransition() {
        return new GenericTransitionOptions<TranscodeType>().transition(android.R.anim.fade_in);
    }

}