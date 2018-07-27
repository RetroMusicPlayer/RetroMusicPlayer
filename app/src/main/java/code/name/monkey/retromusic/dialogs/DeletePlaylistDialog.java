package code.name.monkey.retromusic.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.util.PlaylistsUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;


public class DeletePlaylistDialog extends RoundedBottomSheetDialogFragment {

    @BindView(R.id.action_delete)
    TextView delete;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.action_cancel)
    TextView cancel;

    @NonNull
    public static DeletePlaylistDialog create(Playlist playlist) {
        ArrayList<Playlist> list = new ArrayList<>();
        list.add(playlist);
        return create(list);
    }

    @NonNull
    public static DeletePlaylistDialog create(ArrayList<Playlist> playlists) {
        DeletePlaylistDialog dialog = new DeletePlaylistDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("playlists", playlists);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_delete_playlist, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection unchecked
        final ArrayList<Playlist> playlists = getArguments().getParcelableArrayList("playlists");
        int title;
        CharSequence content;
        //noinspection ConstantConditions
        if (playlists.size() > 1) {
            title = R.string.delete_playlists_title;
            content = Html.fromHtml(getString(R.string.delete_x_playlists, playlists.size()));
        } else {
            title = R.string.delete_playlist_title;
            content = Html.fromHtml(getString(R.string.delete_playlist_x, playlists.get(0).name));
        }
        this.title.setText(title);
        this.delete.setText(content);
    }

    @OnClick({R.id.action_cancel, R.id.action_delete})
    void actions(View view) {
        final ArrayList<Playlist> playlists = getArguments().getParcelableArrayList("playlists");
        switch (view.getId()) {
            case R.id.action_delete:
                if (getActivity() == null)
                    return;
                PlaylistsUtil.deletePlaylists(getActivity(), playlists);
                break;
            default:
        }
        dismiss();
    }

}