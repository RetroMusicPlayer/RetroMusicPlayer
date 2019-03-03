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

package code.name.monkey.retromusic.util;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import code.name.monkey.retromusic.rest.model.LastFmAlbum.Album.Image;
import code.name.monkey.retromusic.rest.model.LastFmArtist;

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
