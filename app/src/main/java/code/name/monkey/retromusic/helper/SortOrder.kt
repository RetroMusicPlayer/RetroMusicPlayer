/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.helper

import android.provider.MediaStore
import code.name.monkey.retromusic.ALBUM_ARTIST

class SortOrder {

    /**
     * Artist sort order entries.
     */
    interface ArtistSortOrder {

        companion object {

            /* Artist sort order A-Z */
            const val ARTIST_A_Z = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER

            /* Artist sort order Z-A */
            const val ARTIST_Z_A = "$ARTIST_A_Z DESC"

            /* Artist sort order number of songs */
            const val ARTIST_NUMBER_OF_SONGS = MediaStore.Audio.Artists.NUMBER_OF_TRACKS + " DESC"

            /* Artist sort order number of albums */
            const val ARTIST_NUMBER_OF_ALBUMS = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS + " DESC"
        }
    }

    /**
     * Album sort order entries.
     */
    interface AlbumSortOrder {

        companion object {

            /* Album sort order A-Z */
            const val ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER

            /* Album sort order Z-A */
            const val ALBUM_Z_A = "$ALBUM_A_Z DESC"

            /* Album sort order songs */
            const val ALBUM_NUMBER_OF_SONGS =
                MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS + " DESC"

            /* Album Artist sort order artist */
            const val ALBUM_ARTIST = "case when lower(album_artist) is null then 1 else 0 end, lower(album_artist)"

            /* Album sort order year */
            const val ALBUM_YEAR = MediaStore.Audio.Media.YEAR + " DESC"
        }
    }

    /**
     * Song sort order entries.
     */
    interface SongSortOrder {

        companion object {

            const val SONG_DEFAULT = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

            /* Song sort order A-Z */
            const val SONG_A_Z = MediaStore.Audio.Media.TITLE

            /* Song sort order Z-A */
            const val SONG_Z_A = "$SONG_A_Z DESC"

            /* Song sort order artist */
            const val SONG_ARTIST = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER

            /* Song sort order album artist */
            const val SONG_ALBUM_ARTIST = ALBUM_ARTIST

            /* Song sort order album */
            const val SONG_ALBUM = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER

            /* Song sort order year */
            const val SONG_YEAR = MediaStore.Audio.Media.YEAR + " DESC"

            /* Song sort order duration */
            const val SONG_DURATION = MediaStore.Audio.Media.DURATION + " DESC"

            /* Song sort order date */
            const val SONG_DATE = MediaStore.Audio.Media.DATE_ADDED + " DESC"

            /* Song sort modified date */
            const val SONG_DATE_MODIFIED = MediaStore.Audio.Media.DATE_MODIFIED + " DESC"

            /* Song sort order composer*/
            const val COMPOSER = MediaStore.Audio.Media.COMPOSER
        }
    }

    /**
     * Album song sort order entries.
     */
    interface AlbumSongSortOrder {

        companion object {

            /* Album song sort order A-Z */
            const val SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

            /* Album song sort order Z-A */
            const val SONG_Z_A = "$SONG_A_Z DESC"

            /* Album song sort order track list */
            const val SONG_TRACK_LIST = (MediaStore.Audio.Media.TRACK + ", " +
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER)

            /* Album song sort order duration */
            const val SONG_DURATION = SongSortOrder.SONG_DURATION
        }
    }

    /**
     * Artist song sort order entries.
     */
    interface ArtistSongSortOrder {

        companion object {

            /* Artist song sort order A-Z */
            const val SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

            /* Artist song sort order Z-A */
            const val SONG_Z_A = "$SONG_A_Z DESC"

            /* Artist song sort order album */
            const val SONG_ALBUM = MediaStore.Audio.Media.ALBUM

            /* Artist song sort order year */
            const val SONG_YEAR = MediaStore.Audio.Media.YEAR + " DESC"

            /* Artist song sort order duration */
            const val SONG_DURATION = MediaStore.Audio.Media.DURATION + " DESC"

            /* Artist song sort order date */
            const val SONG_DATE = MediaStore.Audio.Media.DATE_ADDED + " DESC"
        }
    }

    /**
     * Artist album sort order entries.
     */
    interface ArtistAlbumSortOrder {

        companion object {

            /* Artist album sort order A-Z */
            const val ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER

            /* Artist album sort order Z-A */
            const val ALBUM_Z_A = "$ALBUM_A_Z DESC"

            /* Artist album sort order year */
            const val ALBUM_YEAR = MediaStore.Audio.Media.YEAR + " DESC"

            /* Artist album sort order year */
            const val ALBUM_YEAR_ASC = MediaStore.Audio.Media.YEAR + " ASC"
        }
    }

    /**
     * Genre sort order entries.
     */
    interface GenreSortOrder {

        companion object {

            /* Genre sort order A-Z */
            const val GENRE_A_Z = MediaStore.Audio.Genres.DEFAULT_SORT_ORDER

            /* Genre sort order Z-A */
            const val ALBUM_Z_A = "$GENRE_A_Z DESC"
        }
    }

    /**
     * Playlist sort order entries.
     */
    interface PlaylistSortOrder {

        companion object {

            /* Playlist sort order A-Z */
            const val PLAYLIST_A_Z = MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER

            /* Playlist sort order Z-A */
            const val PLAYLIST_Z_A = "$PLAYLIST_A_Z DESC"

            /* Playlist sort order number of songs */
            const val PLAYLIST_SONG_COUNT = "playlist_song_count"

            /* Playlist sort order number of songs */
            const val PLAYLIST_SONG_COUNT_DESC = "$PLAYLIST_SONG_COUNT DESC"
        }
    }
}
