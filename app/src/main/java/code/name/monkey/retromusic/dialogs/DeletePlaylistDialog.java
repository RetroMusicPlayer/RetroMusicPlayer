package code.name.monkey.retromusic.dialogs;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.util.PlaylistsUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;


public class DeletePlaylistDialog extends RoundedBottomSheetDialogFragment {

    @BindView(R.id.action_delete)
    MaterialButton actionDelete;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.action_cancel)
    MaterialButton actionCancel;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_delete, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @SuppressWarnings("ConstantConditions")
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
        this.title.setText(content);
        this.actionDelete.setText(title);

        this.title.setTextColor(ThemeStore.textColorPrimary(getContext()));

        int accentColor = ThemeStore.accentColor(Objects.requireNonNull(getContext()));
        actionDelete.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        actionCancel.setStrokeColor(ColorStateList.valueOf(accentColor));
        actionCancel.setTextColor(accentColor);

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