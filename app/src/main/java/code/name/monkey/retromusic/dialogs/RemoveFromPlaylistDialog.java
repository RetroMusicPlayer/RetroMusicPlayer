package code.name.monkey.retromusic.dialogs;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.PlaylistSong;
import code.name.monkey.retromusic.util.PlaylistsUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;


public class RemoveFromPlaylistDialog extends RoundedBottomSheetDialogFragment {
    @BindView(R.id.action_remove)
    TextView remove;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.action_cancel)
    TextView cancel;

    @NonNull
    public static RemoveFromPlaylistDialog create(PlaylistSong song) {
        ArrayList<PlaylistSong> list = new ArrayList<>();
        list.add(song);
        return create(list);
    }

    @NonNull
    public static RemoveFromPlaylistDialog create(ArrayList<PlaylistSong> songs) {
        RemoveFromPlaylistDialog dialog = new RemoveFromPlaylistDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("songs", songs);
        dialog.setArguments(args);
        return dialog;
    }

    @OnClick({R.id.action_cancel, R.id.action_remove})
    void actions(View view) {
        final ArrayList<PlaylistSong> songs = getArguments().getParcelableArrayList("songs");
        switch (view.getId()) {
            case R.id.action_remove:
                if (getActivity() == null)
                    return;
                PlaylistsUtil.removeFromPlaylist(getActivity(), songs);
                break;
            default:
        }
        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_remove_from_playlist, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection unchecked
        final ArrayList<PlaylistSong> songs = getArguments().getParcelableArrayList("songs");
        int title;
        CharSequence content;
        if (songs != null && songs.size() > 1) {
            title = R.string.remove_songs_from_playlist_title;
            content = Html.fromHtml(getString(R.string.remove_x_songs_from_playlist, songs.size()));
        } else {
            title = R.string.remove_song_from_playlist_title;
            content = Html.fromHtml(getString(R.string.remove_song_x_from_playlist, songs.get(0).title));
        }
        this.remove.setText(content);
        this.title.setText(title);

        this.title.setTextColor(ThemeStore.textColorPrimary(getContext()));
        this.remove.setTextColor(ThemeStore.textColorSecondary(getContext()));
        this.cancel.setTextColor(ThemeStore.textColorSecondary(getContext()));
    }
}