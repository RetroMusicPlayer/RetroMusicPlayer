package code.name.monkey.retromusic.ui.activities.base;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;

import com.google.android.gms.cast.framework.CastSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
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
import code.name.monkey.retromusic.ui.fragments.player.fit.FitFragment;
import code.name.monkey.retromusic.ui.fragments.player.flat.FlatPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.full.FullPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.hmm.HmmPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.material.MaterialFragment;
import code.name.monkey.retromusic.ui.fragments.player.normal.PlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.plain.PlainPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.simple.SimplePlayerFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroUtil;
import code.name.monkey.retromusic.util.ViewUtil;
import code.name.monkey.retromusic.views.BottomNavigationBarTinted;
import code.name.monkey.retromusic.views.FitSystemWindowsLayout;

public abstract class AbsSlidingMusicPanelActivity extends AbsMusicServiceActivity implements
        SlidingUpPanelLayout.PanelSlideListener, PlayerFragment.Callbacks {

    public static final String TAG = AbsSlidingMusicPanelActivity.class.getSimpleName();

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    @BindView(R.id.bottom_navigation)
    BottomNavigationBarTinted bottomNavigationView;

    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    private MiniPlayerFragment miniPlayerFragment;
    private AbsPlayerFragment playerFragment;
    private NowPlayingScreen currentNowPlayingScreen;

    private int navigationbarColor;
    private int taskColor;
    private boolean lightStatusbar;
    private boolean lightNavigationBar;
    private ValueAnimator navigationBarColorAnimator;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    protected AbsSlidingMusicPanelActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDrawUnderNavigationBar();
        super.onCreate(savedInstanceState);
        setContentView(createContentView());
        ButterKnife.bind(this);
        checkDisplayCutout();
        choosFragmentForTheme();
        setupSlidingUpPanel();
    }

    public void setBottomBarVisibility(int gone) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(gone);
            hideBottomBar(false);
        }
    }

    protected abstract View createContentView();

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        if (!MusicPlayerRemote.getPlayingQueue().isEmpty()) {
            slidingUpPanelLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    slidingUpPanelLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    hideBottomBar(false);
                }
            });
        } // don't call hideBottomBar(true) here as it causes a bug with the SlidingUpPanelLayout
    }

    @Override
    public void onQueueChanged() {
        super.onQueueChanged();
        hideBottomBar(MusicPlayerRemote.getPlayingQueue().isEmpty());
    }

    public void hideBottomBar(final boolean hide) {
        //int heightOfBarWithTabs = getResources().getDimensionPixelSize(R.dimen.mini_player_height_expanded) + RetroUtil.getNavigationBarHeight(getResources());
        int height = RetroUtil.checkNavigationBarHeight() ? getResources().getDimensionPixelSize(R.dimen.mini_player_height) : getResources().getDimensionPixelSize(R.dimen.mini_player_height);
        int heightOfBar = getResources().getDimensionPixelSize(R.dimen.mini_player_height) + RetroUtil.getNavigationBarHeight(this);
        int heightOfBarWithTabs =
                getResources().getDimensionPixelSize(R.dimen.mini_player_height_expanded) +
                        RetroUtil.getNavigationBarHeight(this);
        if (hide) {
            slidingUpPanelLayout.setPanelHeight(0);
            collapsePanel();
        } else {
            if (!MusicPlayerRemote.getPlayingQueue().isEmpty()) {
                slidingUpPanelLayout.setPanelHeight(bottomNavigationView.getVisibility() == View.VISIBLE ? heightOfBarWithTabs : heightOfBar);
            }
        }
    }

    private void checkDisplayCutout() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

    }

    protected View wrapSlidingMusicPanel(@LayoutRes int resId) {
        @SuppressLint("InflateParams")
        View slidingMusicPanelLayout = getLayoutInflater().inflate(R.layout.sliding_music_panel_layout, null);
        ViewGroup contentContainer = slidingMusicPanelLayout.findViewById(R.id.content_container);
        getLayoutInflater().inflate(resId, contentContainer);
        return slidingMusicPanelLayout;
    }

    @Override
    public void onBackPressed() {
        if (!handleBackPress())
            super.onBackPressed();
    }

    public boolean handleBackPress() {
        if (slidingUpPanelLayout.getPanelHeight() != 0 && playerFragment.onBackPressed())
            return true;
        if (getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            collapsePanel();
            return true;
        }
        return false;
    }

    @Override
    protected View getSnackBarContainer() {
        return findViewById(R.id.content_container);
    }

    @Override
    public void hideCastMiniController() {
        super.hideCastMiniController();
    }

    @Override
    public void showCastMiniController() {
        super.showCastMiniController();

    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        CastSession castSession = getCastSession();
        if (castSession == null) {
            return;
        }
        //MusicPlayerRemote.setZeroVolume();
        //CastHelper.startCasting(castSession, MusicPlayerRemote.getCurrentSong());
    }

    public void toggleBottomNavigationView(boolean toggle) {
        bottomNavigationView.setVisibility(toggle ? View.GONE : View.VISIBLE);
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public SlidingUpPanelLayout getSlidingUpPanelLayout() {
        return slidingUpPanelLayout;
    }

    public AbsPlayerFragment getPlayerFragment() {
        return playerFragment;
    }

    protected void setupSlidingUpPanel() {
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

        slidingUpPanelLayout.addPanelSlideListener(this);

        applyInsets();
    }

    protected void applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(coordinatorLayout, (v, insets) -> {
            //Bottom navigation view
            ViewGroup.MarginLayoutParams bParams = (ViewGroup.MarginLayoutParams) bottomNavigationView.getLayoutParams();
            if (!PreferenceUtil.getInstance().getFullScreenMode())
                bParams.bottomMargin = insets.getSystemWindowInsetBottom();
            bParams.rightMargin = insets.getSystemWindowInsetRight();
            bParams.leftMargin = insets.getSystemWindowInsetLeft();


            //For now playing screen
            FrameLayout layout = findViewById(R.id.safeArea);
            if (layout != null) {
                ViewGroup.MarginLayoutParams fParams = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
                if (!PreferenceUtil.getInstance().getFullScreenMode()) {
                    fParams.topMargin = insets.getSystemWindowInsetTop();
                    fParams.bottomMargin = insets.getSystemWindowInsetBottom();
                }
                fParams.leftMargin = insets.getSystemWindowInsetLeft();
                fParams.rightMargin = insets.getSystemWindowInsetRight();
            }

            //Mini player
            FitSystemWindowsLayout miniPlayer = (FitSystemWindowsLayout) miniPlayerFragment.getView();
            if (miniPlayer != null) {
                ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) miniPlayer.getLayoutParams();
                mParams.bottomMargin = insets.getSystemWindowInsetBottom();//RetroUtil.checkNavigationBarHeight() ? 0 : getResources().getDimensionPixelSize(R.dimen.mini_player_height);
                mParams.leftMargin = insets.getSystemWindowInsetLeft();
                mParams.rightMargin = insets.getSystemWindowInsetRight();
            }

            //For Library, Folder, Home etc
            ViewGroup viewGroup = findViewById(R.id.content_container);
            if (viewGroup != null) {
                ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) viewGroup.getLayoutParams();
                mParams.leftMargin = insets.getSystemWindowInsetLeft();
                mParams.rightMargin = insets.getSystemWindowInsetRight();
                mParams.bottomMargin = insets.getSystemWindowInsetBottom();
            }

            FrameLayout frameLayout = findViewById(R.id.sliding_panel);
            if (frameLayout != null) {
                ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
                mParams.leftMargin = insets.getSystemWindowInsetLeft();
                mParams.rightMargin = insets.getSystemWindowInsetRight();
                if (!PreferenceUtil.getInstance().getFullScreenMode()) {
                    mParams.bottomMargin = insets.getSystemWindowInsetBottom();
                }
            }

            coordinatorLayout.setOnApplyWindowInsetsListener(null);
            return insets.consumeSystemWindowInsets();
        });
    }

    public SlidingUpPanelLayout.PanelState getPanelState() {
        return slidingUpPanelLayout == null ? null : slidingUpPanelLayout.getPanelState();
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        bottomNavigationView.setTranslationY(slideOffset * 400);
        setMiniPlayerAlphaProgress(slideOffset);
        if (navigationBarColorAnimator != null) navigationBarColorAnimator.cancel();
        super.setNavigationbarColor((int) argbEvaluator.evaluate(slideOffset, navigationbarColor, Color.TRANSPARENT));
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
        super.setLightStatusbar(lightStatusbar);
        super.setTaskDescriptionColor(taskColor);
        super.setNavigationbarColor(ThemeStore.primaryColor(this));
        super.setLightNavigationBar(lightNavigationBar);

        playerFragment.setMenuVisibility(false);
        playerFragment.setUserVisibleHint(false);
        playerFragment.onHide();
    }

    public void onPanelExpanded(View panel) {
        int playerFragmentColor = playerFragment.getPaletteColor();

        super.setTaskDescriptionColor(playerFragmentColor);
        if (currentNowPlayingScreen == NowPlayingScreen.COLOR) {
            super.setNavigationbarColor(playerFragmentColor);
        } else {
            super.setNavigationbarColor(Color.TRANSPARENT);
        }

        onPaletteColorChanged();
        playerFragment.setMenuVisibility(true);
        playerFragment.setUserVisibleHint(true);
        playerFragment.onShow();

    }

    private void setMiniPlayerAlphaProgress(@FloatRange(from = 0, to = 1) float progress) {
        if (miniPlayerFragment.getView() == null) return;
        float alpha = 1 - progress;
        miniPlayerFragment.getView().setAlpha(alpha);
        // necessary to make the views below clickable
        miniPlayerFragment.getView().setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);
    }

    private void choosFragmentForTheme() {
        currentNowPlayingScreen = PreferenceUtil.getInstance().getNowPlayingScreen();

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
            case FIT:
                fragment = new FitFragment();
                break;
            case NORMAL:
            default:
                fragment = new PlayerFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.player_fragment_container, fragment).commit();
        getSupportFragmentManager().executePendingTransactions();

        playerFragment = (AbsPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_fragment_container);
        miniPlayerFragment = (MiniPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.mini_player_fragment);

        //noinspection ConstantConditions
        miniPlayerFragment.getView().setOnClickListener(v -> expandPanel());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentNowPlayingScreen != PreferenceUtil.getInstance().getNowPlayingScreen()) {
            postRecreate();
        }
    }

    public void setAntiDragView(View antiDragView) {
        //slidingUpPanelLayout.setAntiDragView(antiDragView);
    }

    public void collapsePanel() {
        slidingUpPanelLayout.setPanelState(PanelState.COLLAPSED);
    }

    public void expandPanel() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void onPaletteColorChanged() {
        if (getPanelState() == PanelState.EXPANDED) {
            int paletteColor = playerFragment.getPaletteColor();
            boolean isColorLight = ColorUtil.isColorLight(paletteColor);
            super.setTaskDescriptionColor(paletteColor);
            if ((currentNowPlayingScreen == NowPlayingScreen.FLAT || currentNowPlayingScreen == NowPlayingScreen.NORMAL) && PreferenceUtil.getInstance().getAdaptiveColor()) {
                super.setLightNavigationBar(true);
                super.setLightStatusbar(isColorLight);
            } else if (currentNowPlayingScreen == NowPlayingScreen.COLOR) {
                super.setLightStatusbar(isColorLight);
                super.setLightNavigationBar(isColorLight);
            } else if (currentNowPlayingScreen == NowPlayingScreen.BLUR || currentNowPlayingScreen == NowPlayingScreen.BLUR_CARD) {
                super.setLightStatusbar(false);
            } else if (currentNowPlayingScreen == NowPlayingScreen.CARD || currentNowPlayingScreen == NowPlayingScreen.FULL) {
                super.setNavigationbarColor(Color.TRANSPARENT);
                super.setLightStatusbar(false);
            } else if (currentNowPlayingScreen == NowPlayingScreen.FIT) {
                super.setNavigationbarColor(Color.TRANSPARENT);
                super.setLightStatusbar(false);
            } else {
                boolean isTheme = isOneOfTheseThemes() && ColorUtil.isColorLight(ThemeStore.primaryColor(this));
                super.setStatusbarColor(Color.TRANSPARENT);
                super.setLightStatusbar(isTheme);
                super.setLightNavigationBar(isTheme);
            }
        }
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
    public void setLightStatusbar(boolean enabled) {
        lightStatusbar = enabled;
        if (getPanelState() == PanelState.COLLAPSED) {
            super.setLightStatusbar(enabled);
        }
    }

    @Override
    public void setLightNavigationBar(boolean enabled) {
        lightNavigationBar = enabled;
        if (getPanelState() == PanelState.COLLAPSED) {
            super.setLightNavigationBar(enabled);
        }
    }

    @Override
    public void setNavigationbarColor(int color) {
        this.navigationbarColor = color;
        if (getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            if (navigationBarColorAnimator != null) navigationBarColorAnimator.cancel();
            super.setNavigationbarColor(color);
        }
    }

    private void animateNavigationBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (navigationBarColorAnimator != null) navigationBarColorAnimator.cancel();
            navigationBarColorAnimator = ValueAnimator
                    .ofArgb(getWindow().getNavigationBarColor(), color)
                    .setDuration(ViewUtil.RETRO_MUSIC_ANIM_TIME);
            navigationBarColorAnimator.setInterpolator(new PathInterpolator(0.4f, 0f, 1f, 1f));
            navigationBarColorAnimator.addUpdateListener(animation -> AbsSlidingMusicPanelActivity.super.setNavigationbarColor((Integer) animation.getAnimatedValue()));
            navigationBarColorAnimator.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (navigationBarColorAnimator != null) navigationBarColorAnimator.cancel(); // just in case
    }
}