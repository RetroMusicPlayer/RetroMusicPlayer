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

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.util.PlaylistsUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;

/**
 * @author Karim Abou Zeid (kabouzeid), Aidan Follestad (afollestad)
 */
public class RenamePlaylistDialog extends RoundedBottomSheetDialogFragment {
    @BindView(R.id.option_1)
    EditText playlistName;
    @BindView(R.id.action_cancel)
    Button cancel;
    @BindView(R.id.action_rename)
    Button rename;

    @NonNull
    public static RenamePlaylistDialog create(long playlistId) {
        RenamePlaylistDialog dialog = new RenamePlaylistDialog();
        Bundle args = new Bundle();
        args.putLong("playlist_id", playlistId);
        dialog.setArguments(args);
        return dialog;
    }

    @OnClick({R.id.action_cancel, R.id.action_rename})
    void actions(View view) {
        switch (view.getId()) {
            case R.id.action_cancel:
                dismiss();
                break;
            case R.id.action_rename:
                if (!playlistName.toString().trim().equals("")) {
                    long playlistId = getArguments().getLong("playlist_id");
                    PlaylistsUtil.renamePlaylist(getActivity(), playlistId, playlistName.toString());
                }
                break;
        }
        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_playlist_rename, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int accentColor = ThemeStore.accentColor(Objects.requireNonNull(getContext()));
        TintHelper.setTintAuto(playlistName, accentColor, true);
        TintHelper.setTintAuto(rename, accentColor, true);
        cancel.setTextColor(accentColor);

        long playlistId = getArguments().getLong("playlist_id");
        playlistName.setText(PlaylistsUtil.getNameForPlaylist(getActivity(), playlistId));
    }
}