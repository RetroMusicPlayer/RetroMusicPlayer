package code.name.monkey.retromusic.dialogs;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.util.PlaylistsUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;

public class RenamePlaylistDialog extends RoundedBottomSheetDialogFragment {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.option_1)
    TextInputEditText playlistName;

    @BindView(R.id.action_new_playlist)
    TextInputLayout textInputLayout;

    @BindView(R.id.action_cancel)
    MaterialButton actionCancel;

    @BindView(R.id.action_rename)
    MaterialButton rename;

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
                    PlaylistsUtil.renamePlaylist(getActivity(), playlistId,
                            playlistName.getText().toString());
                }
                break;
        }
        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_playlist_rename, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int accentColor = ThemeStore.accentColor(Objects.requireNonNull(getContext()));
        rename.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        actionCancel.setStrokeColor(ColorStateList.valueOf(accentColor));
        actionCancel.setTextColor(accentColor);
        playlistName.setHintTextColor(ColorStateList.valueOf(accentColor));

        textInputLayout.setBoxStrokeColor(accentColor);
        textInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(accentColor));

        playlistName.setHintTextColor(accentColor);

        playlistName.setTextColor(ThemeStore.textColorPrimary(getContext()));
        title.setTextColor(ThemeStore.textColorPrimary(getContext()));

        long playlistId = 0;
        if (getArguments() != null) {
            playlistId = getArguments().getLong("playlist_id");
        }
        playlistName.setText(PlaylistsUtil.getNameForPlaylist(getActivity(), playlistId));
    }
}