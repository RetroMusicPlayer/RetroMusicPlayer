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

package code.name.monkey.retromusic.auto;

import androidx.annotation.NonNull;

/**
 * Created by Beesham Sarendranauth (Beesham)
 */
public class AutoMediaIDHelper {

    // Media IDs used on browseable items of MediaBrowser
    public static final String MEDIA_ID_EMPTY_ROOT = "__EMPTY_ROOT__";
    public static final String MEDIA_ID_ROOT = "__ROOT__";
    public static final String MEDIA_ID_MUSICS_BY_SEARCH = "__BY_SEARCH__";  // TODO
    public static final String MEDIA_ID_MUSICS_BY_HISTORY = "__BY_HISTORY__";
    public static final String MEDIA_ID_MUSICS_BY_TOP_TRACKS = "__BY_TOP_TRACKS__";
    public static final String MEDIA_ID_MUSICS_BY_SUGGESTIONS = "__BY_SUGGESTIONS__";
    public static final String MEDIA_ID_MUSICS_BY_PLAYLIST = "__BY_PLAYLIST__";
    public static final String MEDIA_ID_MUSICS_BY_ALBUM = "__BY_ALBUM__";
    public static final String MEDIA_ID_MUSICS_BY_ARTIST = "__BY_ARTIST__";
    public static final String MEDIA_ID_MUSICS_BY_ALBUM_ARTIST = "__BY_ALBUM_ARTIST__";
    public static final String MEDIA_ID_MUSICS_BY_GENRE = "__BY_GENRE__";
    public static final String MEDIA_ID_MUSICS_BY_SHUFFLE = "__BY_SHUFFLE__";
    public static final String MEDIA_ID_MUSICS_BY_QUEUE = "__BY_QUEUE__";
    public static final String RECENT_ROOT = "__RECENT__";

    private static final String CATEGORY_SEPARATOR = "__/__";
    private static final String LEAF_SEPARATOR = "__|__";

    /**
     * Create a String value that represents a playable or a browsable media.
     * <p/>
     * Encode the media browseable categories, if any, and the unique music ID, if any,
     * into a single String mediaID.
     * <p/>
     * MediaIDs are of the form <categoryType>__/__<categoryValue>__|__<musicUniqueId>, to make it
     * easy to find the category (like genre) that a music was selected from, so we
     * can correctly build the playing queue. This is specially useful when
     * one music can appear in more than one list, like "by genre -> genre_1"
     * and "by artist -> artist_1".
     *
     * @param mediaID    Unique ID for playable items, or null for browseable items.
     * @param categories Hierarchy of categories representing this item's browsing parents.
     * @return A hierarchy-aware media ID.
     */
    public static String createMediaID(String mediaID, String... categories) {
        StringBuilder sb = new StringBuilder();
        if (categories != null) {
            for (int i = 0; i < categories.length; i++) {
                if (!isValidCategory(categories[i])) {
                    throw new IllegalArgumentException("Invalid category: " + categories[i]);
                }
                sb.append(categories[i]);
                if (i < categories.length - 1) {
                    sb.append(CATEGORY_SEPARATOR);
                }
            }
        }
        if (mediaID != null) {
            sb.append(LEAF_SEPARATOR).append(mediaID);
        }
        return sb.toString();
    }

    public static String extractCategory(@NonNull String mediaID) {
        int pos = mediaID.indexOf(LEAF_SEPARATOR);
        if (pos >= 0) {
            return mediaID.substring(0, pos);
        }
        return mediaID;
    }

    public static String extractMusicID(@NonNull String mediaID) {
        int pos = mediaID.indexOf(LEAF_SEPARATOR);
        if (pos >= 0) {
            return mediaID.substring(pos + LEAF_SEPARATOR.length());
        }
        return null;
    }

    public static boolean isBrowseable(@NonNull String mediaID) {
        return !mediaID.contains(LEAF_SEPARATOR);
    }

    private static boolean isValidCategory(String category) {
        return category == null ||
                (!category.contains(CATEGORY_SEPARATOR) && !category.contains(LEAF_SEPARATOR));
    }
}
