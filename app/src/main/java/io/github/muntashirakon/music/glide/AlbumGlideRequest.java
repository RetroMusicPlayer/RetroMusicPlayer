package io.github.muntashirakon.music.glide;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import io.github.muntashirakon.music.R;
import io.github.muntashirakon.music.glide.audiocover.AudioFileCover;
import io.github.muntashirakon.music.glide.palette.BitmapPaletteTranscoder;
import io.github.muntashirakon.music.glide.palette.BitmapPaletteWrapper;
import io.github.muntashirakon.music.model.Song;
import io.github.muntashirakon.music.util.MusicUtil;
import io.github.muntashirakon.music.util.PreferenceUtil;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.signature.MediaStoreSignature;

public class AlbumGlideRequest {
  private static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.NONE;
  private static final int DEFAULT_ERROR_IMAGE = R.drawable.default_album_art;
  private static final int DEFAULT_ANIMATION = android.R.anim.fade_in;

  @NonNull
  private static DrawableTypeRequest createBaseRequest(
      @NonNull RequestManager requestManager, @NonNull Song song, boolean ignoreMediaStore) {
    if (ignoreMediaStore) {
      return requestManager.load(new AudioFileCover(song.getData()));
    } else {
      return requestManager.loadFromMediaStore(
          MusicUtil.INSTANCE.getMediaStoreAlbumCoverUri(song.getAlbumId()));
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
    public Builder checkIgnoreMediaStore() {
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
          .error(DEFAULT_ERROR_IMAGE)
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
          .dontTransform()
          .signature(createSignature(builder.song));
    }
  }

  public static class PaletteBuilder {
    private final Context context;
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
