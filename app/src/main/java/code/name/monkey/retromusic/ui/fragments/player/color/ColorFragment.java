package code.name.monkey.retromusic.ui.fragments.player.color;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.model.lyrics.Lyrics;
import code.name.monkey.retromusic.ui.activities.LyricsActivity;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.RetroColorUtil;
import code.name.monkey.retromusic.util.ViewUtil;

public class ColorFragment extends AbsPlayerFragment {

    @BindView(R.id.player_toolbar)
    Toolbar toolbar;

    @BindView(R.id.gradient_background)
    View colorBackground;

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.lyrics)
    TextView lyricsView;

    @BindView(R.id.lyrics_container)
    View lyricsViewContainer;

    @BindView(R.id.album_cover_container)
    View imageViewContainer;

    private int lastColor;
    private int backgroundColor;
    private ColorPlaybackControlsFragment playbackControlsFragment;
    private Unbinder unbinder;
    private ValueAnimator valueAnimator;
    private AsyncTask updateLyricsAsyncTask;
    private Lyrics lyrics;

    public static ColorFragment newInstance() {
        Bundle args = new Bundle();
        ColorFragment fragment = new ColorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onShow() {
        playbackControlsFragment.show();
    }

    @Override
    public void onHide() {
        playbackControlsFragment.hide();
        onBackPressed();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    @ColorInt
    public int getPaletteColor() {
        return backgroundColor;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public int toolbarIconColor() {
        return lastColor;
    }

    @Override
    protected void toggleFavorite(Song song) {
        super.toggleFavorite(song);
        if (song.id == MusicPlayerRemote.getCurrentSong().id) {
            updateIsFavorite();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSubFragments();
        setUpPlayerToolbar();
    }

    private void setUpSubFragments() {
        playbackControlsFragment = (ColorPlaybackControlsFragment)
                getChildFragmentManager().findFragmentById(R.id.playback_controls_fragment);

    }

    private void setUpPlayerToolbar() {
        toolbar.inflateMenu(R.menu.menu_player);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(this);

        ToolbarContentTintHelper.colorizeToolbar(toolbar,
                ATHUtil.resolveColor(getContext(), R.attr.iconColor), getActivity());
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        updateSong();
        updateLyricsLocal();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        updateSong();
        updateLyricsLocal();
    }

    private void updateSong() {
        Activity activity = getActivity();

        SongGlideRequest.Builder.from(Glide.with(activity), MusicPlayerRemote.getCurrentSong())
                .checkIgnoreMediaStore(activity)
                .generatePalette(activity).build().dontAnimate()
                .into(new RetroMusicColoredTarget(imageView) {
                    @Override
                    public void onColorReady(int color) {
                        //setColors(color);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);

                        int backgroundColor = getDefaultFooterColor();
                        int textColor = ColorUtil.isColorLight(getDefaultFooterColor()) ?
                                MaterialValueHelper.getPrimaryTextColor(getContext(), true) :
                                MaterialValueHelper.getPrimaryTextColor(getContext(), false);

                        setColors(backgroundColor, textColor);
                    }

                    @Override
                    public void onResourceReady(BitmapPaletteWrapper resource,
                                                GlideAnimation<? super BitmapPaletteWrapper> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
           /* MediaNotificationProcessor processor = new MediaNotificationProcessor(getContext(),
                getContext());
            Palette.Builder builder = MediaNotificationProcessor
                .generatePalette(resource.getBitmap());

            int backgroundColor = processor.getBackgroundColor(builder);
            int textColor = processor.getTextColor(builder);*/

                        Palette palette = resource.getPalette();
                        Palette.Swatch swatch = RetroColorUtil.getSwatch(palette);

                        int textColor = RetroColorUtil.getTextColor(palette);
                        int backgroundColor = swatch.getRgb();

                        setColors(backgroundColor, textColor);
                    }
                });
    }

    private void setColors(int backgroundColor, int textColor) {
        playbackControlsFragment.setDark(textColor, backgroundColor);

        colorBackground.setBackgroundColor(backgroundColor);

        ToolbarContentTintHelper.colorizeToolbar(toolbar, textColor, getActivity());

        lastColor = textColor;

        this.backgroundColor = backgroundColor;

        getCallbacks().onPaletteColorChanged();
    }

    private void colorize(int i) {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }

        valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.TRANSPARENT, i);
        valueAnimator.addUpdateListener(animation -> {
            if (colorBackground != null) {
                colorBackground.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(ViewUtil.RETRO_MUSIC_ANIM_TIME).start();
    }

    @SuppressLint("StaticFieldLeak")
    private void updateLyricsLocal() {
        if (updateLyricsAsyncTask != null) {
            updateLyricsAsyncTask.cancel(false);
        }
        final Song song = MusicPlayerRemote.getCurrentSong();
        updateLyricsAsyncTask = new AsyncTask<Void, Void, Lyrics>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lyrics = null;
                toolbar.getMenu().removeItem(R.id.action_show_lyrics);
            }

            @Override
            protected Lyrics doInBackground(Void... params) {
                String data = MusicUtil.getLyrics(song);
                if (TextUtils.isEmpty(data)) {
                    return null;
                }
                return Lyrics.parse(song, data);
            }

            @Override
            protected void onPostExecute(Lyrics l) {
                lyrics = l;
                if (lyrics == null) {
                    lyricsView.setText(R.string.no_lyrics_found);
                } else {
                    lyricsView.setText(lyrics.getText());
                }
            }

            @Override
            protected void onCancelled(Lyrics s) {
                onPostExecute(null);
            }
        }.execute();
    }

    @OnClick(R.id.expand)
    void expand() {
        startActivity(new Intent(getContext(), LyricsActivity.class));
    }

    @OnClick({R.id.lyrics, R.id.image})
    void toggleLyrics(View view) {
        if (lyricsViewContainer.getVisibility() == View.GONE) {
            lyricsViewContainer.setVisibility(View.VISIBLE);
        } else {
            lyricsViewContainer.setVisibility(View.GONE);
        }
    }
}
