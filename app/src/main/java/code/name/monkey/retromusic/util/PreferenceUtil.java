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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import code.name.monkey.retromusic.App;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.activities.MainActivity;
import code.name.monkey.retromusic.fragments.AlbumCoverStyle;
import code.name.monkey.retromusic.fragments.NowPlayingScreen;
import code.name.monkey.retromusic.fragments.mainactivity.folders.FoldersFragment;
import code.name.monkey.retromusic.helper.SortOrder;
import code.name.monkey.retromusic.helper.SortOrder.AlbumSongSortOrder;
import code.name.monkey.retromusic.model.CategoryInfo;
import code.name.monkey.retromusic.transform.CascadingPageTransformer;
import code.name.monkey.retromusic.transform.DepthTransformation;
import code.name.monkey.retromusic.transform.HingeTransformation;
import code.name.monkey.retromusic.transform.HorizontalFlipTransformation;
import code.name.monkey.retromusic.transform.NormalPageTransformer;
import code.name.monkey.retromusic.transform.VerticalFlipTransformation;
import code.name.monkey.retromusic.transform.VerticalStackTransformer;

public final class PreferenceUtil {
    public static final String LIBRARY_CATEGORIES = "library_categories";
    public static final String DESATURATED_COLOR = "desaturated_color";
    public static final String BLACK_THEME = "black_theme";
    public static final String DIALOG_CORNER = "dialog_corner";
    public static final String KEEP_SCREEN_ON = "keep_screen_on";
    public static final String TOGGLE_HOME_BANNER = "toggle_home_banner";
    public static final String NOW_PLAYING_SCREEN_ID = "now_playing_screen_id";
    public static final String CAROUSEL_EFFECT = "carousel_effect";
    public static final String COLORED_NOTIFICATION = "colored_notification";
    public static final String CLASSIC_NOTIFICATION = "classic_notification";
    public static final String GAPLESS_PLAYBACK = "gapless_playback";
    public static final String ALBUM_ART_ON_LOCKSCREEN = "album_art_on_lockscreen";
    public static final String BLURRED_ALBUM_ART = "blurred_album_art";
    public static final String NEW_BLUR_AMOUNT = "new_blur_amount";
    public static final String SLEEP_TIMER_FINISH_SONG = "sleep_timer_finish_song";
    public static final String TOGGLE_HEADSET = "toggle_headset";
    public static final String DOMINANT_COLOR = "dominant_color";
    public static final String GENERAL_THEME = "general_theme";
    public static final String CIRCULAR_ALBUM_ART = "circular_album_art";
    public static final String USER_NAME = "user_name";
    public static final String USER_BIO = "user_bio";
    public static final String TOGGLE_FULL_SCREEN = "toggle_full_screen";
    public static final String TOGGLE_VOLUME = "toggle_volume";
    public static final String TOGGLE_TAB_TITLES = "toggle_tab_titles";
    public static final String ROUND_CORNERS = "corner_window";
    public static final String TOGGLE_GENRE = "toggle_genre";
    public static final String PROFILE_IMAGE_PATH = "profile_image_path";
    public static final String BANNER_IMAGE_PATH = "banner_image_path";
    public static final String ADAPTIVE_COLOR_APP = "adaptive_color_app";
    public static final String TOGGLE_SEPARATE_LINE = "toggle_separate_line";
    public static final String ALBUM_GRID_STYLE = "album_grid_style";
    public static final String HOME_ARTIST_GRID_STYLE = "home_artist_grid_style";
    public static final String ARTIST_GRID_STYLE = "artist_grid_style";
    public static final String TOGGLE_ADD_CONTROLS = "toggle_add_controls";
    public static final String ALBUM_COVER_STYLE = "album_cover_style_id";
    public static final String ALBUM_COVER_TRANSFORM = "album_cover_transform";
    public static final String TAB_TEXT_MODE = "tab_text_mode";
    public static final String SAF_SDCARD_URI = "saf_sdcard_uri";
    private static final String GENRE_SORT_ORDER = "genre_sort_order";
    private static final String LAST_PAGE = "last_start_page";
    private static final String LAST_MUSIC_CHOOSER = "last_music_chooser";
    private static final String DEFAULT_START_PAGE = "default_start_page";
    private static final String INITIALIZED_BLACKLIST = "initialized_blacklist";
    private static final String ARTIST_SORT_ORDER = "artist_sort_order";
    private static final String ARTIST_SONG_SORT_ORDER = "artist_song_sort_order";
    private static final String ARTIST_ALBUM_SORT_ORDER = "artist_album_sort_order";
    private static final String ALBUM_SORT_ORDER = "album_sort_order";
    private static final String ALBUM_SONG_SORT_ORDER = "album_song_sort_order";
    private static final String SONG_SORT_ORDER = "song_sort_order";
    private static final String ALBUM_GRID_SIZE = "album_grid_size";
    private static final String ALBUM_GRID_SIZE_LAND = "album_grid_size_land";
    private static final String SONG_GRID_SIZE = "song_grid_size";
    private static final String SONG_GRID_SIZE_LAND = "song_grid_size_land";
    private static final String ARTIST_GRID_SIZE = "artist_grid_size";
    private static final String ARTIST_GRID_SIZE_LAND = "artist_grid_size_land";
    private static final String ALBUM_COLORED_FOOTERS = "album_colored_footers";
    private static final String SONG_COLORED_FOOTERS = "song_colored_footers";
    private static final String ARTIST_COLORED_FOOTERS = "artist_colored_footers";
    private static final String ALBUM_ARTIST_COLORED_FOOTERS = "album_artist_colored_footers";
    private static final String COLORED_APP_SHORTCUTS = "colored_app_shortcuts";
    private static final String AUDIO_DUCKING = "audio_ducking";
    private static final String LAST_ADDED_CUTOFF = "last_added_interval";
    private static final String LAST_SLEEP_TIMER_VALUE = "last_sleep_timer_value";
    private static final String NEXT_SLEEP_TIMER_ELAPSED_REALTIME = "next_sleep_timer_elapsed_real_time";
    private static final String IGNORE_MEDIA_STORE_ARTWORK = "ignore_media_store_artwork";
    private static final String LAST_CHANGELOG_VERSION = "last_changelog_version";
    private static final String INTRO_SHOWN = "intro_shown";
    private static final String AUTO_DOWNLOAD_IMAGES_POLICY = "auto_download_images_policy";
    private static final String START_DIRECTORY = "start_directory";
    private static final String SYNCHRONIZED_LYRICS_SHOW = "synchronized_lyrics_show";
    private static final String LOCK_SCREEN = "lock_screen";
    private static final String ALBUM_DETAIL_SONG_SORT_ORDER = "album_detail_song_sort_order";
    private static final String ARTIST_DETAIL_SONG_SORT_ORDER = "artist_detail_song_sort_order";
    private static final String LYRICS_OPTIONS = "lyrics_tab_position";
    private static final String CHOOSE_EQUALIZER = "choose_equalizer";
    private static final String TOGGLE_SHUFFLE = "toggle_shuffle";
    private static final String SONG_GRID_STYLE = "song_grid_style";
    private static final String TOGGLE_ANIMATIONS = "toggle_animations";
    private static final String LAST_KNOWN_LYRICS_TYPE = "LAST_KNOWN_LYRICS_TYPE";
    private static final String ALBUM_DETAIL_STYLE = "album_detail_style";
    private static final String PAUSE_ON_ZERO_VOLUME = "pause_on_zero_volume";
    private static final String NOW_PLAYING_SCREEN = "now_playing_screen";
    private static final String SNOW_FALL_EFFECT = "snow_fall_effect";
    private static final String FILTER_SONG = "filter_song";
    private static PreferenceUtil sInstance;
    private final SharedPreferences mPreferences;

