package code.name.monkey.retromusic.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity;
import code.name.monkey.retromusic.ui.fragments.player.lockscreen.LockScreenPlayerControlsFragment;

public class LockScreenActivity extends AbsMusicServiceActivity {
    private LockScreenPlayerControlsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setDrawUnderStatusBar(true);
        setContentView(R.layout.activity_lock_screen_old_style);

        hideStatusBar();
        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
        setLightNavigationBar(true);

        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.BOTTOM)
                .build();

        Slidr.attach(this, config);

        ButterKnife.bind(this);
        mFragment = (LockScreenPlayerControlsFragment) getSupportFragmentManager().findFragmentById(R.id.playback_controls_fragment);

        findViewById(R.id.slide).setTranslationY(100f);
        findViewById(R.id.slide).setAlpha(0f);
        ViewCompat.animate(findViewById(R.id.slide))
                .translationY(0f)
                .alpha(1f)
                .setDuration(1500)
                .start();

        findViewById(R.id.root_layout).setBackgroundColor(ThemeStore.primaryColor(this));
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        updateSongs();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        updateSongs();
    }

    private void updateSongs() {
        Song song = MusicPlayerRemote.getCurrentSong();
        SongGlideRequest.Builder.from(Glide.with(this), song)
                .checkIgnoreMediaStore(this)
                .generatePalette(this)
                .build().into(new RetroMusicColoredTarget(findViewById(R.id.image)) {
            @Override
            public void onColorReady(int color) {
                mFragment.setDark(color);
            }
        });
    }
}
