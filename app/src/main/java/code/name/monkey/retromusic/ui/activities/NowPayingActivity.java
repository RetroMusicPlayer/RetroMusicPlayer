package code.name.monkey.retromusic.ui.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity;
import code.name.monkey.retromusic.ui.fragments.NowPlayingScreen;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.adaptive.AdaptiveFragment;
import code.name.monkey.retromusic.ui.fragments.player.blur.BlurPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.card.CardFragment;
import code.name.monkey.retromusic.ui.fragments.player.cardblur.CardBlurFragment;
import code.name.monkey.retromusic.ui.fragments.player.color.ColorFragment;
import code.name.monkey.retromusic.ui.fragments.player.fit.FitFragment;
import code.name.monkey.retromusic.ui.fragments.player.flat.FlatPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.full.FullPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.hmm.HmmPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.material.MaterialFragment;
import code.name.monkey.retromusic.ui.fragments.player.normal.PlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.plain.PlainPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.simple.SimplePlayerFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;

public class NowPayingActivity extends AbsMusicServiceActivity implements AbsPlayerFragment.Callbacks, SharedPreferences.OnSharedPreferenceChangeListener {

    private NowPlayingScreen currentNowPlayingScreen;
    private AbsPlayerFragment playerFragment;

    void setupWindowTransition() {
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(
                AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in));
        getWindow().setEnterTransition(slide);
        //getWindow().setExitTransition(slide);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLightNavigationBar(true);
        setupWindowTransition();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playng);
        chooseFragmentForTheme();
    }

    private boolean isOneOfTheseThemes() {
        return currentNowPlayingScreen == NowPlayingScreen.FLAT
                || currentNowPlayingScreen == NowPlayingScreen.PLAIN
                || currentNowPlayingScreen == NowPlayingScreen.SIMPLE
                || currentNowPlayingScreen == NowPlayingScreen.NORMAL
                || currentNowPlayingScreen == NowPlayingScreen.ADAPTIVE
                || currentNowPlayingScreen == NowPlayingScreen.TINY
                || currentNowPlayingScreen == NowPlayingScreen.MATERIAL;
    }

    @Override
    public void onPaletteColorChanged() {
        int paletteColor = playerFragment.getPaletteColor();
        if ((currentNowPlayingScreen == NowPlayingScreen.FLAT || currentNowPlayingScreen == NowPlayingScreen.NORMAL) && PreferenceUtil.getInstance().getAdaptiveColor()) {
            setNavigationbarColor(Color.TRANSPARENT);
            setLightStatusbar(ColorUtil.isColorLight(paletteColor));
        } else if (currentNowPlayingScreen == NowPlayingScreen.COLOR) {
            setLightStatusbar(ColorUtil.isColorLight(paletteColor));
        } else if (currentNowPlayingScreen == NowPlayingScreen.BLUR || currentNowPlayingScreen == NowPlayingScreen.BLUR_CARD) {
            setLightStatusbar(false);
        } else {
            setLightStatusbar(isOneOfTheseThemes() && ColorUtil.isColorLight(ThemeStore.primaryColor(this)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentNowPlayingScreen != PreferenceUtil.getInstance().getNowPlayingScreen()) {
            postRecreate();
        }
        PreferenceUtil.getInstance().registerOnSharedPreferenceChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceUtil.getInstance().unregisterOnSharedPreferenceChangedListener(this);
    }

    @Override
    public void onQueueChanged() {
        super.onQueueChanged();
        if (MusicPlayerRemote.getPlayingQueue().isEmpty()) {
            finish();
        }
    }

    private void chooseFragmentForTheme() {
        currentNowPlayingScreen = PreferenceUtil.getInstance().getNowPlayingScreen();

        Fragment fragment; // must implement AbsPlayerFragment
        switch (currentNowPlayingScreen) {
            case MATERIAL:
                fragment = new MaterialFragment();
                break;
            case FIT:
                fragment = new FitFragment();
                break;
            case BLUR:
                fragment = new BlurPlayerFragment();
                break;
            case FLAT:
                fragment = new FlatPlayerFragment();
                break;
            case PLAIN:
                fragment = new PlainPlayerFragment();
                break;
            case FULL:
                fragment = new FullPlayerFragment();
                break;
            case COLOR:
                fragment = new ColorFragment();
                break;
            case CARD:
                fragment = new CardFragment();
                break;
            case SIMPLE:
                fragment = new SimplePlayerFragment();
                break;
            case TINY:
                fragment = new HmmPlayerFragment();
                break;
            case BLUR_CARD:
                fragment = new CardBlurFragment();
                break;
            case ADAPTIVE:
                fragment = new AdaptiveFragment();
                break;
            case NORMAL:
            default:
                fragment = new PlayerFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.player_fragment_container, fragment).commit();
        getSupportFragmentManager().executePendingTransactions();

        playerFragment = (AbsPlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.player_fragment_container);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferenceUtil.ALBUM_COVER_STYLE) || key.equals(PreferenceUtil.ALBUM_COVER_TRANSFORM)) {
            recreate();
        }
    }
}