    private PreferenceUtil(@NonNull final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isAllowedToDownloadMetadata(final Context context) {
        switch (getInstance(context).autoDownloadImagesPolicy()) {
            case "always":
                return true;
            case "only_wifi":
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                return netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI && netInfo.isConnectedOrConnecting();
            case "never":
            default:
                return false;
        }
    }

    @NonNull
    public static PreferenceUtil getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(App.Companion.getContext());
        }
        return sInstance;
    }

    @StyleRes
    public static int getThemeResFromPrefValue(@NonNull String themePrefValue) {
        switch (themePrefValue) {
            case "light":
                return R.style.Theme_RetroMusic_Light;
            case "dark":
            default:
                return R.style.Theme_RetroMusic;
        }
    }

    public boolean desaturatedColor() {
        return mPreferences.getBoolean(DESATURATED_COLOR, false);
    }

    public void setDesaturatedColor(boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(DESATURATED_COLOR, value);
        editor.apply();
    }

    public boolean getSleepTimerFinishMusic() {
        return mPreferences.getBoolean(SLEEP_TIMER_FINISH_SONG, false);
    }

    public void setSleepTimerFinishMusic(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(SLEEP_TIMER_FINISH_SONG, value);
        editor.apply();
    }

    public boolean isBlackMode() {
        return mPreferences.getBoolean(BLACK_THEME, false);
    }

    public String getUserBio() {
        return mPreferences.getString(USER_BIO, "");
    }

    public void setUserBio(String bio) {
        mPreferences.edit().putString(USER_BIO, bio).apply();
    }

    public int getFilterLength() {
        return mPreferences.getInt(FILTER_SONG, 20);
    }

    public float getDialogCorner() {
        return mPreferences.getInt(DIALOG_CORNER, 16);
    }

    public boolean isSnowFall() {
        return mPreferences.getBoolean(SNOW_FALL_EFFECT, false);
    }

    public final String getArtistSortOrder() {
        return mPreferences.getString(ARTIST_SORT_ORDER, SortOrder.ArtistSortOrder.ARTIST_A_Z);
    }

    public void setArtistSortOrder(final String sortOrder) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(ARTIST_SORT_ORDER, sortOrder);
        editor.apply();
    }

    public final String getArtistSongSortOrder() {
        return mPreferences.getString(ARTIST_SONG_SORT_ORDER, SortOrder.ArtistSongSortOrder.SONG_A_Z);
    }

    public final boolean isHomeBanner() {
        return mPreferences.getBoolean(TOGGLE_HOME_BANNER, false);
    }

    public final String getArtistAlbumSortOrder() {
        return mPreferences.getString(ARTIST_ALBUM_SORT_ORDER, SortOrder.ArtistAlbumSortOrder.ALBUM_YEAR);
    }

    public final String getAlbumSortOrder() {
        return mPreferences.getString(ALBUM_SORT_ORDER, SortOrder.AlbumSortOrder.ALBUM_A_Z);
    }

    public void setAlbumSortOrder(final String sortOrder) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(ALBUM_SORT_ORDER, sortOrder);
        editor.apply();
    }

    public final String getAlbumSongSortOrder() {
        return mPreferences
                .getString(ALBUM_SONG_SORT_ORDER, AlbumSongSortOrder.SONG_TRACK_LIST);
    }

    public final String getSongSortOrder() {
        return mPreferences.getString(SONG_SORT_ORDER, SortOrder.SongSortOrder.SONG_A_Z);
    }

    public void setSongSortOrder(final String sortOrder) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SONG_SORT_ORDER, sortOrder);
        editor.apply();
    }

    public final String getGenreSortOrder() {
        return mPreferences.getString(GENRE_SORT_ORDER, SortOrder.GenreSortOrder.GENRE_A_Z);
    }

    public boolean isScreenOnEnabled() {
        return mPreferences.getBoolean(KEEP_SCREEN_ON, false);
    }

    public void setInitializedBlacklist() {
        final Editor editor = mPreferences.edit();
        editor.putBoolean(INITIALIZED_BLACKLIST, true);
        editor.apply();
    }

    public final boolean initializedBlacklist() {
        return mPreferences.getBoolean(INITIALIZED_BLACKLIST, false);
    }

    public boolean isExtraMiniExtraControls() {
        return mPreferences.getBoolean(TOGGLE_ADD_CONTROLS, false);
    }

    public boolean carouselEffect() {
        return mPreferences.getBoolean(CAROUSEL_EFFECT, false);
    }

    public boolean isRoundCorners() {
        return mPreferences.getBoolean(ROUND_CORNERS, false);
    }

    public void registerOnSharedPreferenceChangedListener(
            SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener) {
        mPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    public void unregisterOnSharedPreferenceChangedListener(
            SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener) {
        mPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    public final int getDefaultStartPage() {
        return Integer.parseInt(mPreferences.getString(DEFAULT_START_PAGE, "-1"));
    }

    public final int getLastPage() {
        return mPreferences.getInt(LAST_PAGE, R.id.action_song);
    }

    public void setLastPage(final int value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(LAST_PAGE, value);
        editor.apply();
    }

    public void setLastLyricsType(int group) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(LAST_KNOWN_LYRICS_TYPE, group);
        editor.apply();
    }

    public final int getLastMusicChooser() {
        return mPreferences.getInt(LAST_MUSIC_CHOOSER, MainActivity.HOME);
    }

    public void setLastMusicChooser(int value) {
        mPreferences.edit().putInt(LAST_MUSIC_CHOOSER, value).apply();
    }

    public final boolean coloredNotification() {
        return mPreferences.getBoolean(COLORED_NOTIFICATION, true);
    }

    public final void setColoredNotification(boolean b) {
        mPreferences.edit().putBoolean(COLORED_NOTIFICATION, b).apply();
    }

    public final boolean classicNotification() {
        return mPreferences.getBoolean(CLASSIC_NOTIFICATION, false);
    }

    public void setClassicNotification(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(CLASSIC_NOTIFICATION, value);
        editor.apply();
    }

    public final NowPlayingScreen getNowPlayingScreen() {
        int id = mPreferences.getInt(NOW_PLAYING_SCREEN_ID, 0);
        for (NowPlayingScreen nowPlayingScreen : NowPlayingScreen.values()) {
            if (nowPlayingScreen.getId() == id) {
                return nowPlayingScreen;
            }
        }
        return NowPlayingScreen.ADAPTIVE;
    }

    @SuppressLint("CommitPrefEdits")
    public void setNowPlayingScreen(NowPlayingScreen nowPlayingScreen) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(NOW_PLAYING_SCREEN_ID, nowPlayingScreen.getId());
        editor.apply();
    }

    public final AlbumCoverStyle getAlbumCoverStyle() {
        int id = mPreferences.getInt(ALBUM_COVER_STYLE, 0);
        for (AlbumCoverStyle albumCoverStyle : AlbumCoverStyle.values()) {
            if (albumCoverStyle.getId() == id) {
                return albumCoverStyle;
            }
        }
        return AlbumCoverStyle.CARD;
    }

    public void setAlbumCoverStyle(AlbumCoverStyle albumCoverStyle) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(ALBUM_COVER_STYLE, albumCoverStyle.getId());
        editor.apply();
    }

    public void setColoredAppShortcuts(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(COLORED_APP_SHORTCUTS, value);
        editor.apply();
    }

    public final boolean coloredAppShortcuts() {
        return mPreferences.getBoolean(COLORED_APP_SHORTCUTS, true);
    }

    public final boolean gaplessPlayback() {
        return mPreferences.getBoolean(GAPLESS_PLAYBACK, false);
    }

    public final boolean audioDucking() {
        return mPreferences.getBoolean(AUDIO_DUCKING, true);
    }

    public final boolean albumArtOnLockscreen() {
        return mPreferences.getBoolean(ALBUM_ART_ON_LOCKSCREEN, true);
    }

    public final boolean blurredAlbumArt() {
        return mPreferences.getBoolean(BLURRED_ALBUM_ART, false);
    }

    public final boolean ignoreMediaStoreArtwork() {
        return mPreferences.getBoolean(IGNORE_MEDIA_STORE_ARTWORK, false);
    }

    public int getLastSleepTimerValue() {
        return mPreferences.getInt(LAST_SLEEP_TIMER_VALUE, 30);
    }

    public void setLastSleepTimerValue(final int value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(LAST_SLEEP_TIMER_VALUE, value);
        editor.apply();
    }

    public long getNextSleepTimerElapsedRealTime() {
        return mPreferences.getLong(NEXT_SLEEP_TIMER_ELAPSED_REALTIME, -1);
    }

    public void setNextSleepTimerElapsedRealtime(final long value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(NEXT_SLEEP_TIMER_ELAPSED_REALTIME, value);
        editor.apply();
    }

    public final int getAlbumGridSize(Context context) {
        return mPreferences
                .getInt(ALBUM_GRID_SIZE, context.getResources().getInteger(R.integer.default_grid_columns));
    }

    public void setSongGridSize(final int gridSize) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(SONG_GRID_SIZE, gridSize);
        editor.apply();
    }

    public final int getSongGridSize(Context context) {
        return mPreferences
                .getInt(SONG_GRID_SIZE, context.getResources().getInteger(R.integer.default_list_columns));
    }

    public void setArtistGridSize(final int gridSize) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(ARTIST_GRID_SIZE, gridSize);
        editor.apply();
    }

    public final int getArtistGridSize(Context context) {
        return mPreferences.getInt(ARTIST_GRID_SIZE,
                context.getResources().getInteger(R.integer.default_list_artist_columns));
    }

    public void setAlbumGridSizeLand(final int gridSize) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(ALBUM_GRID_SIZE_LAND, gridSize);
        editor.apply();
    }

    public final int getAlbumGridSizeLand(Context context) {
        return mPreferences.getInt(ALBUM_GRID_SIZE_LAND,
                context.getResources().getInteger(R.integer.default_grid_columns_land));
    }

    public void setSongGridSizeLand(final int gridSize) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(SONG_GRID_SIZE_LAND, gridSize);
        editor.apply();
    }

    public final int getSongGridSizeLand(Context context) {
        return mPreferences.getInt(SONG_GRID_SIZE_LAND,
                context.getResources().getInteger(R.integer.default_list_columns_land));
    }

    public void setArtistGridSizeLand(final int gridSize) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(ARTIST_GRID_SIZE_LAND, gridSize);
        editor.apply();
    }

    public final int getArtistGridSizeLand(Context context) {
        return mPreferences.getInt(ARTIST_GRID_SIZE_LAND,
                context.getResources().getInteger(R.integer.default_list_artist_columns_land));
    }

    public void setAlbumGridSize(final int gridSize) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(ALBUM_GRID_SIZE, gridSize);
        editor.apply();
    }

    public void setAlbumColoredFooters(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(ALBUM_COLORED_FOOTERS, value);
        editor.apply();
    }

    public final boolean albumColoredFooters() {
        return mPreferences.getBoolean(ALBUM_COLORED_FOOTERS, false);
    }

    public void setAlbumArtistColoredFooters(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(ALBUM_ARTIST_COLORED_FOOTERS, value);
        editor.apply();
    }

    public final boolean albumArtistColoredFooters() {
        return mPreferences.getBoolean(ALBUM_ARTIST_COLORED_FOOTERS, true);
    }

    public void setSongColoredFooters(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(SONG_COLORED_FOOTERS, value);
        editor.apply();
    }

    public final boolean songColoredFooters() {
        return mPreferences.getBoolean(SONG_COLORED_FOOTERS, false);
    }

    public void setArtistColoredFooters(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(ARTIST_COLORED_FOOTERS, value);
        editor.apply();
    }

    public final boolean artistColoredFooters() {
        return mPreferences.getBoolean(ARTIST_COLORED_FOOTERS, true);
    }

    public void setLastChangeLogVersion(int version) {
        mPreferences.edit().putInt(LAST_CHANGELOG_VERSION, version).apply();
    }

    public final int getLastChangelogVersion() {
        return mPreferences.getInt(LAST_CHANGELOG_VERSION, -1);
    }

    @SuppressLint("CommitPrefEdits")
    public void setIntroShown() {
        // don't use apply here
        mPreferences.edit().putBoolean(INTRO_SHOWN, true).commit();
    }

    public final File getStartDirectory() {
        return new File(mPreferences
                .getString(START_DIRECTORY, FoldersFragment.getDefaultStartDirectory().getPath()));
    }

    public void setStartDirectory(File file) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(START_DIRECTORY, FileUtil.safeGetCanonicalPath(file));
        editor.apply();
    }

    public final boolean introShown() {
        return mPreferences.getBoolean(INTRO_SHOWN, false);
    }

    public final String autoDownloadImagesPolicy() {
        return mPreferences.getString(AUTO_DOWNLOAD_IMAGES_POLICY, "only_wifi");
    }

    public final boolean synchronizedLyricsShow() {
        return mPreferences.getBoolean(SYNCHRONIZED_LYRICS_SHOW, true);
    }

    public int getGeneralTheme() {
        return getThemeResFromPrefValue(mPreferences.getString(GENERAL_THEME, "dark"));
    }

    public void setGeneralTheme(String theme) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(GENERAL_THEME, theme);
        editor.apply();
    }

    @NonNull
    public String getGeneralThemeValue() {
        if (isBlackMode()) return "black";
        else
            return mPreferences.getString(GENERAL_THEME, "dark");
    }

    public String getBaseTheme() {
        return mPreferences.getString(GENERAL_THEME, "dark");
    }

    public long getLastAddedCutoff() {
        final CalendarUtil calendarUtil = new CalendarUtil();
        long interval;

        switch (mPreferences.getString(LAST_ADDED_CUTOFF, "")) {
            case "today":
                interval = calendarUtil.getElapsedToday();
                break;
            case "this_week":
                interval = calendarUtil.getElapsedWeek();
                break;
            case "past_three_months":
                interval = calendarUtil.getElapsedMonths(3);
                break;
            case "this_year":
                interval = calendarUtil.getElapsedYear();
                break;
            case "this_month":
            default:
                interval = calendarUtil.getElapsedMonth();
                break;
        }

        return (System.currentTimeMillis() - interval) / 1000;
    }

    public boolean getAdaptiveColor() {
        return mPreferences.getBoolean(ADAPTIVE_COLOR_APP, false);
    }

    public boolean getLockScreen() {
        return mPreferences.getBoolean(LOCK_SCREEN, false);
    }

    public String getUserName() {
        return mPreferences.getString(USER_NAME, "User");
    }

    public void setUserName(String name) {
        mPreferences.edit().putString(USER_NAME, name).apply();
    }

    public boolean getFullScreenMode() {
        return mPreferences.getBoolean(TOGGLE_FULL_SCREEN, false);
    }

    public void setFullScreenMode(int newValue) {
        mPreferences.edit().putInt(TOGGLE_FULL_SCREEN, newValue).apply();
    }

    public void saveProfileImage(String profileImagePath) {
        mPreferences.edit().putString(PROFILE_IMAGE_PATH, profileImagePath)
                .apply();

    }

    public String getProfileImage() {
        return mPreferences.getString(PROFILE_IMAGE_PATH, "");
    }

    public String getBannerImage() {
        return mPreferences.getString(BANNER_IMAGE_PATH, "");
    }

    public void setBannerImagePath(String bannerImagePath) {
        mPreferences.edit().putString(BANNER_IMAGE_PATH, bannerImagePath)
                .apply();
    }

    public String getAlbumDetailSongSortOrder() {
        return mPreferences
                .getString(ALBUM_DETAIL_SONG_SORT_ORDER, AlbumSongSortOrder.SONG_TRACK_LIST);
    }

    public void setAlbumDetailSongSortOrder(String sortOrder) {
        Editor edit = this.mPreferences.edit();
        edit.putString(ALBUM_DETAIL_SONG_SORT_ORDER, sortOrder);
        edit.apply();
    }

    public String getArtistDetailSongSortOrder() {
        return mPreferences
                .getString(ARTIST_DETAIL_SONG_SORT_ORDER, SortOrder.ArtistSongSortOrder.SONG_A_Z);
    }

    public void setArtistDetailSongSortOrder(String sortOrder) {
        Editor edit = this.mPreferences.edit();
        edit.putString(ARTIST_DETAIL_SONG_SORT_ORDER, sortOrder);
        edit.apply();
    }

    public boolean getVolumeToggle() {
        return mPreferences.getBoolean(TOGGLE_VOLUME, false);
    }

    public int getLyricsOptions() {
        return mPreferences.getInt(LYRICS_OPTIONS, 1);
    }

    public void setLyricsOptions(int i) {
        mPreferences.edit().putInt(LYRICS_OPTIONS, i).apply();
    }

    public boolean getHeadsetPlugged() {
        return mPreferences.getBoolean(TOGGLE_HEADSET, false);
    }

    public boolean isDominantColor() {
        return mPreferences.getBoolean(DOMINANT_COLOR, false);
    }

    public boolean isGenreShown() {
        return mPreferences.getBoolean(TOGGLE_GENRE, false);
    }

    public String getSelectedEqualizer() {
        return mPreferences.getString(CHOOSE_EQUALIZER, "system");
    }

    public boolean isShuffleModeOn() {
        return mPreferences.getBoolean(TOGGLE_SHUFFLE, false);
    }

    public void resetCarouselEffect() {
        mPreferences.edit().putBoolean(CAROUSEL_EFFECT, false).apply();
    }

    public void resetCircularAlbumArt() {
        mPreferences.edit().putBoolean(CIRCULAR_ALBUM_ART, false).apply();
    }

    public String getAlbumDetailsStyle() {
        return mPreferences.getString(ALBUM_DETAIL_STYLE, "0");
    }

    public int getAlbumDetailsStyle(Context context) {
        int pos = Integer.parseInt(Objects.requireNonNull(mPreferences.getString(ALBUM_DETAIL_STYLE, "0")));
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.pref_album_details_style_layout);
        int layoutRes = typedArray.getResourceId(pos, -1);
        typedArray.recycle();
        if (layoutRes == -1) {
            return R.layout.activity_album;
        }
        return layoutRes;
    }

    public void setArtistGridStyle(int viewAs) {
        mPreferences.edit().putInt(ARTIST_GRID_STYLE, viewAs).apply();
    }

    public boolean toggleSeparateLine() {
        return mPreferences.getBoolean(TOGGLE_SEPARATE_LINE, false);
    }

    public int getSongGridStyle() {
        return mPreferences.getInt(SONG_GRID_STYLE, R.layout.item_list);
    }

    public void setSongGridStyle(int viewAs) {
        mPreferences.edit().putInt(SONG_GRID_STYLE, viewAs).apply();
    }

    public boolean enableAnimations() {
        return mPreferences.getBoolean(TOGGLE_ANIMATIONS, false);
    }

    public boolean pauseOnZeroVolume() {
        return mPreferences.getBoolean(PAUSE_ON_ZERO_VOLUME, false);
    }

    public ViewPager.PageTransformer getAlbumCoverTransform() {
        int style = Integer.parseInt(Objects.requireNonNull(mPreferences.getString(ALBUM_COVER_TRANSFORM, "0")));
        switch (style) {
            default:
            case 0:
                return new NormalPageTransformer();
            case 1:
                return new CascadingPageTransformer();
            case 2:
                return new DepthTransformation();
            case 3:
                return new HorizontalFlipTransformation();
            case 4:
                return new VerticalFlipTransformation();
            case 5:
                return new HingeTransformation();
            case 6:
                return new VerticalStackTransformer();
        }
    }

    @LabelVisibilityMode
    public int getTabTitleMode() {
        int mode = Integer.parseInt(mPreferences.getString(TAB_TEXT_MODE, "1"));
        switch (mode) {
            default:
            case 1:
                return LabelVisibilityMode.LABEL_VISIBILITY_LABELED;
            case 0:
                return LabelVisibilityMode.LABEL_VISIBILITY_AUTO;
            case 2:
                return LabelVisibilityMode.LABEL_VISIBILITY_SELECTED;
            case 3:
                return LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED;
        }
    }

    public boolean tabTitles() {
        return getTabTitleMode() != LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED;
    }

    @LayoutRes
    public int getHomeGridStyle(@NonNull Context context) {
        int pos = Integer.parseInt(mPreferences.getString(HOME_ARTIST_GRID_STYLE, "0"));
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.pref_home_grid_style_layout);
        int layoutRes = typedArray.getResourceId(pos, -1);
        typedArray.recycle();
        if (layoutRes == -1) {
            return R.layout.item_artist;
        }
        return layoutRes;
    }

    @LayoutRes
    public int getArtistGridStyle(Context context) {
        int pos = Integer.parseInt(Objects.requireNonNull(mPreferences.getString(ARTIST_GRID_STYLE, "0")));
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.pref_grid_style_layout);
        int layoutRes = typedArray.getResourceId(pos, -1);
        typedArray.recycle();
        if (layoutRes == -1) {
            return R.layout.item_card;
        }
        return layoutRes;
    }

    @LayoutRes
    public int getAlbumGridStyle(Context context) {
        int pos = Integer.parseInt(mPreferences.getString(ALBUM_GRID_STYLE, "0"));
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.pref_grid_style_layout);
        int layoutRes = typedArray.getResourceId(pos, -1);
        typedArray.recycle();
        if (layoutRes == -1) {
            return R.layout.item_card;
        }
        return layoutRes;
    }

    public boolean isClickOrSave() {
        return mPreferences.getBoolean(NOW_PLAYING_SCREEN, false);
    }

    @NonNull
    public List<CategoryInfo> getLibraryCategoryInfos() {
        String data = mPreferences.getString(LIBRARY_CATEGORIES, null);
        if (data != null) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<CategoryInfo>>() {
            }.getType();

            try {
                return gson.fromJson(data, collectionType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }

        return getDefaultLibraryCategoryInfos();
    }

    public void setLibraryCategoryInfos(List<CategoryInfo> categories) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CategoryInfo>>() {
        }.getType();

        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(LIBRARY_CATEGORIES, gson.toJson(categories, collectionType));
        editor.apply();
    }

    @NonNull
    public List<CategoryInfo> getDefaultLibraryCategoryInfos() {
        List<CategoryInfo> defaultCategoryInfos = new ArrayList<>(6);
        defaultCategoryInfos.add(new CategoryInfo(CategoryInfo.Category.HOME, true));
        defaultCategoryInfos.add(new CategoryInfo(CategoryInfo.Category.SONGS, true));
        defaultCategoryInfos.add(new CategoryInfo(CategoryInfo.Category.ALBUMS, true));
        defaultCategoryInfos.add(new CategoryInfo(CategoryInfo.Category.ARTISTS, true));
        defaultCategoryInfos.add(new CategoryInfo(CategoryInfo.Category.PLAYLISTS, true));
        defaultCategoryInfos.add(new CategoryInfo(CategoryInfo.Category.GENRES, false));
        return defaultCategoryInfos;
    }

    public final String getSAFSDCardUri() {
        return mPreferences.getString(SAF_SDCARD_URI, "");
    }

    public final void setSAFSDCardUri(Uri uri) {
        mPreferences.edit().putString(SAF_SDCARD_URI, uri.toString()).apply();
    }
}
