package code.name.monkey.retromusic.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;

/**
 * @author Karim Abou Zeid (kabouzeid), Aidan Follestad (afollestad)
 */
public class SongDetailDialog extends RoundedBottomSheetDialogFragment {

    public static final String TAG = SongDetailDialog.class.getSimpleName();

    @NonNull
    public static SongDetailDialog create(Song song) {
        SongDetailDialog dialog = new SongDetailDialog();
        Bundle args = new Bundle();
        args.putParcelable("song", song);
        dialog.setArguments(args);
        return dialog;
    }

    private static Spanned makeTextWithTitle(@NonNull Context context, int titleResId, String text) {
        return Html.fromHtml("<b>" + context.getResources().getString(titleResId) + ": " + "</b>" + text);
    }

    private static String getFileSizeString(long sizeInBytes) {
        long fileSizeInKB = sizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;
        return fileSizeInMB + " MB";
    }

    private void setTextColor(List<TextView> textColor) {
        for (TextView textView : textColor) {
            //noinspection ConstantConditions
            textView.setTextColor(ThemeStore.textColorPrimary(getContext()));
        }
    }

    @BindViews({R.id.title,
            R.id.file_name,
            R.id.file_path,
            R.id.file_size,
            R.id.file_format,
            R.id.track_length,
            R.id.bitrate,
            R.id.sampling_rate})
    List<TextView> textViews;

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_file_details, container, false);
        ButterKnife.bind(this, dialogView);
        Context context = getContext();

        setTextColor(textViews);

        textViews.get(1).setText(makeTextWithTitle(context, R.string.label_file_name, "-"));
        textViews.get(2).setText(makeTextWithTitle(context, R.string.label_file_path, "-"));
        textViews.get(3).setText(makeTextWithTitle(context, R.string.label_file_size, "-"));
        textViews.get(4).setText(makeTextWithTitle(context, R.string.label_file_format, "-"));
        textViews.get(5).setText(makeTextWithTitle(context, R.string.label_track_length, "-"));
        textViews.get(6).setText(makeTextWithTitle(context, R.string.label_bit_rate, "-"));
        textViews.get(7).setText(makeTextWithTitle(context, R.string.label_sampling_rate, "-"));

        final Song song = getArguments().getParcelable("song");
        if (song != null) {
            final File songFile = new File(song.data);
            if (songFile.exists()) {
                textViews.get(1).setText(makeTextWithTitle(context, R.string.label_file_name, songFile.getName()));
                textViews.get(2).setText(makeTextWithTitle(context, R.string.label_file_path, songFile.getAbsolutePath()));
                textViews.get(3).setText(makeTextWithTitle(context, R.string.label_file_size, getFileSizeString(songFile.length())));
                try {
                    AudioFile audioFile = AudioFileIO.read(songFile);
                    AudioHeader audioHeader = audioFile.getAudioHeader();

                    textViews.get(4).setText(makeTextWithTitle(context, R.string.label_file_format, audioHeader.getFormat()));
                    textViews.get(5).setText(makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString(audioHeader.getTrackLength() * 1000)));
                    textViews.get(6).setText(makeTextWithTitle(context, R.string.label_bit_rate, audioHeader.getBitRate() + " kb/s"));
                    textViews.get(7).setText(makeTextWithTitle(context, R.string.label_sampling_rate, audioHeader.getSampleRate() + " Hz"));
                } catch (@NonNull CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
                    Log.e(TAG, "error while reading the song file", e);
                    // fallback
                    textViews.get(5).setText(makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString(song.duration)));
                }
            } else {
                // fallback
                textViews.get(1).setText(makeTextWithTitle(context, R.string.label_file_name, song.title));
                textViews.get(5).setText(makeTextWithTitle(context, R.string.label_track_length, MusicUtil.getReadableDurationString(song.duration)));
            }
        }

        return dialogView;
    }
}
