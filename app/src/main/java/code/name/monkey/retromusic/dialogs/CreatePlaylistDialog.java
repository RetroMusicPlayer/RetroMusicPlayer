package code.name.monkey.retromusic.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.PlaylistsUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;

/**
 * @author Karim Abou Zeid (kabouzeid), Aidan Follestad (afollestad)
 */
public class CreatePlaylistDialog extends RoundedBottomSheetDialogFragment {

    @BindView(R.id.option_1)
    EditText playlistName;
    @BindView(R.id.action_cancel)
    Button actionCancel;
    @BindView(R.id.action_create)
    Button actionCreate;

    @NonNull
    public static CreatePlaylistDialog create() {
        return create((Song) null);
    }

    @NonNull
    public static CreatePlaylistDialog create(@Nullable Song song) {
        ArrayList<Song> list = new ArrayList<>();
        if (song != null) {
            list.add(song);
        }
        return create(list);
    }

    @NonNull
    public static CreatePlaylistDialog create(ArrayList<Song> songs) {
        CreatePlaylistDialog dialog = new CreatePlaylistDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("songs", songs);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_create_playlist, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int accentColor = ThemeStore.accentColor(Objects.requireNonNull(getContext()));
        TintHelper.setTintAuto(playlistName, accentColor, true);
        TintHelper.setTintAuto(actionCreate, accentColor, true);
        actionCancel.setTextColor(accentColor);
    }

    @OnClick({R.id.action_cancel, R.id.action_create})
    void actions(View view) {
        switch (view.getId()) {
            case R.id.action_cancel:
                dismiss();
                break;
            case R.id.action_create:
                if (getActivity() == null) {
                    return;
                }
                if (!playlistName.getText().toString().trim().isEmpty()) {
                    final int playlistId = PlaylistsUtil
                            .createPlaylist(getActivity(), playlistName.getText().toString());
                    if (playlistId != -1 && getActivity() != null) {
                        //noinspection unchecked
                        ArrayList<Song> songs = getArguments().getParcelableArrayList("songs");
                        if (songs != null) {
                            PlaylistsUtil.addToPlaylist(getActivity(), songs, playlistId, true);
                        }
                    }
                }
                break;
        }
        dismiss();
    }
}