/*
 * Copyright (C) 2012 Andrew Neal Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package code.name.monkey.retromusic.helper

import android.provider.MediaStore

/**
 * Holds all of the sort orders for each list type.
 *
 * @author Andrew Neal (andrewdneal@gmail.com)
 */
/**
 * This class is never instantiated
 */
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
            const val ALBUM_NUMBER_OF_SONGS = MediaStore.Audio.Albums.NUMBER_OF_SONGS + " DESC"

            /* Album sort order artist */
            const val ALBUM_ARTIST = (MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
                    + ", " + MediaStore.Audio.Albums.DEFAULT_SORT_ORDER)

            /* Album sort order year */
            const val ALBUM_YEAR = MediaStore.Audio.Media.YEAR + " DESC"
        }
    }

    /**
     * Song sort order entries.
     */
    interface SongSortOrder {
        companion object {

            /* Song sort order A-Z */
            const val SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

            /* Song sort order Z-A */
            const val SONG_Z_A = "$SONG_A_Z DESC"

            /* Song sort order artist */
            const val SONG_ARTIST = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER

            /* Song sort order album */
            const val SONG_ALBUM = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER

            /* Song sort order year */
            const val SONG_YEAR = MediaStore.Audio.Media.YEAR + " DESC"

            /* Song sort order duration */
            const val SONG_DURATION = MediaStore.Audio.Media.DURATION + " DESC"

            /* Song sort order date */
            const val SONG_DATE = MediaStore.Audio.Media.DATE_ADDED + " DESC"

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
            const val SONG_TRACK_LIST = (MediaStore.Audio.Media.TRACK + ", "
                    + MediaStore.Audio.Media.DEFAULT_SORT_ORDER)

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

}