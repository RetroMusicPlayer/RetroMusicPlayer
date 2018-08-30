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
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;

public class DeleteSongsDialog extends RoundedBottomSheetDialogFragment {
    @BindView(R.id.action_delete)
    MaterialButton actionDelete;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.action_cancel)
    MaterialButton actionCancel;

    @NonNull
    public static DeleteSongsDialog create(Song song) {
        ArrayList<Song> list = new ArrayList<>();
        list.add(song);
        return create(list);
    }

    @NonNull
    public static DeleteSongsDialog create(ArrayList<Song> songs) {
        DeleteSongsDialog dialog = new DeleteSongsDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("songs", songs);
        dialog.setArguments(args);
        return dialog;
    }

    @OnClick({R.id.action_cancel, R.id.action_delete})
    void actions(View view) {
        //noinspection ConstantConditions
        final ArrayList<Song> songs = getArguments().getParcelableArrayList("songs");
        switch (view.getId()) {
            case R.id.action_delete:
                if (getActivity() == null) {
                    return;
                }
                if (songs != null) {
                    MusicUtil.deleteTracks(getActivity(), songs);
                }
                break;
            default:
        }
        dismiss();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title.setTextColor(ThemeStore.textColorPrimary(getContext()));
        int accentColor = ThemeStore.accentColor(Objects.requireNonNull(getContext()));
        actionDelete.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        actionCancel.setStrokeColor(ColorStateList.valueOf(accentColor));
        actionCancel.setTextColor(accentColor);

        //noinspection unchecked,ConstantConditions
        final ArrayList<Song> songs = getArguments().getParcelableArrayList("songs");
        CharSequence content;
        if (songs != null) {
            if (songs.size() > 1) {
                content = Html.fromHtml(getString(R.string.delete_x_songs, songs.size()));
            } else {
                content = Html.fromHtml(getString(R.string.delete_song_x, songs.get(0).title));
            }
            this.title.setText(content);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_delete, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }
}

