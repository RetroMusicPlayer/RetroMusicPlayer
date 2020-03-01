/*
 * Copyright (c) 2020 Hemanth Savarala.
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

package code.name.monkey.retromusic.activities;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

import com.afollestad.materialcab.MaterialCab;
import com.afollestad.materialcab.MaterialCab.Callback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity;
import code.name.monkey.retromusic.dialogs.CreatePlaylistDialog;
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment;
import code.name.monkey.retromusic.fragments.mainactivity.AlbumsFragment;
import code.name.monkey.retromusic.fragments.mainactivity.ArtistsFragment;
import code.name.monkey.retromusic.fragments.mainactivity.BannerHomeFragment;
import code.name.monkey.retromusic.fragments.mainactivity.FoldersFragment;
import code.name.monkey.retromusic.fragments.mainactivity.GenresFragment;
import code.name.monkey.retromusic.fragments.mainactivity.PlayingQueueFragment;
import code.name.monkey.retromusic.fragments.mainactivity.PlaylistsFragment;
import code.name.monkey.retromusic.fragments.mainactivity.SongsFragment;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.SearchQueryHelper;
import code.name.monkey.retromusic.helper.SortOrder.AlbumSortOrder;
import code.name.monkey.retromusic.helper.SortOrder.ArtistSortOrder;
import code.name.monkey.retromusic.helper.SortOrder.SongSortOrder;
import code.name.monkey.retromusic.interfaces.CabHolder;
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks;
import code.name.monkey.retromusic.loaders.AlbumLoader;
import code.name.monkey.retromusic.loaders.ArtistLoader;
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.util.AppRater;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroColorUtil;
import code.name.monkey.retromusic.util.RetroUtil;

/**
 * Created by hemanths on 2020-02-19.
 */
