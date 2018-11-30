package code.name.monkey.retromusic.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.loaders.PlaylistLoader;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.adapter.playlist.AddToPlaylist;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;

/**
 * @author Karim Abou Zeid (kabouzeid), Aidan Follestad (afollestad)
 */
public class AddToPlaylistDialog extends RoundedBottomSheetDialogFragment {

    @BindView(R.id.playlists)
    RecyclerView playlist;

    @BindView(R.id.title)
    TextView title;

    @NonNull
    public static AddToPlaylistDialog create(Song song) {
        ArrayList<Song> list = new ArrayList<>();
        list.add(song);
        return create(list);
    }

    @NonNull
    public static AddToPlaylistDialog create(ArrayList<Song> songs) {
        AddToPlaylistDialog dialog = new AddToPlaylistDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("songs", songs);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_add_to_playlist, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @SuppressWarnings("ConstantConditions")
    @OnClick(R.id.action_add_playlist)
    void newPlaylist() {
        final ArrayList<Song> songs = getArguments().getParcelableArrayList("songs");
        CreatePlaylistDialog.create(songs)
                .show(getActivity().getSupportFragmentManager(), "ADD_TO_PLAYLIST");
        dismiss();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title.setTextColor(ThemeStore.textColorPrimary(getContext()));
        final ArrayList<Song> songs = getArguments().getParcelableArrayList("songs");
        final ArrayList<Playlist> playlists = PlaylistLoader.INSTANCE.getAllPlaylists(getActivity()).blockingFirst();
        final AddToPlaylist playlistAdapter = new AddToPlaylist(getActivity(), playlists, R.layout.item_playlist, songs, getDialog());
        playlist.setLayoutManager(new LinearLayoutManager(getContext()));
        playlist.setItemAnimator(new DefaultItemAnimator());
        playlist.setAdapter(playlistAdapter);
    }
}