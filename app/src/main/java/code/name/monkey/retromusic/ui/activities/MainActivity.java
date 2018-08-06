package code.name.monkey.retromusic.ui.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.dialogs.ChangelogDialog;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.SearchQueryHelper;
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks;
import code.name.monkey.retromusic.loaders.AlbumLoader;
import code.name.monkey.retromusic.loaders.ArtistLoader;
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity;
import code.name.monkey.retromusic.ui.fragments.mainactivity.LibraryFragment;
import code.name.monkey.retromusic.ui.fragments.mainactivity.home.BannerHomeFragment;
import code.name.monkey.retromusic.ui.fragments.mainactivity.home.HomeFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AbsSlidingMusicPanelActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    public static final int APP_INTRO_REQUEST = 2323;
    private static final String TAG = "MainActivity";
    private static final int APP_USER_INFO_REQUEST = 9003;
    private static final int REQUEST_CODE_THEME = 9002;

    @Nullable
    MainActivityFragmentCallbacks currentFragment;

    @BindView(R.id.parent_container)
    FrameLayout drawerLayout;

    private boolean blockRequestPermissions;
    private CompositeDisposable disposable = new CompositeDisposable();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_SCREEN_OFF)) {
                if (PreferenceUtil.getInstance(context).getLockScreen() && MusicPlayerRemote.isPlaying()) {
                    Intent activity = new Intent(context, LockScreenActivity.class);
                    activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    ActivityCompat.startActivity(context, activity, null);
                }
            }
        }
    };


    @Override
    protected View createContentView() {
        @SuppressLint("InflateParams")
        View contentView = getLayoutInflater().inflate(R.layout.activity_main_drawer_layout, null);
        ViewGroup drawerContent = contentView.findViewById(R.id.drawer_content_container);
        drawerContent.addView(wrapSlidingMusicPanel(R.layout.activity_main_content));
        return contentView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDrawUnderStatusBar(true);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setBottomBarVisibility(View.VISIBLE);

        drawerLayout.setOnApplyWindowInsetsListener((view, windowInsets) ->
                windowInsets.replaceSystemWindowInsets(0, 0, 0, 0));

        if (savedInstanceState == null) {
            setCurrentFragment(PreferenceUtil.getInstance(this).getLastPage());
        } else {
            restoreCurrentFragment();
        }
        getBottomNavigationView().setOnNavigationItemSelectedListener(this);
        checkShowChangelog();
    }

    private void checkShowChangelog() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int currentVersion = pInfo.versionCode;
            if (currentVersion != PreferenceUtil.getInstance(this).getLastChangelogVersion()) {
                ChangelogDialog.create().show(getSupportFragmentManager(), "CHANGE_LOG_DIALOG");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter screenOnOff = new IntentFilter();
        screenOnOff.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(broadcastReceiver, screenOnOff);

        PreferenceUtil.getInstance(this).registerOnSharedPreferenceChangedListener(this);

        if (getIntent().hasExtra("expand")) {
            if (getIntent().getBooleanExtra("expand", false)) {
                expandPanel();
                getIntent().putExtra("expand", false);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
        if (broadcastReceiver == null) {
            return;
        }
        unregisterReceiver(broadcastReceiver);
        PreferenceUtil.getInstance(this).unregisterOnSharedPreferenceChangedListener(this);
    }

    public void setCurrentFragment(@Nullable Fragment fragment, boolean isStackAdd) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, TAG);
        if (isStackAdd) {
            fragmentTransaction.addToBackStack(TAG);
        }
        fragmentTransaction.commit();
        currentFragment = (MainActivityFragmentCallbacks) fragment;
    }

    private void restoreCurrentFragment() {
        currentFragment = (MainActivityFragmentCallbacks) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        PreferenceUtil.getInstance(this).setLastPage(item.getItemId());
        disposable.add(Observable.just(item.getItemId())
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(this::setCurrentFragment));
        return true;
    }

    private void setCurrentFragment(int menuItem) {
        switch (menuItem) {
            case R.id.action_song:
            case R.id.action_album:
            case R.id.action_artist:
            case R.id.action_playlist:
                setCurrentFragment(LibraryFragment.newInstance(menuItem), false);
                break;
            default:
            case R.id.action_home:
                setCurrentFragment(PreferenceUtil.getInstance(this).toggleHomeBanner() ? BannerHomeFragment
                                .newInstance() : HomeFragment.newInstance(),
                        false);
                break;
        }
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
            final ArrayList<Song> songs = SearchQueryHelper.getSongs(this, intent.getExtras());

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
                ArrayList<Song> songs = new ArrayList<>(
                        PlaylistSongsLoader.getPlaylistSongList(this, id).blockingFirst());
                MusicPlayerRemote.openQueue(songs, position, true);
                handled = true;
            }
        } else if (MediaStore.Audio.Albums.CONTENT_TYPE.equals(mimeType)) {
            final int id = (int) parseIdFromIntent(intent, "albumId", "album");
            if (id >= 0) {
                int position = intent.getIntExtra("position", 0);
                MusicPlayerRemote
                        .openQueue(AlbumLoader.getAlbum(this, id).blockingFirst().songs, position, true);
                handled = true;
            }
        } else if (MediaStore.Audio.Artists.CONTENT_TYPE.equals(mimeType)) {
            final int id = (int) parseIdFromIntent(intent, "artistId", "artist");
            if (id >= 0) {
                int position = intent.getIntExtra("position", 0);
                MusicPlayerRemote
                        .openQueue(ArtistLoader.getArtist(this, id).blockingFirst().getSongs(), position, true);
                handled = true;
            }
        }
        if (handled) {
            setIntent(new Intent());
        }
    }

    private long parseIdFromIntent(@NonNull Intent intent, String longKey, String stringKey) {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case APP_INTRO_REQUEST:
                blockRequestPermissions = false;
                if (!hasPermissions()) {
                    requestPermissions();
                }

                break;
            case REQUEST_CODE_THEME:
            case APP_USER_INFO_REQUEST:
                postRecreate();
                break;
        }

    }

    @Override
    public boolean handleBackPress() {
        return super.handleBackPress() || (currentFragment != null &&
                currentFragment.handleBackPress());
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        handlePlaybackIntent(getIntent());
    }

    @Override
    protected void requestPermissions() {
        if (!blockRequestPermissions) {
            super.requestPermissions();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferenceUtil.GENERAL_THEME) ||
                key.equals(PreferenceUtil.ADAPTIVE_COLOR_APP) ||
                key.equals(PreferenceUtil.DOMINANT_COLOR) ||
                key.equals(PreferenceUtil.USER_NAME) ||
                key.equals(PreferenceUtil.TOGGLE_FULL_SCREEN) ||
                key.equals(PreferenceUtil.TOGGLE_VOLUME) ||
                key.equals(PreferenceUtil.TOGGLE_TAB_TITLES) ||
                key.equals(PreferenceUtil.ROUND_CORNERS) ||
                key.equals(PreferenceUtil.CAROUSEL_EFFECT) ||
                key.equals(PreferenceUtil.NOW_PLAYING_SCREEN_ID) ||
                key.equals(PreferenceUtil.TOGGLE_GENRE) ||
                key.equals(PreferenceUtil.BANNER_IMAGE_PATH) ||
                key.equals(PreferenceUtil.PROFILE_IMAGE_PATH) ||
                key.equals(PreferenceUtil.CIRCULAR_ALBUM_ART) ||
                key.equals(PreferenceUtil.KEEP_SCREEN_ON) ||
                key.equals(PreferenceUtil.TOGGLE_SEPARATE_LINE) ||
                key.equals(PreferenceUtil.ALBUM_GRID_STYLE) ||
                key.equals(PreferenceUtil.ARTIST_GRID_STYLE) ||
                key.equals(PreferenceUtil.TOGGLE_HOME_BANNER)) postRecreate();
    }

    private void showPromotionalOffer() {
        new MaterialDialog.Builder(this)
                .positiveText("Buy")
                .onPositive((dialog, which) ->
                        startActivity(new Intent(MainActivity.this, ProVersionActivity.class)))
                .negativeText(android.R.string.cancel)
                .customView(R.layout.dialog_promotional_offer, false)
                .dismissListener(dialog -> {
                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                            .edit()
                            .putBoolean("shown", true)
                            .apply();
                })
                .show();
    }
}
