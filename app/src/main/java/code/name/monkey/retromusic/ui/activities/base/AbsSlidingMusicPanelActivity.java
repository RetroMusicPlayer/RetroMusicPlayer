package code.name.monkey.retromusic.ui.activities.base;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.PathInterpolator;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.ui.fragments.MiniPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.NowPlayingScreen;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.adaptive.AdaptiveFragment;
import code.name.monkey.retromusic.ui.fragments.player.blur.BlurPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.card.CardFragment;
import code.name.monkey.retromusic.ui.fragments.player.cardblur.CardBlurFragment;
import code.name.monkey.retromusic.ui.fragments.player.color.ColorFragment;
import code.name.monkey.retromusic.ui.fragments.player.flat.FlatPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.full.FullPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.hmm.HmmPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.material.MaterialFragment;
import code.name.monkey.retromusic.ui.fragments.player.normal.PlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.plain.PlainPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.simple.SimplePlayerFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.ViewUtil;
import code.name.monkey.retromusic.views.BottomNavigationViewEx;

public abstract class AbsSlidingMusicPanelActivity extends AbsMusicServiceActivity implements
        SlidingUpPanelLayout.PanelSlideListener,
        PlayerFragment.Callbacks {

    public static final String TAG = AbsSlidingMusicPanelActivity.class.getSimpleName();

    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx bottomNavigationView;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    private int navigationbarColor;
    private int taskColor;
    private boolean lightStatusBar;
    private boolean lightNavigationBar;
    private NowPlayingScreen currentNowPlayingScreen;
    private AbsPlayerFragment playerFragment;
    private MiniPlayerFragment miniPlayerFragment;
    private ValueAnimator navigationBarColorAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createContentView());
        ButterKnife.bind(this);
        choosFragmentForTheme();
        //noinspection ConstantConditions
        miniPlayerFragment.getView().setOnClickListener(v -> expandPanel());
        slidingUpPanelLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        slidingUpPanelLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        if (getPanelState() == PanelState.EXPANDED) {
                            onPanelSlide(slidingUpPanelLayout, 1);
                            onPanelExpanded(slidingUpPanelLayout);
                        } else if (getPanelState() == PanelState.COLLAPSED) {
                            onPanelCollapsed(slidingUpPanelLayout);
                        } else {
                            playerFragment.onHide();
                        }
                    }
                });

        setupBottomView();
        slidingUpPanelLayout.addPanelSlideListener(this);

    }

    private void choosFragmentForTheme() {
        currentNowPlayingScreen = PreferenceUtil.getInstance(this).getNowPlayingScreen();

        Fragment fragment; // must implement AbsPlayerFragment
        switch (currentNowPlayingScreen) {
            case MATERIAL:
                fragment = new MaterialFragment();
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
        getSupportFragmentManager().beginTransaction().replace(R.id.player_fragment_container, fragment)
                .commit();
        getSupportFragmentManager().executePendingTransactions();

        playerFragment = (AbsPlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.player_fragment_container);
        miniPlayerFragment = (MiniPlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mini_player_fragment);
    }

    private void setupBottomView() {
        bottomNavigationView.setSelectedItemId(PreferenceUtil.getInstance(this).getLastPage());
        bottomNavigationView.setBackgroundColor(ThemeStore.primaryColor(this));
        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.setTextSize(10f);
        bottomNavigationView.setTextVisibility(PreferenceUtil.getInstance(this).tabTitles());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentNowPlayingScreen != PreferenceUtil.getInstance(this).getNowPlayingScreen()) {
            postRecreate();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (navigationBarColorAnimator != null) {
            navigationBarColorAnimator.cancel(); // just in case
        }
    }


    protected abstract View createContentView();

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        if (!MusicPlayerRemote.getPlayingQueue().isEmpty()) {
            slidingUpPanelLayout.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            slidingUpPanelLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            hideBottomBar(false);
                        }
                    });
        }// don't call hideBottomBar(true) here as it causes a bug with the SlidingUpPanelLayout
    }

    @Override
    public void onQueueChanged() {
        super.onQueueChanged();
        hideBottomBar(MusicPlayerRemote.getPlayingQueue().isEmpty());
    }

    @Override
    public void onPanelSlide(View panel, @FloatRange(from = 0, to = 1) float slideOffset) {
        bottomNavigationView.setTranslationY(slideOffset * 400);
        setMiniPlayerAlphaProgress(slideOffset);
        //findViewById(R.id.player_fragment_container).setAlpha(slideOffset);
    }

    @Override
    public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
        switch (newState) {
            case COLLAPSED:
                onPanelCollapsed(panel);
                break;
            case EXPANDED:
                onPanelExpanded(panel);
                break;
            case ANCHORED:
                collapsePanel(); // this fixes a bug where the panel would get stuck for some reason
                break;
        }
    }

    public void onPanelCollapsed(View panel) {
        // restore values
        super.setLightStatusbar(lightStatusBar);
        super.setTaskDescriptionColor(taskColor);
        super.setNavigationbarColor(navigationbarColor);
        super.setLightNavigationBar(lightNavigationBar);

        playerFragment.setMenuVisibility(false);
        playerFragment.setUserVisibleHint(false);
        playerFragment.onHide();
    }

    public void onPanelExpanded(View panel) {
        // setting fragments values
        int playerFragmentColor = playerFragment.getPaletteColor();
        super.setTaskDescriptionColor(playerFragmentColor);

        if (currentNowPlayingScreen == NowPlayingScreen.COLOR) {
            super.setNavigationbarColor(playerFragmentColor);
        } else {
            super.setNavigationbarColor(ThemeStore.primaryColor(this));
        }

        setLightStatusBar();

        playerFragment.setMenuVisibility(true);
        playerFragment.setUserVisibleHint(true);
        playerFragment.onShow();
    }

    private void setLightStatusBar() {
        super.setLightStatusbar(!PreferenceUtil.getInstance(this).getAdaptiveColor() &&
                ColorUtil.isColorLight(ThemeStore.primaryColor(this)) && (
                currentNowPlayingScreen == NowPlayingScreen.FLAT
                        || currentNowPlayingScreen == NowPlayingScreen.PLAIN
                        || currentNowPlayingScreen == NowPlayingScreen.SIMPLE
                        || currentNowPlayingScreen == NowPlayingScreen.NORMAL
                        || currentNowPlayingScreen == NowPlayingScreen.ADAPTIVE)
                || currentNowPlayingScreen == NowPlayingScreen.TINY);
    }

    @Override
    public void setLightStatusbar(boolean enabled) {
        lightStatusBar = enabled;
        if (getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            super.setLightStatusbar(enabled);
        }
    }

    @Override
    public void setLightNavigationBar(boolean enabled) {
        lightNavigationBar = enabled;
        /*if (getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            super.setLightNavigationBar(enabled);
        }*/
    }

    @Override
    public void setTaskDescriptionColor(@ColorInt int color) {
        taskColor = color;
        if (getPanelState() == null || getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            super.setTaskDescriptionColor(color);
        }
    }

    @Override
    public void setNavigationbarColor(int color) {
        navigationbarColor = color;
        if (getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            if (navigationBarColorAnimator != null) {
                navigationBarColorAnimator.cancel();
            }
            super.setNavigationbarColor(color);
        }
    }

    @Override
    public void onPaletteColorChanged() {
        int playerFragmentColor = playerFragment.getPaletteColor();

        if (getPanelState() == PanelState.EXPANDED) {
            super.setTaskDescriptionColor(playerFragmentColor);
            if (currentNowPlayingScreen == NowPlayingScreen.COLOR) {
                super.setNavigationbarColor(playerFragmentColor);
            }
        }
    }

    private void setMiniPlayerAlphaProgress(@FloatRange(from = 0, to = 1) float progress) {
        if (miniPlayerFragment == null) {
            return;
        }
        float alpha = 1 - progress;
        miniPlayerFragment.getView().setAlpha(alpha);
        // necessary to make the views below clickable
        miniPlayerFragment.getView().setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);

    }

    public void hideBottomBar(final boolean hide) {

        int heightOfBar =
                getResources().getDimensionPixelSize(R.dimen.mini_player_height);
        int heightOfBarWithTabs =
                getResources().getDimensionPixelSize(R.dimen.mini_player_height_expanded);

        if (hide) {
            slidingUpPanelLayout.setPanelHeight(0);
            collapsePanel();
        } else {
            if (!MusicPlayerRemote.getPlayingQueue().isEmpty()) {
                slidingUpPanelLayout.setPanelHeight(bottomNavigationView.getVisibility() == View.VISIBLE ?
                        heightOfBarWithTabs : heightOfBar);
            }
        }
    }

    public void setBottomBarVisibility(int gone) {
        if (bottomNavigationView != null) {
            //TransitionManager.beginDelayedTransition(bottomNavigationView);
            bottomNavigationView.setVisibility(gone);
            hideBottomBar(false);
        }
    }

    protected View wrapSlidingMusicPanel(@LayoutRes int resId) {
        @SuppressLint("InflateParams")
        View slidingMusicPanelLayout = getLayoutInflater()
                .inflate(R.layout.sliding_music_panel_layout, null);
        ViewGroup contentContainer = slidingMusicPanelLayout.findViewById(R.id.content_container);
        getLayoutInflater().inflate(resId, contentContainer);
        return slidingMusicPanelLayout;
    }

    @Override
    public void onBackPressed() {
        if (!handleBackPress()) {
            super.onBackPressed();
        }
    }

    public boolean handleBackPress() {
        if (slidingUpPanelLayout.getPanelHeight() != 0 && playerFragment.onBackPressed()) {
            return true;
        }
        if (getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            collapsePanel();
            return true;
        }
        return false;
    }

    private void animateNavigationBarColor(int color) {
        if (navigationBarColorAnimator != null) {
            navigationBarColorAnimator.cancel();
        }
        navigationBarColorAnimator = ValueAnimator.ofArgb(getWindow().getNavigationBarColor(), color)
                .setDuration(ViewUtil.RETRO_MUSIC_ANIM_TIME);
        navigationBarColorAnimator.setInterpolator(new PathInterpolator(0.4f, 0f, 1f, 1f));
        navigationBarColorAnimator.addUpdateListener(animation -> {
            int playerFragmentColorDark = ColorUtil.darkenColor((Integer) animation.getAnimatedValue());

            bottomNavigationView.setBackgroundColor(playerFragmentColorDark);
            miniPlayerFragment.setColor(playerFragmentColorDark);
            AbsSlidingMusicPanelActivity.super.setNavigationbarColor(playerFragmentColorDark);

            View view = getWindow().getDecorView().getRootView();
            view.setBackgroundColor(playerFragmentColorDark);

            if (view.findViewById(R.id.toolbar) != null) {
                view.findViewById(R.id.toolbar).setBackgroundColor(playerFragmentColorDark);
            }
            if (view.findViewById(R.id.appbar) != null) {
                view.findViewById(R.id.appbar).setBackgroundColor(playerFragmentColorDark);
            }
            if (view.findViewById(R.id.status_bar) != null) {
                view.findViewById(R.id.status_bar)
                        .setBackgroundColor(ColorUtil.darkenColor(playerFragmentColorDark));
            }
        });
        navigationBarColorAnimator.start();
    }

    @Override
    protected View getSnackBarContainer() {
        return findViewById(R.id.content_container);
    }

    public SlidingUpPanelLayout getSlidingUpPanelLayout() {
        return slidingUpPanelLayout;
    }

    public MiniPlayerFragment getMiniPlayerFragment() {
        return miniPlayerFragment;
    }

    public AbsPlayerFragment getPlayerFragment() {
        return playerFragment;
    }

    public BottomNavigationViewEx getBottomNavigationView() {
        return bottomNavigationView;
    }

    public SlidingUpPanelLayout.PanelState getPanelState() {
        return slidingUpPanelLayout == null ? null : slidingUpPanelLayout.getPanelState();
    }

    public void collapsePanel() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public void expandPanel() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }
}