public class MainActivity extends AbsSlidingMusicPanelActivity
        implements CabHolder, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int APP_INTRO_REQUEST = 100;

    public static final String EXPAND_PANEL = "expand_panel";
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_SCREEN_OFF)) {
                if (PreferenceUtil.getInstance(context).getLockScreen() && MusicPlayerRemote.isPlaying()) {
                    final Intent activity = new Intent(context, LockScreenActivity.class);
                    activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    ActivityCompat.startActivity(context, activity, null);
                }
            }
        }
    };
    private final IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
    @Nullable
    MainActivityFragmentCallbacks currentFragment;
    private boolean blockRequestPermissions = false;
    private MaterialCab cab;
    private AppBarLayout mAppBarLayout;
    private MaterialTextView mAppTitle;
    private Toolbar mToolbar;
    private MaterialCardView mToolbarContainer;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        setDrawUnderStatusBar();
        super.onCreate(savedInstanceState);
        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setLightNavigationBar(true);
        setTaskDescriptionColorAuto();
        hideStatusBar();
        setBottomBarVisibility(View.VISIBLE);

        getBottomNavigationView().setSelectedItemId(PreferenceUtil.getInstance(this).getLastPage());
        getBottomNavigationView().setOnNavigationItemSelectedListener(item -> {
            PreferenceUtil.getInstance(MainActivity.this).setLastPage(item.getItemId());
            selectedFragment(item.getItemId());
            return true;
        });

        if (savedInstanceState == null) {
            selectedFragment(PreferenceUtil.getInstance(this).getLastPage());
        } else {
            restoreCurrentFragment();
        }

        mToolbarContainer = findViewById(R.id.toolbarContainer);
        mAppTitle = findViewById(R.id.appTitle);
        mToolbar = findViewById(R.id.toolbar);
        mAppBarLayout = findViewById(R.id.appBarLayout);

        checkShowChangelog();
        AppRater.appLaunched(this);
        setupToolbar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_INTRO_REQUEST) {
            blockRequestPermissions = false;
            if (!hasPermissions()) {
                requestPermissions();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mBroadcastReceiver, mIntentFilter);
        PreferenceUtil.getInstance(this).registerOnSharedPreferenceChangedListener(this);

        if (getIntent().hasExtra(EXPAND_PANEL) && PreferenceUtil.getInstance(this).isExpandPanel()) {
            if (getIntent().getBooleanExtra(EXPAND_PANEL, false)) {
                expandPanel();
                getIntent().putExtra(EXPAND_PANEL, false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        PreferenceUtil.getInstance(this).unregisterOnSharedPreferenceChangedListener(this);
    }

    public void addOnAppBarOffsetChangedListener(
            @NonNull AppBarLayout.OnOffsetChangedListener onOffsetChangedListener) {
        mAppBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);
    }

    public int getTotalAppBarScrollingRange() {
        return mAppBarLayout.getTotalScrollRange();
    }

    @Override
    public boolean handleBackPress() {
        if (cab != null && cab.isActive()) {
            cab.finish();
            return true;
        }
        return super.handleBackPress() || (currentFragment != null && currentFragment.handleBackPress());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (isPlaylistPage()) {
            menu.add(0, R.id.action_new_playlist, 0, R.string.new_playlist_title)
                    .setIcon(R.drawable.ic_playlist_add_white_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        if (isHomePage()) {
            menu.add(0, R.id.action_search, 0, getString(R.string.action_search))
                    .setIcon(R.drawable.ic_mic_white_24dp)
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        }
        if (isFolderPage()) {
            menu.add(0, R.id.action_scan, 0, R.string.scan_media)
                    .setIcon(R.drawable.ic_scanner_white_24dp)
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.add(0, R.id.action_go_to_start_directory, 0, R.string.action_go_to_start_directory)
                    .setIcon(R.drawable.ic_bookmark_music_white_24dp)
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof AbsLibraryPagerRecyclerViewCustomGridSizeFragment) {
            AbsLibraryPagerRecyclerViewCustomGridSizeFragment currentFragment
                    = (AbsLibraryPagerRecyclerViewCustomGridSizeFragment) fragment;
            if (currentFragment instanceof SongsFragment) {
                menu.removeItem(R.id.action_grid_size);
                menu.removeItem(R.id.action_layout_type);
            } else {
                MenuItem gridSizeItem = menu.findItem(R.id.action_grid_size);
                if (RetroUtil.isLandscape()) {
                    gridSizeItem.setTitle(R.string.action_grid_size_land);
                }
                setUpGridSizeMenu(currentFragment, gridSizeItem.getSubMenu());
                MenuItem layoutItem = menu.findItem(R.id.action_layout_type);
                setupLayoutMenu(currentFragment, layoutItem.getSubMenu());
            }
            setUpSortOrderMenu(currentFragment, menu.findItem(R.id.action_sort_order).getSubMenu());
        } else {
            menu.removeItem(R.id.action_layout_type);
            menu.removeItem(R.id.action_grid_size);
            menu.removeItem(R.id.action_sort_order);
        }
        menu.add(0, R.id.action_settings, 0, getString(R.string.action_settings))
                .setIcon(R.drawable.ic_settings_white_24dp)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof AbsLibraryPagerRecyclerViewCustomGridSizeFragment) {
            AbsLibraryPagerRecyclerViewCustomGridSizeFragment currentFragment
                    = (AbsLibraryPagerRecyclerViewCustomGridSizeFragment) fragment;
            if (handleGridSizeMenuItem(currentFragment, item)) {
                return true;
            }
            if (handleLayoutResType(currentFragment, item)) {
                return true;
            }
            if (handleSortOrderMenuItem(currentFragment, item)) {
                return true;
            }
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, mToolbarContainer,
                        getString(R.string.transition_toolbar));
                NavigationUtil.goToSearch(this, true, options);
                break;
            case R.id.action_new_playlist:
                CreatePlaylistDialog.create().show(getSupportFragmentManager(), "CREATE_PLAYLIST");
                return true;
            case R.id.action_settings:
                NavigationUtil.goToSettings(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(this, mToolbar);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        handlePlaybackIntent(getIntent());
    }

    @Override
    public void onSharedPreferenceChanged(final @NonNull SharedPreferences sharedPreferences,
                                          final @NonNull String key) {
        if (key.equals(PreferenceUtil.GENERAL_THEME) || key.equals(PreferenceUtil.BLACK_THEME) ||
                key.equals(PreferenceUtil.ADAPTIVE_COLOR_APP) || key.equals(PreferenceUtil.DOMINANT_COLOR) ||
                key.equals(PreferenceUtil.USER_NAME) || key.equals(PreferenceUtil.TOGGLE_FULL_SCREEN) ||
                key.equals(PreferenceUtil.TOGGLE_VOLUME) || key.equals(PreferenceUtil.ROUND_CORNERS) ||
                key.equals(PreferenceUtil.CAROUSEL_EFFECT) || key.equals(PreferenceUtil.NOW_PLAYING_SCREEN_ID) ||
                key.equals(PreferenceUtil.TOGGLE_GENRE) || key.equals(PreferenceUtil.BANNER_IMAGE_PATH) ||
                key.equals(PreferenceUtil.PROFILE_IMAGE_PATH) || key.equals(PreferenceUtil.CIRCULAR_ALBUM_ART) ||
                key.equals(PreferenceUtil.KEEP_SCREEN_ON) || key.equals(PreferenceUtil.TOGGLE_SEPARATE_LINE) ||
                key.equals(PreferenceUtil.TOGGLE_HOME_BANNER) || key.equals(PreferenceUtil.TOGGLE_ADD_CONTROLS) ||
                key.equals(PreferenceUtil.ALBUM_COVER_STYLE) || key.equals(PreferenceUtil.HOME_ARTIST_GRID_STYLE) ||
                key.equals(PreferenceUtil.ALBUM_COVER_TRANSFORM) || key.equals(PreferenceUtil.DESATURATED_COLOR) ||
                key.equals(PreferenceUtil.TAB_TEXT_MODE) || key.equals(PreferenceUtil.LIBRARY_CATEGORIES)
        ) {
            postRecreate();
        }
    }

    @NotNull
    @Override
    public MaterialCab openCab(final int menuRes, @NotNull final Callback callback) {
        if (cab != null && cab.isActive()) {
            cab.finish();
        }
        cab = new MaterialCab(this, R.id.cab_stub)
                .setMenu(menuRes)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(
                        RetroColorUtil.shiftBackgroundColorForLightText(
                                ATHUtil.INSTANCE.resolveColor(this, R.attr.colorSurface)))
                .start(callback);
        return cab;
    }

    public void removeOnAppBarOffsetChangedListener(
            @NonNull AppBarLayout.OnOffsetChangedListener onOffsetChangedListener) {
        mAppBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener);
    }

    public void setCurrentFragment(@NonNull Fragment fragment, @NonNull String tag) {
        String currentTag = null;
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            currentTag = getSupportFragmentManager().findFragmentByTag(tag).getTag();
        }

        if (!tag.equals(currentTag)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .commit();
            currentFragment = (MainActivityFragmentCallbacks) fragment;
        }
    }

    @NotNull
    @Override
    protected View createContentView() {
        return wrapSlidingMusicPanel(R.layout.activity_main_content);
    }

    @Override
    protected void requestPermissions() {
        if (!blockRequestPermissions) {
            super.requestPermissions();
        }
    }

    private void checkShowChangelog() {
        try {
            final PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            final int currentVersion = pInfo.versionCode;
            if (currentVersion != PreferenceUtil.getInstance(this).getLastChangelogVersion()) {
                startActivityForResult(new Intent(this, WhatsNewActivity.class), APP_INTRO_REQUEST);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private boolean handleGridSizeMenuItem(
            @NonNull AbsLibraryPagerRecyclerViewCustomGridSizeFragment fragment,
            @NonNull MenuItem item) {
        int gridSize = 0;
        switch (item.getItemId()) {
            case R.id.action_grid_size_1:
                gridSize = 1;
                break;
            case R.id.action_grid_size_2:
                gridSize = 2;
                break;
            case R.id.action_grid_size_3:
                gridSize = 3;
                break;
            case R.id.action_grid_size_4:
                gridSize = 4;
                break;
            case R.id.action_grid_size_5:
                gridSize = 5;
                break;
            case R.id.action_grid_size_6:
                gridSize = 6;
                break;
            case R.id.action_grid_size_7:
                gridSize = 7;
                break;
            case R.id.action_grid_size_8:
                gridSize = 8;
                break;
        }

        if (gridSize > 0) {
            item.setChecked(true);
            fragment.setAndSaveGridSize(gridSize);
            return true;
        }
        return false;
    }

    private boolean handleLayoutResType(
            final AbsLibraryPagerRecyclerViewCustomGridSizeFragment fragment,
            final MenuItem item) {
        int layoutRes = -1;
        switch (item.getItemId()) {
            case R.id.action_layout_normal:
                layoutRes = R.layout.item_grid;
                break;
            case R.id.action_layout_card:
                layoutRes = R.layout.item_card;
                break;
            case R.id.action_layout_colored_card:
                layoutRes = R.layout.item_card_color;
                break;
            case R.id.action_layout_circular:
                layoutRes = R.layout.item_grid_circle;
                break;
            case R.id.action_layout_image:
                layoutRes = R.layout.image;
                break;
            case R.id.action_layout_gradient_image:
                layoutRes = R.layout.item_image_gradient;
                break;
        }
        if (layoutRes != -1) {
            item.setChecked(true);
            fragment.setAndSaveLayoutRes(layoutRes);
            return true;
        }
        return false;
    }

    private void handlePlaybackIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        Uri uri = intent.getData();
        String mimeType = intent.getType();
        boolean handled = false;

        if (intent.getAction() != null && intent.getAction()
                .equals(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)) {
            final List<Song> songs = SearchQueryHelper.getSongs(this, intent.getExtras());
            if (MusicPlayerRemote.getShuffleMode() == MusicService.SHUFFLE_MODE_SHUFFLE) {
                MusicPlayerRemote.openAndShuffleQueue(songs, true);
            } else {
                MusicPlayerRemote.openQueue(songs, 0, true);
            }
            handled = true;
        }

        if (uri != null && uri.toString().length() > 0) {
            MusicPlayerRemote.playFromUri(uri);
            handled = true;
        } else if (MediaStore.Audio.Playlists.CONTENT_TYPE.equals(mimeType)) {
            final int id = (int) parseIdFromIntent(intent, "playlistId", "playlist");
            if (id >= 0) {
                int position = intent.getIntExtra("position", 0);
                List<Song> songs = new ArrayList<>(PlaylistSongsLoader.getPlaylistSongList(this, id));
                MusicPlayerRemote.openQueue(songs, position, true);
                handled = true;
            }
        } else if (MediaStore.Audio.Albums.CONTENT_TYPE.equals(mimeType)) {
            final int id = (int) parseIdFromIntent(intent, "albumId", "album");
            if (id >= 0) {
                int position = intent.getIntExtra("position", 0);
                MusicPlayerRemote.openQueue(AlbumLoader.getAlbum(this, id).getSongs(), position, true);
                handled = true;
            }
        } else if (MediaStore.Audio.Artists.CONTENT_TYPE.equals(mimeType)) {
            final int id = (int) parseIdFromIntent(intent, "artistId", "artist");
            if (id >= 0) {
                int position = intent.getIntExtra("position", 0);
                MusicPlayerRemote.openQueue(ArtistLoader.getArtist(this, id).getSongs(), position, true);
                handled = true;
            }
        }
        if (handled) {
            setIntent(new Intent());
        }
    }

    private boolean handleSortOrderMenuItem(
            @NonNull AbsLibraryPagerRecyclerViewCustomGridSizeFragment
                    fragment, @NonNull MenuItem item) {
        String sortOrder = null;
        if (fragment instanceof AlbumsFragment) {
            switch (item.getItemId()) {
                case R.id.action_album_sort_order_asc:
                    sortOrder = AlbumSortOrder.ALBUM_A_Z;
                    break;
                case R.id.action_album_sort_order_desc:
                    sortOrder = AlbumSortOrder.ALBUM_Z_A;
                    break;
                case R.id.action_album_sort_order_artist:
                    sortOrder = AlbumSortOrder.ALBUM_ARTIST;
                    break;
                case R.id.action_album_sort_order_year:
                    sortOrder = AlbumSortOrder.ALBUM_YEAR;
                    break;
            }
        } else if (fragment instanceof ArtistsFragment) {
            switch (item.getItemId()) {
                case R.id.action_artist_sort_order_asc:
                    sortOrder = ArtistSortOrder.ARTIST_A_Z;
                    break;
                case R.id.action_artist_sort_order_desc:
                    sortOrder = ArtistSortOrder.ARTIST_Z_A;
                    break;
            }
        } else if (fragment instanceof SongsFragment) {
            switch (item.getItemId()) {
                case R.id.action_song_sort_order_asc:
                    sortOrder = SongSortOrder.SONG_A_Z;
                    break;
                case R.id.action_song_sort_order_desc:
                    sortOrder = SongSortOrder.SONG_Z_A;
                    break;
                case R.id.action_song_sort_order_artist:
                    sortOrder = SongSortOrder.SONG_ARTIST;
                    break;
                case R.id.action_song_sort_order_album:
                    sortOrder = SongSortOrder.SONG_ALBUM;
                    break;
                case R.id.action_song_sort_order_year:
                    sortOrder = SongSortOrder.SONG_YEAR;
                    break;
                case R.id.action_song_sort_order_date:
                    sortOrder = SongSortOrder.SONG_DATE;
                    break;
                case R.id.action_song_sort_order_composer:
                    sortOrder = SongSortOrder.COMPOSER;
                    break;
                case R.id.action_song_sort_order_date_modified:
                    sortOrder = SongSortOrder.SONG_DATE_MODIFIED;
                    break;
            }
        }

        if (sortOrder != null) {
            item.setChecked(true);
            fragment.setAndSaveSortOrder(sortOrder);
            return true;
        }

        return false;
    }

    private boolean isFolderPage() {
        return getSupportFragmentManager().findFragmentByTag(FoldersFragment.TAG) instanceof FoldersFragment;
    }

    private boolean isHomePage() {
        return getSupportFragmentManager().findFragmentByTag(BannerHomeFragment.TAG) instanceof BannerHomeFragment;
    }

    private boolean isPlaylistPage() {
        return getSupportFragmentManager().findFragmentByTag(PlaylistsFragment.TAG) instanceof PlaylistsFragment;
    }

    private long parseIdFromIntent(@NonNull Intent intent, String longKey,
                                   String stringKey) {
        long id = intent.getLongExtra(longKey, -1);
        if (id < 0) {
            String idString = intent.getStringExtra(stringKey);
            if (idString != null) {
                try {
                    id = Long.parseLong(idString);
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return id;
    }

    private void restoreCurrentFragment() {
        currentFragment = (MainActivityFragmentCallbacks) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
    }

    private void selectedFragment(final int itemId) {
        switch (itemId) {
            case R.id.action_album:
                setCurrentFragment(AlbumsFragment.newInstance(), AlbumsFragment.TAG);
                break;
            case R.id.action_artist:
                setCurrentFragment(ArtistsFragment.newInstance(), ArtistsFragment.TAG);
                break;
            case R.id.action_playlist:
                setCurrentFragment(PlaylistsFragment.newInstance(), PlaylistsFragment.TAG);
                break;
            case R.id.action_genre:
                setCurrentFragment(GenresFragment.newInstance(), GenresFragment.TAG);
                break;
            case R.id.action_playing_queue:
                setCurrentFragment(PlayingQueueFragment.newInstance(), PlayingQueueFragment.TAG);
                break;
            case R.id.action_song:
                setCurrentFragment(SongsFragment.newInstance(), SongsFragment.TAG);
                break;
            case R.id.action_folder:
                setCurrentFragment(FoldersFragment.newInstance(this), FoldersFragment.TAG);
                break;
            default:
            case R.id.action_home:
                setCurrentFragment(BannerHomeFragment.newInstance(), BannerHomeFragment.TAG);
                break;
        }
    }


    private void setUpGridSizeMenu(@NonNull AbsLibraryPagerRecyclerViewCustomGridSizeFragment fragment,
                                   @NonNull SubMenu gridSizeMenu) {

        switch (fragment.getGridSize()) {
            case 1:
                gridSizeMenu.findItem(R.id.action_grid_size_1).setChecked(true);
                break;
            case 2:
                gridSizeMenu.findItem(R.id.action_grid_size_2).setChecked(true);
                break;
            case 3:
                gridSizeMenu.findItem(R.id.action_grid_size_3).setChecked(true);
                break;
            case 4:
                gridSizeMenu.findItem(R.id.action_grid_size_4).setChecked(true);
                break;
            case 5:
                gridSizeMenu.findItem(R.id.action_grid_size_5).setChecked(true);
                break;
            case 6:
                gridSizeMenu.findItem(R.id.action_grid_size_6).setChecked(true);
                break;
            case 7:
                gridSizeMenu.findItem(R.id.action_grid_size_7).setChecked(true);
                break;
            case 8:
                gridSizeMenu.findItem(R.id.action_grid_size_8).setChecked(true);
                break;
        }
        int maxGridSize = fragment.getMaxGridSize();
        if (maxGridSize < 8) {
            gridSizeMenu.findItem(R.id.action_grid_size_8).setVisible(false);
        }
        if (maxGridSize < 7) {
            gridSizeMenu.findItem(R.id.action_grid_size_7).setVisible(false);
        }
        if (maxGridSize < 6) {
            gridSizeMenu.findItem(R.id.action_grid_size_6).setVisible(false);
        }
        if (maxGridSize < 5) {
            gridSizeMenu.findItem(R.id.action_grid_size_5).setVisible(false);
        }
        if (maxGridSize < 4) {
            gridSizeMenu.findItem(R.id.action_grid_size_4).setVisible(false);
        }
        if (maxGridSize < 3) {
            gridSizeMenu.findItem(R.id.action_grid_size_3).setVisible(false);
        }
    }

    private void setUpSortOrderMenu(
            @NonNull AbsLibraryPagerRecyclerViewCustomGridSizeFragment fragment,
            @NonNull SubMenu sortOrderMenu) {
        String currentSortOrder = fragment.getSortOrder();
        sortOrderMenu.clear();

        if (fragment instanceof AlbumsFragment) {
            sortOrderMenu.add(0, R.id.action_album_sort_order_asc, 0, R.string.sort_order_a_z)
                    .setChecked(currentSortOrder.equals(AlbumSortOrder.ALBUM_A_Z));
            sortOrderMenu.add(0, R.id.action_album_sort_order_desc, 1, R.string.sort_order_z_a)
                    .setChecked(currentSortOrder.equals(AlbumSortOrder.ALBUM_Z_A));
            sortOrderMenu.add(0, R.id.action_album_sort_order_artist, 2, R.string.sort_order_artist)
                    .setChecked(currentSortOrder.equals(AlbumSortOrder.ALBUM_ARTIST));
            sortOrderMenu.add(0, R.id.action_album_sort_order_year, 3, R.string.sort_order_year)
                    .setChecked(currentSortOrder.equals(AlbumSortOrder.ALBUM_YEAR));
        } else if (fragment instanceof ArtistsFragment) {
            sortOrderMenu.add(0, R.id.action_artist_sort_order_asc, 0, R.string.sort_order_a_z)
                    .setChecked(currentSortOrder.equals(ArtistSortOrder.ARTIST_A_Z));
            sortOrderMenu.add(0, R.id.action_artist_sort_order_desc, 1, R.string.sort_order_z_a)
                    .setChecked(currentSortOrder.equals(ArtistSortOrder.ARTIST_Z_A));
        } else if (fragment instanceof SongsFragment) {
            sortOrderMenu.add(0, R.id.action_song_sort_order_asc, 0, R.string.sort_order_a_z)
                    .setChecked(currentSortOrder.equals(SongSortOrder.SONG_A_Z));
            sortOrderMenu.add(0, R.id.action_song_sort_order_desc, 1, R.string.sort_order_z_a)
                    .setChecked(currentSortOrder.equals(SongSortOrder.SONG_Z_A));
            sortOrderMenu.add(0, R.id.action_song_sort_order_artist, 2, R.string.sort_order_artist)
                    .setChecked(currentSortOrder.equals(SongSortOrder.SONG_ARTIST));
            sortOrderMenu.add(0, R.id.action_song_sort_order_album, 3, R.string.sort_order_album)
                    .setChecked(currentSortOrder.equals(SongSortOrder.SONG_ALBUM));
            sortOrderMenu.add(0, R.id.action_song_sort_order_year, 4, R.string.sort_order_year)
                    .setChecked(currentSortOrder.equals(SongSortOrder.SONG_YEAR));
            sortOrderMenu.add(0, R.id.action_song_sort_order_date, 5, R.string.sort_order_date)
                    .setChecked(currentSortOrder.equals(SongSortOrder.SONG_DATE));
            sortOrderMenu.add(0, R.id.action_song_sort_order_date_modified, 6, R.string.sort_order_date_modified)
                    .setChecked(currentSortOrder.equals(SongSortOrder.SONG_DATE_MODIFIED));
            sortOrderMenu.add(0, R.id.action_song_sort_order_composer, 7, R.string.sort_order_composer)
                    .setChecked(currentSortOrder.equals(SongSortOrder.COMPOSER));
        }
        sortOrderMenu.setGroupCheckable(0, true, true);
    }

    private void setupLayoutMenu(
            @NonNull final AbsLibraryPagerRecyclerViewCustomGridSizeFragment fragment,
            @NonNull final SubMenu subMenu) {
        switch (fragment.itemLayoutRes()) {
            case R.layout.item_card:
                subMenu.findItem(R.id.action_layout_card).setChecked(true);
                break;
            case R.layout.item_grid:
                subMenu.findItem(R.id.action_layout_normal).setChecked(true);
                break;
            case R.layout.item_card_color:
                subMenu.findItem(R.id.action_layout_colored_card).setChecked(true);
                break;
            case R.layout.item_grid_circle:
                subMenu.findItem(R.id.action_layout_circular).setChecked(true);
                break;
            case R.layout.image:
                subMenu.findItem(R.id.action_layout_image).setChecked(true);
                break;
            case R.layout.item_image_gradient:
                subMenu.findItem(R.id.action_layout_gradient_image).setChecked(true);
                break;
        }
    }

    private void setupToolbar() {
        setTitle(null);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        mToolbarContainer.setCardBackgroundColor(
                ColorStateList.valueOf(ATHUtil.INSTANCE.resolveColor(this, R.attr.colorSurface)));
        setSupportActionBar(mToolbar);
        mToolbar.setOnClickListener(v -> {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, mToolbarContainer, getString(R.string.transition_toolbar));
            NavigationUtil.goToSearch(this, options);
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(mToolbar);
                Log.i(TAG, "run: " + Thread.currentThread().getName());
                mAppTitle.setVisibility(View.GONE);
                setTitle(R.string.action_search);
            }
        }, 3000);
    }
}
