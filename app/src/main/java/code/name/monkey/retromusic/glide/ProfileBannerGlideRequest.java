package code.name.monkey.retromusic.glide;

import static code.name.monkey.retromusic.Constants.USER_BANNER;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import code.name.monkey.retromusic.App;
import code.name.monkey.retromusic.R;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.MediaStoreSignature;
import java.io.File;

public class ProfileBannerGlideRequest {
  private static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.NONE;
  private static final int DEFAULT_ERROR_IMAGE = R.drawable.material_design_default;
  private static final int DEFAULT_ANIMATION = android.R.anim.fade_in;

  public static File getBannerModel() {
    File dir = App.Companion.getContext().getFilesDir();
    return new File(dir, USER_BANNER);
  }

  private static BitmapTypeRequest<File> createBaseRequest(
      RequestManager requestManager, File profile) {
    return requestManager.load(profile).asBitmap();
  }

  private static Key createSignature(File file) {
    return new MediaStoreSignature("", file.lastModified(), 0);
  }

  public static class Builder {
    private RequestManager requestManager;
    private File profile;

    private Builder(RequestManager requestManager, File profile) {
      this.requestManager = requestManager;
      this.profile = profile;
    }

    public static Builder from(@NonNull RequestManager requestManager, File profile) {
      return new Builder(requestManager, profile);
    }

    @NonNull
    public BitmapRequestBuilder<File, Bitmap> build() {
      //noinspection unchecked
      return createBaseRequest(requestManager, profile)
          .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
          .placeholder(DEFAULT_ERROR_IMAGE)
          .animate(DEFAULT_ANIMATION)
          .signature(createSignature(profile));
    }
  }

  public static class BitmapBuilder {
    private final Builder builder;

    BitmapBuilder(Builder builder) {
      this.builder = builder;
    }

    public BitmapRequestBuilder<?, Bitmap> build() {
      //noinspection unchecked
      return createBaseRequest(builder.requestManager, builder.profile)
          .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
          .error(DEFAULT_ERROR_IMAGE)
          .animate(DEFAULT_ANIMATION)
          .signature(createSignature(builder.profile));
    }
  }
}
