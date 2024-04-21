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

package code.name.monkey.retromusic.auto

/**
 * Created by Beesham Sarendranauth (Beesham)
 */
object AutoMediaIDHelper {
    // Media IDs used on browseable items of MediaBrowser
    const val MEDIA_ID_EMPTY_ROOT = "__EMPTY_ROOT__"
    const val MEDIA_ID_ROOT = "__ROOT__"
    const val MEDIA_ID_MUSICS_BY_SEARCH = "__BY_SEARCH__" // TODO

    const val MEDIA_ID_MUSICS_BY_HISTORY = "__BY_HISTORY__"
    const val MEDIA_ID_MUSICS_BY_TOP_TRACKS = "__BY_TOP_TRACKS__"
    const val MEDIA_ID_MUSICS_BY_SUGGESTIONS = "__BY_SUGGESTIONS__"
    const val MEDIA_ID_MUSICS_BY_PLAYLIST = "__BY_PLAYLIST__"
    const val MEDIA_ID_MUSICS_BY_ALBUM = "__BY_ALBUM__"
    const val MEDIA_ID_MUSICS_BY_ARTIST = "__BY_ARTIST__"
    const val MEDIA_ID_MUSICS_BY_ALBUM_ARTIST = "__BY_ALBUM_ARTIST__"
    const val MEDIA_ID_MUSICS_BY_GENRE = "__BY_GENRE__"
    const val MEDIA_ID_MUSICS_BY_SHUFFLE = "__BY_SHUFFLE__"
    const val MEDIA_ID_MUSICS_BY_QUEUE = "__BY_QUEUE__"
    const val RECENT_ROOT = "__RECENT__"

    private const val CATEGORY_SEPARATOR = "__/__"
    private const val LEAF_SEPARATOR = "__|__"

    /**
     * Create a String value that represents a playable or a browsable media.
     *
     * Encode the media browsable categories, if any, and the unique music ID, if any,
     * into a single String mediaID.
     * MediaIDs are of the form `__/__` `__|__`, to make it
     * easy to find the category (like genre) that a music was selected from, so we
     * can correctly build the playing queue. This is specially useful when
     * one music can appear in more than one list, like "by genre -> genre_1"
     * and "by artist -> artist_1".
     *
     * @param mediaID    Unique ID for playable items, or null for browseable items.
     * @param categories Hierarchy of categories representing this item's browsing parents.
     * @return A hierarchy-aware media ID.
     */
    fun createMediaID(mediaID: String?, vararg categories: String?): String {
        val sb = StringBuilder()
        for (i in categories.indices) {
            require(isValidCategory(categories[i])) {
                "Invalid category: " + categories[i]
            }
            sb.append(categories[i])
            if (i < categories.size - 1) {
                sb.append(CATEGORY_SEPARATOR)
            }
        }
        if (mediaID != null) {
            sb.append(LEAF_SEPARATOR).append(mediaID)
        }
        return sb.toString()
    }

    fun extractCategory(mediaID: String): String {
        val pos = mediaID.indexOf(LEAF_SEPARATOR)
        return if (pos >= 0) {
            mediaID.substring(0, pos)
        } else {
            mediaID
        }
    }

    fun extractMusicID(mediaID: String): String? {
        val pos = mediaID.indexOf(LEAF_SEPARATOR)
        return if (pos >= 0) {
            mediaID.substring(pos + LEAF_SEPARATOR.length)
        } else {
            null
        }
    }

    fun isBrowsable(mediaID: String): Boolean = !mediaID.contains(LEAF_SEPARATOR)

    private fun isValidCategory(category: String?): Boolean = category == null ||
        !category.contains(CATEGORY_SEPARATOR) && !category.contains(LEAF_SEPARATOR)
}
