package code.name.monkey.retromusic.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.loaders.PlaylistLoader;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.PlaylistsUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;

/**
 * @author Karim Abou Zeid (kabouzeid), Aidan Follestad (afollestad)
 */
public class AddToPlaylistDialog extends RoundedBottomSheetDialogFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.playlists)
    ListView playlist;
    @BindView(R.id.title)
    TextView title;
    List<Playlist> playlists;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_add_to_playlist, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> playlistAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_list_item);
        playlists = PlaylistLoader.getAllPlaylists(getActivity()).blockingFirst();
        playlistAdapter.add(getActivity().getResources().getString(R.string.action_new_playlist));

        for (int i = 1; i < playlists.size(); i++) {
            playlistAdapter.add(playlists.get(i - 1).name);
            playlistAdapter.notifyDataSetChanged();
        }

        this.playlist.setAdapter(playlistAdapter);
        this.playlist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //noinspection unchecked
        final ArrayList<Song> songs = getArguments().getParcelableArrayList("songs");

        if (songs == null) {
            return;
        }
        if (i == 0) {
            dismiss();
            CreatePlaylistDialog.create(songs)
                    .show(getActivity().getSupportFragmentManager(), "ADD_TO_PLAYLIST");
        } else {
            dismiss();
            PlaylistsUtil.addToPlaylist(getActivity(), songs, playlists.get(i - 1).id, true);
        }
    }
}