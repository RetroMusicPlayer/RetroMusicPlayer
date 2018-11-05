package code.name.monkey.retromusic.ui.activities;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jaudiotagger.tag.FieldKey;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.model.lyrics.Lyrics;
import code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity;
import code.name.monkey.retromusic.ui.activities.tageditor.WriteTagsAsyncTask;
import code.name.monkey.retromusic.util.LyricUtil;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroUtil;
import code.name.monkey.retromusic.views.FitSystemWindowsLayout;
import code.name.monkey.retromusic.views.LyricView;
import io.reactivex.disposables.CompositeDisposable;

public class LyricsActivity extends AbsMusicServiceActivity implements
        MusicProgressViewUpdateHelper.Callback {

    @BindView(R.id.bottom_app_bar)
    BottomAppBar bottomAppBar;

    @BindView(R.id.lyrics_view)
    LyricView lyricView;

    @BindView(R.id.offline_lyrics)
    TextView offlineLyrics;

    @BindView(R.id.actions)
    RadioGroup actionsLayout;


    @BindView(R.id.fab)
    FloatingActionButton actionButton;

    @BindView(R.id.container)
    FitSystemWindowsLayout fitSystemWindowsLayout;

    private MusicProgressViewUpdateHelper updateHelper;
    private AsyncTask updateLyricsAsyncTask;
    private CompositeDisposable disposable;
    private Song song;
    private Lyrics lyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDrawUnderStatusBar();
        setDrawUnderNavigationBar();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lyrics);
        ButterKnife.bind(this);

        setTaskDescriptionColorAuto();
        setNavigationbarColorAuto();

        fitSystemWindowsLayout.setFit(true);

        setSupportActionBar(bottomAppBar);
        Objects.requireNonNull(bottomAppBar.getNavigationIcon())
                .setColorFilter(ThemeStore.textColorPrimary(this), PorterDuff.Mode.SRC_IN);
        bottomAppBar.setBackgroundTint(ColorStateList.valueOf(ThemeStore.primaryColor(this)));

        TintHelper.setTintAuto(actionButton, ThemeStore.accentColor(this), true);

        updateHelper = new MusicProgressViewUpdateHelper(this, 500, 1000);

        setupLyricsView();
        setupWakelock();
        loadLrcFile();

        actionsLayout.setOnCheckedChangeListener((group, checkedId) -> selectLyricsTye(checkedId));
        actionsLayout.check(PreferenceUtil.getInstance().getLastLyricsType());
    }

    private void selectLyricsTye(int group) {
        PreferenceUtil.getInstance().setLastLyricsType(group);
        RadioButton radioButton = actionsLayout.findViewById(group);
        if (radioButton != null) {
            radioButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            //radioButton.setTextColor(ThemeStore.textColorPrimary(this));
        }

        offlineLyrics.setVisibility(View.GONE);
        lyricView.setVisibility(View.GONE);

        switch (group) {
            case R.id.synced_lyrics:
                loadLRCLyrics();
                lyricView.setVisibility(View.VISIBLE);
                break;
            default:
            case R.id.normal_lyrics:
                loadSongLyrics();
                offlineLyrics.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void loadLRCLyrics() {
        if (LyricUtil.isLrcFileExist(song.title, song.artistName)) {
            showLyricsLocal(LyricUtil.getLocalLyricFile(song.title, song.artistName));
        }
    }

    private void setupWakelock() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void setupLyricsView() {
        disposable = new CompositeDisposable();

        lyricView
                .setOnPlayerClickListener((progress, content) -> MusicPlayerRemote.seekTo((int) progress));
        //lyricView.setHighLightTextColor(ThemeStore.accentColor(this));
        lyricView.setDefaultColor(ContextCompat.getColor(this, R.color.md_grey_400));
        //lyricView.setTouchable(false);
        lyricView.setHintColor(Color.WHITE);
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        loadLrcFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHelper.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateHelper.stop();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        loadLrcFile();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();

        if (updateLyricsAsyncTask != null && !updateLyricsAsyncTask.isCancelled()) {
            updateLyricsAsyncTask.cancel(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateProgressViews(int progress, int total) {
        lyricView.setCurrentTimeMillis(progress);
    }

    private void loadLrcFile() {
        song = MusicPlayerRemote.getCurrentSong();
        bottomAppBar.setTitle(song.title);
        bottomAppBar.setSubtitle(song.artistName);
        SongGlideRequest.Builder.from(Glide.with(this), song)
                .checkIgnoreMediaStore(this)
                .generatePalette(this)
                .build()
                .into(new RetroMusicColoredTarget(findViewById(R.id.image)) {
                    @Override
                    public void onColorReady(int color) {
                        if (PreferenceUtil.getInstance().getAdaptiveColor()) {
                            //background.setBackgroundColor(color);
                        }
                    }
                });
    }

    private void showLyricsLocal(File file) {
        if (file == null) {
            lyricView.reset();
        } else {
            lyricView.setLyricFile(file, "UTF-8");
        }
    }

    @OnClick({R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.fab:
                switch (actionsLayout.getCheckedRadioButtonId()) {
                    case R.id.synced_lyrics:
                        showSyncedLyrics();
                        break;
                    case R.id.normal_lyrics:
                        showLyricsSaveDialog();
                        break;
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadSongLyrics() {
        if (updateLyricsAsyncTask != null) {
            updateLyricsAsyncTask.cancel(false);
        }
        final Song song = MusicPlayerRemote.getCurrentSong();
        updateLyricsAsyncTask = new AsyncTask<Void, Void, Lyrics>() {
            @Override
            protected Lyrics doInBackground(Void... params) {
                String data = MusicUtil.getLyrics(song);
                if (TextUtils.isEmpty(data)) {
                    return null;
                }
                return Lyrics.parse(song, data);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lyrics = null;
            }

            @Override
            protected void onPostExecute(Lyrics l) {
                lyrics = l;
                offlineLyrics.setVisibility(View.VISIBLE);
                if (l == null) {
                    offlineLyrics.setText(R.string.no_lyrics_found);
                    return;
                }
                offlineLyrics.setText(l.data);
            }

            @Override
            protected void onCancelled(Lyrics s) {
                onPostExecute(null);
            }
        }.execute();
    }

    private void showSyncedLyrics() {
        String content = "";
        try {
            content = LyricUtil.getStringFromFile(song.title, song.artistName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MaterialDialog.Builder(this)
                .title("Add lyrics")
                .neutralText("Search")
                .content("Add time frame lyrics")
                .negativeText("Delete")
                .onNegative((dialog, which) -> {
                    LyricUtil.deleteLrcFile(song.title, song.artistName);
                    loadLrcFile();
                })
                .onNeutral(
                        (dialog, which) -> RetroUtil.openUrl(LyricsActivity.this, getGoogleSearchLrcUrl()))
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .input("Paste lyrics here", content, (dialog, input) -> {
                    LyricUtil.writeLrcToLoc(song.title, song.artistName, input.toString());
                    loadLrcFile();
                }).show();
    }

    private String getGoogleSearchLrcUrl() {
        String baseUrl = "http://www.google.com/search?";
        String query = song.title + "+" + song.artistName;
        query = "q=" + query.replace(" ", "+") + " .lrc";
        baseUrl += query;
        return baseUrl;
    }

    private void showLyricsSaveDialog() {
        String content = "";
        if (lyrics == null) {
            content = "";
        } else {
            content = lyrics.data;
        }
        new MaterialDialog.Builder(this)
                .title("Add lyrics")
                .neutralText("Search")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        RetroUtil.openUrl(LyricsActivity.this, getGoogleSearchUrl(song.title, song.artistName));
                    }
                })
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .input("Paste lyrics here", content, (dialog, input) -> {
                    Map<FieldKey, String> fieldKeyValueMap = new EnumMap<>(FieldKey.class);
                    fieldKeyValueMap.put(FieldKey.LYRICS, input.toString());

                    new WriteTagsAsyncTask(LyricsActivity.this)
                            .execute(
                                    new WriteTagsAsyncTask.LoadingInfo(getSongPaths(song), fieldKeyValueMap, null));
                    loadLrcFile();
                })
                .show();
    }

    private ArrayList<String> getSongPaths(Song song) {
        ArrayList<String> paths = new ArrayList<>(1);
        paths.add(song.data);
        return paths;
    }

    private String getGoogleSearchUrl(String title, String text) {
        String baseUrl = "http://www.google.com/search?";
        String query = title + "+" + text;
        query = "q=" + query.replace(" ", "+") + " lyrics";
        baseUrl += query;
        return baseUrl;
    }
}
