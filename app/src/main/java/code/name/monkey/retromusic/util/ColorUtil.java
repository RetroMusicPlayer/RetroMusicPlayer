package code.name.monkey.retromusic.util;

import android.graphics.Bitmap;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import java.util.Collections;
import java.util.Comparator;

public class ColorUtil {

  @Nullable
  public static Palette generatePalette(Bitmap bitmap) {
    if (bitmap == null) return null;
    return Palette.from(bitmap).generate();
  }

  @ColorInt
  public static int getColor(@Nullable Palette palette, int fallback) {
    if (palette != null) {
      if (palette.getVibrantSwatch() != null) {
        return palette.getVibrantSwatch().getRgb();
      } else if (palette.getMutedSwatch() != null) {
        return palette.getMutedSwatch().getRgb();
      } else if (palette.getDarkVibrantSwatch() != null) {
        return palette.getDarkVibrantSwatch().getRgb();
      } else if (palette.getDarkMutedSwatch() != null) {
        return palette.getDarkMutedSwatch().getRgb();
      } else if (palette.getLightVibrantSwatch() != null) {
        return palette.getLightVibrantSwatch().getRgb();
      } else if (palette.getLightMutedSwatch() != null) {
        return palette.getLightMutedSwatch().getRgb();
      } else if (!palette.getSwatches().isEmpty()) {
        return Collections.max(palette.getSwatches(), SwatchComparator.getInstance()).getRgb();
      }
    }
    return fallback;
  }

  private static class SwatchComparator implements Comparator<Palette.Swatch> {
    private static SwatchComparator sInstance;

    static SwatchComparator getInstance() {
      if (sInstance == null) {
        sInstance = new SwatchComparator();
      }
      return sInstance;
    }

    @Override
    public int compare(Palette.Swatch lhs, Palette.Swatch rhs) {
      return lhs.getPopulation() - rhs.getPopulation();
    }
  }
}
