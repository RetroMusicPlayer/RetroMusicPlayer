package code.name.monkey.retromusic.ui.activities.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.cast.framework.CastSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.LayoutRes;
import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.NavigationViewUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.cast.CastHelper;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.ui.fragments.MiniPlayerFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.views.BottomNavigationBarTinted;

public abstract class AbsSlidingMusicPanelActivity extends AbsMusicServiceActivity {

    public static final String TAG = AbsSlidingMusicPanelActivity.class.getSimpleName();

    @BindView(R.id.bottom_navigation)
    BottomNavigationBarTinted bottomNavigationView;

    @BindView(R.id.parentPanel)
    ViewGroup parentPanel;

    @BindView(R.id.mini_player_container)
    ViewGroup miniPlayerContainer;

    private MiniPlayerFragment miniPlayerFragment;

    protected AbsSlidingMusicPanelActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createContentView());
        ButterKnife.bind(this);
        setLightStatusbar(true);
        setLightNavigationBar(true);
        //setupBottomView();
        miniPlayerFragment = (MiniPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.mini_player_fragment);
    }

    private void setupBottomView() {
        bottomNavigationView.setBackgroundColor(ThemeStore.primaryColor(this));
        bottomNavigationView.setSelectedItemId(PreferenceUtil.getInstance().getLastPage());

        int iconColor = ATHUtil.resolveColor(this, R.attr.iconColor);
        int accentColor = ThemeStore.accentColor(this);
        NavigationViewUtil.setItemIconColors(bottomNavigationView, ColorUtil.withAlpha(iconColor, 0.5f), accentColor);
        NavigationViewUtil.setItemTextColors(bottomNavigationView, ColorUtil.withAlpha(iconColor, 0.5f), accentColor);

        bottomNavigationView.setLabelVisibilityMode(PreferenceUtil.getInstance().getTabTitleMode());
        //bottomNavigationView.getMenu().removeItem(R.id.action_playlist);
    }

    public void setBottomBarVisibility(int gone) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(gone);
        }
    }

    protected abstract View createContentView();

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        if (!MusicPlayerRemote.getPlayingQueue().isEmpty())
            parentPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    parentPanel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    hideBottomBar(false);
                }
            });
    }

    @Override
    public void onQueueChanged() {
        super.onQueueChanged();
        hideBottomBar(MusicPlayerRemote.getPlayingQueue().isEmpty());
    }

    public void hideBottomBar(final boolean hide) {
        miniPlayerContainer.setVisibility(hide ? View.GONE : View.VISIBLE);
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
        if (handleBackPress()) {
            super.onBackPressed();
        }
    }

    public boolean handleBackPress() {
        return true;
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
        MusicPlayerRemote.setZeroVolume();
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        CastSession castSession = getCastSession();
        if (castSession == null) {
            return;
        }
        //MusicPlayerRemote.pauseSong();
        CastHelper.startCasting(castSession, MusicPlayerRemote.getCurrentSong());
    }

    public void toggleBottomNavigationView(boolean toggle) {
        bottomNavigationView.setVisibility(toggle ? View.GONE : View.VISIBLE);
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

}