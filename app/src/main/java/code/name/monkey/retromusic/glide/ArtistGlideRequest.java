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
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.App;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.artistimage.ArtistImage;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTranscoder;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.util.ArtistSignatureUtil;
import code.name.monkey.retromusic.util.CustomArtistImageUtil;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;

public class ArtistGlideRequest {
  private static final int DEFAULT_ANIMATION = android.R.anim.fade_in;

  private static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.SOURCE;

  private static final int DEFAULT_ERROR_IMAGE = R.drawable.default_artist_art;

  @NonNull
  private static Key createSignature(@NonNull Artist artist) {
    return ArtistSignatureUtil.getInstance(App.Companion.getContext())
        .getArtistSignature(artist.getName());
  }

  @NonNull
  private static DrawableTypeRequest createBaseRequest(
      @NonNull RequestManager requestManager,
      @NonNull Artist artist,
      boolean noCustomImage,
      boolean forceDownload) {
    boolean hasCustomImage =
        CustomArtistImageUtil.Companion.getInstance(App.Companion.getContext())
            .hasCustomArtistImage(artist);
    if (noCustomImage || !hasCustomImage) {
      return requestManager.load(new ArtistImage(artist));
    } else {
      return requestManager.load(CustomArtistImageUtil.getFile(artist));
    }
  }

  public static class Builder {
    final Artist artist;
    final RequestManager requestManager;
    private Drawable error;
    private boolean forceDownload;
    private boolean noCustomImage;

    private Builder(@NonNull RequestManager requestManager, Artist artist) {
      this.requestManager = requestManager;
      this.artist = artist;
      error =
          TintHelper.createTintedDrawable(
              ContextCompat.getDrawable(App.Companion.getContext(), R.drawable.default_artist_art),
              ThemeStore.Companion.accentColor(App.Companion.getContext()));
    }

    public static Builder from(@NonNull RequestManager requestManager, Artist artist) {
      return new Builder(requestManager, artist);
    }

    public BitmapBuilder asBitmap() {
      return new BitmapBuilder(this);
    }

    public DrawableRequestBuilder<GlideDrawable> build() {
      //noinspection unchecked
      return createBaseRequest(requestManager, artist, noCustomImage, forceDownload)
          .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
          .animate(DEFAULT_ANIMATION)
          .error(DEFAULT_ERROR_IMAGE)
          .priority(Priority.LOW)
          .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
          .dontTransform()
          .signature(createSignature(artist));
    }

    public Builder forceDownload(boolean forceDownload) {
      this.forceDownload = forceDownload;
      return this;
    }

    public PaletteBuilder generatePalette(Context context) {
      return new PaletteBuilder(this, context);
    }

    public Builder noCustomImage(boolean noCustomImage) {
      this.noCustomImage = noCustomImage;
      return this;
    }
  }

  public static class BitmapBuilder {

    private final Builder builder;

    BitmapBuilder(Builder builder) {
      this.builder = builder;
    }

    public BitmapRequestBuilder<?, Bitmap> build() {
      //noinspection unchecked
      return createBaseRequest(
              builder.requestManager, builder.artist, builder.noCustomImage, builder.forceDownload)
          .asBitmap()
          .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
          .animate(DEFAULT_ANIMATION)
          .error(DEFAULT_ERROR_IMAGE)
          .priority(Priority.LOW)
          .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
          .dontTransform()
          .signature(createSignature(builder.artist));
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
      return createBaseRequest(
              builder.requestManager, builder.artist, builder.noCustomImage, builder.forceDownload)
          .asBitmap()
          .transcode(new BitmapPaletteTranscoder(context), BitmapPaletteWrapper.class)
          .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
          .error(DEFAULT_ERROR_IMAGE)
          .animate(DEFAULT_ANIMATION)
          .priority(Priority.LOW)
          .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
          .dontTransform()
          .signature(createSignature(builder.artist));
    }
  }
}
