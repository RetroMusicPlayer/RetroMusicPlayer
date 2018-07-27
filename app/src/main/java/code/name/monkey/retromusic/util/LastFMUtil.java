package code.name.monkey.retromusic.util;

import code.name.monkey.retromusic.rest.model.LastFmAlbum.Album.Image;
import code.name.monkey.retromusic.rest.model.LastFmArtist;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LastFMUtil {

  public static String getLargestAlbumImageUrl(List<Image> list) {
    Map hashMap = new HashMap();
    for (Image image : list) {
      Object obj = null;
      String size = image.getSize();
      if (size == null) {
        obj = ImageSize.UNKNOWN;
      } else {
        try {
          obj = ImageSize.valueOf(size.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException ignored) {
        }
      }
      if (obj != null) {
        hashMap.put(obj, image.getText());
      }
    }
    return getLargestImageUrl(hashMap);
  }

  public static String getLargestArtistImageUrl(List<LastFmArtist.Artist.Image> list) {
    Map hashMap = new HashMap();
    for (LastFmArtist.Artist.Image image : list) {
      Object obj = null;
      String size = image.getSize();
      if (size == null) {
        obj = ImageSize.UNKNOWN;
      } else {
        try {
          obj = ImageSize.valueOf(size.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException ignored) {
        }
      }
      if (obj != null) {
        hashMap.put(obj, image.getText());
      }
    }
    return getLargestImageUrl(hashMap);
  }

  private static String getLargestImageUrl(Map<ImageSize, String> map) {
    return map.containsKey(ImageSize.MEGA) ? map.get(ImageSize.MEGA)
        : map.containsKey(ImageSize.EXTRALARGE) ? map.get(ImageSize.EXTRALARGE)
            : map.containsKey(ImageSize.LARGE) ? map.get(ImageSize.LARGE)
                : map.containsKey(ImageSize.MEDIUM) ? map.get(ImageSize.MEDIUM)
                    : map.containsKey(ImageSize.SMALL) ? map.get(ImageSize.SMALL)
                        : map.containsKey(ImageSize.UNKNOWN) ? map.get(ImageSize.UNKNOWN) : null;
  }

  private enum ImageSize {
    SMALL,
    MEDIUM,
    LARGE,
    EXTRALARGE,
    MEGA,
    UNKNOWN
  }
}
