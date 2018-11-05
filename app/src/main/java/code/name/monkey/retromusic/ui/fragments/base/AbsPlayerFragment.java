package code.name.monkey.retromusic.ui.fragments.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog;
import code.name.monkey.retromusic.dialogs.CreatePlaylistDialog;
import code.name.monkey.retromusic.dialogs.DeleteSongsDialog;
import code.name.monkey.retromusic.dialogs.SleepTimerDialog;
import code.name.monkey.retromusic.dialogs.SongDetailDialog;
import code.name.monkey.retromusic.dialogs.SongShareDialog;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.interfaces.PaletteColorHolder;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.activities.tageditor.AbsTagEditorActivity;
import code.name.monkey.retromusic.ui.activities.tageditor.SongTagEditorActivity;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroUtil;
import code.name.monkey.retromusic.views.FitSystemWindowsLayout;

public abstract class AbsPlayerFragment extends AbsMusicServiceFragment implements Toolbar.OnMenuItemClickListener, PaletteColorHolder {
    public static final String TAG = AbsPlayerFragment.class.getSimpleName();
    private Callbacks callbacks;
    private AsyncTask updateIsFavoriteTask;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callbacks = (Callbacks) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.getClass().getSimpleName() + " must implement " + Callbacks.class.getSimpleName());
        }
    }

    protected void addSafeArea(View view) {
        FitSystemWindowsLayout safeArea = view.findViewById(R.id.safeArea);
        if (safeArea != null) {
            if (PreferenceUtil.getInstance().getFullScreenMode()) safeArea.setFit(false);
            else safeArea.setFit(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final Song song = MusicPlayerRemote.getCurrentSong();
        switch (item.getItemId()) {
            case R.id.action_toggle_favorite:
                toggleFavorite(song);
                return true;
            case R.id.action_share:
                if (getFragmentManager() != null) {
                    SongShareDialog.create(song).show(getFragmentManager(), "SHARE_SONG");
                }
                return true;
            case R.id.action_delete_from_device:
                DeleteSongsDialog.create(song)
                        .show(getActivity().getSupportFragmentManager(), "DELETE_SONGS");
                return true;
            case R.id.action_add_to_playlist:
                if (getFragmentManager() != null) {
                    AddToPlaylistDialog.create(song).show(getFragmentManager(), "ADD_PLAYLIST");
                }
                return true;
            case R.id.action_clear_playing_queue:
                MusicPlayerRemote.clearQueue();
                return true;
            case R.id.action_save_playing_queue:
                CreatePlaylistDialog.create(MusicPlayerRemote.getPlayingQueue())
                        .show(getActivity().getSupportFragmentManager(), "ADD_TO_PLAYLIST");
                return true;
            case R.id.action_tag_editor:
                Intent intent = new Intent(getActivity(), SongTagEditorActivity.class);
                intent.putExtra(AbsTagEditorActivity.EXTRA_ID, song.id);
                startActivity(intent);
                return true;
            case R.id.action_details:
                if (getFragmentManager() != null) {
                    SongDetailDialog.create(song).show(getFragmentManager(), "SONG_DETAIL");
                }
                return true;
            case R.id.action_go_to_album:
                NavigationUtil.goToAlbum(getActivity(), song.albumId);
                return true;
            case R.id.action_go_to_artist:
                NavigationUtil.goToArtist(getActivity(), song.artistId);
                return true;
            case R.id.now_playing:
                NavigationUtil.goToPlayingQueue(getActivity());
                return true;
            case R.id.action_show_lyrics:
                NavigationUtil.goToLyrics(getActivity());
                return true;
            case R.id.action_equalizer:
                NavigationUtil.openEqualizer(getActivity());
                return true;
            case R.id.action_sleep_timer:
                new SleepTimerDialog().show(getFragmentManager(), TAG);
                return true;
            case R.id.action_set_as_ringtone:
                MusicUtil.setRingtone(getActivity(), song.id);
                return true;
            case R.id.action_settings:
                NavigationUtil.goToSettings(getActivity());
                return true;
            case R.id.action_go_to_genre:
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id);
                retriever.setDataSource(getActivity(), trackUri);
                String genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
                if (genre == null) {
                    genre = "Not Specified";
                }
                Toast.makeText(getContext(), genre, Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    protected void toggleFavorite(Song song) {
        MusicUtil.toggleFavorite(getActivity(), song);
    }

    public abstract void onShow();

    public abstract void onHide();

    public abstract boolean onBackPressed();

    public abstract Toolbar getToolbar();

    public abstract int toolbarIconColor();

    @Override
    public void onServiceConnected() {
        updateIsFavorite();
    }

    @Override
    public void onPlayingMetaChanged() {
        updateIsFavorite();
    }

    @Override
    public void onDestroyView() {
        if (updateIsFavoriteTask != null && !updateIsFavoriteTask.isCancelled()) {
            updateIsFavoriteTask.cancel(true);
        }
        super.onDestroyView();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateIsFavorite() {
        if (updateIsFavoriteTask != null) {
            updateIsFavoriteTask.cancel(false);
        }
        updateIsFavoriteTask = new AsyncTask<Song, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Song... params) {
                Activity activity = getActivity();
                if (activity != null) {
                    return MusicUtil.isFavorite(getActivity(), params[0]);
                } else {
                    cancel(false);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Boolean isFavorite) {
                Activity activity = getActivity();
                if (activity != null) {
                    int res = isFavorite ? R.drawable.ic_favorite_white_24dp
                            : R.drawable.ic_favorite_border_white_24dp;
                    Drawable drawable = RetroUtil.getTintedVectorDrawable(activity, res, toolbarIconColor());
                    getToolbar().getMenu().findItem(R.id.action_toggle_favorite)
                            .setIcon(drawable)
                            .setTitle(isFavorite ? getString(R.string.action_remove_from_favorites) : getString(R.string.action_add_to_favorites));
                }
            }
        }.execute(MusicPlayerRemote.getCurrentSong());
    }

    public Callbacks getCallbacks() {
        return callbacks;
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ThemeStore.primaryColor(getActivity()));
        if (PreferenceUtil.getInstance().getFullScreenMode()) {
            if (view.findViewById(R.id.status_bar) != null)
                view.findViewById(R.id.status_bar).setVisibility(View.GONE);
        }
    }

    public interface Callbacks {

        void onPaletteColorChanged();
    }
}
