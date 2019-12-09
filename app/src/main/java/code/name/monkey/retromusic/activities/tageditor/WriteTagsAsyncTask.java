package code.name.monkey.retromusic.activities.tageditor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.misc.DialogAsyncTask;
import code.name.monkey.retromusic.misc.UpdateToastMediaScannerCompletionListener;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.SAFUtil;

public class WriteTagsAsyncTask extends
        DialogAsyncTask<WriteTagsAsyncTask.LoadingInfo, Integer, String[]> {

    private WeakReference<Activity> activity;

    public WriteTagsAsyncTask(@NonNull Activity activity) {
        super(activity);
        this.activity = new WeakReference<>(activity);
    }

    @Override
    protected String[] doInBackground(LoadingInfo... params) {
        try {
            LoadingInfo info = params[0];

            Artwork artwork = null;
            File albumArtFile = null;
            if (info.artworkInfo != null && info.artworkInfo.getArtwork() != null) {
                try {
                    albumArtFile = MusicUtil.createAlbumArtFile().getCanonicalFile();
                    info.artworkInfo.getArtwork()
                            .compress(Bitmap.CompressFormat.PNG, 0, new FileOutputStream(albumArtFile));
                    artwork = ArtworkFactory.createArtworkFromFile(albumArtFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            int counter = 0;
            boolean wroteArtwork = false;
            boolean deletedArtwork = false;
            for (String filePath : info.filePaths) {
                publishProgress(++counter, info.filePaths.size());
                try {
                    Uri safUri = null;
                    if (filePath.contains(SAFUtil.SEPARATOR)) {
                        String[] fragments = filePath.split(SAFUtil.SEPARATOR);
                        filePath = fragments[0];
                        safUri = Uri.parse(fragments[1]);
                    }

                    AudioFile audioFile = AudioFileIO.read(new File(filePath));
                    Tag tag = audioFile.getTagOrCreateAndSetDefault();

                    if (info.fieldKeyValueMap != null) {
                        for (Map.Entry<FieldKey, String> entry : info.fieldKeyValueMap.entrySet()) {
                            try {
                                tag.setField(entry.getKey(), entry.getValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (info.artworkInfo != null) {
                        if (info.artworkInfo.getArtwork() == null) {
                            tag.deleteArtworkField();
                            deletedArtwork = true;
                        } else if (artwork != null) {
                            tag.deleteArtworkField();
                            tag.setField(artwork);
                            wroteArtwork = true;
                        }
                    }

                    Activity activity = this.activity.get();
                    SAFUtil.write(activity, audioFile, safUri);

                } catch (@NonNull Exception e) {
                    e.printStackTrace();
                }
            }

            Context context = getContext();
            if (context != null) {
                if (wroteArtwork) {
                    MusicUtil.insertAlbumArt(context, info.artworkInfo.getAlbumId(), albumArtFile.getPath());
                } else if (deletedArtwork) {
                    MusicUtil.deleteAlbumArt(context, info.artworkInfo.getAlbumId());
                }
            }

            Collection<String> paths = info.filePaths;
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                paths = new ArrayList<>(info.filePaths.size());
                for (String path : info.filePaths) {
                    if (path.contains(SAFUtil.SEPARATOR))
                        path = path.split(SAFUtil.SEPARATOR)[0];
                    paths.add(path);
                }
            }

            return paths.toArray(new String[paths.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] toBeScanned) {
        super.onPostExecute(toBeScanned);
        scan(toBeScanned);
    }

    @Override
    protected void onCancelled(String[] toBeScanned) {
        super.onCancelled(toBeScanned);
        scan(toBeScanned);
    }

    private void scan(String[] toBeScanned) {
        Activity activity = this.activity.get();
        if (activity != null) {
            MediaScannerConnection.scanFile(activity, toBeScanned, null, new UpdateToastMediaScannerCompletionListener(activity, toBeScanned));
        }
    }

    @NonNull
    @Override
    protected Dialog createDialog(@NonNull Context context) {
        return new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.saving_changes)
                .setCancelable(false)
                .setView(R.layout.loading)
                .create();
    }

    @Override
    protected void onProgressUpdate(@NonNull Dialog dialog, Integer... values) {
        super.onProgressUpdate(dialog, values);
        //((MaterialDialog) dialog).setMaxProgress(values[1]);
        //((MaterialDialog) dialog).setProgress(values[0]);
    }

    public static class LoadingInfo {

        final Collection<String> filePaths;
        @Nullable
        final Map<FieldKey, String> fieldKeyValueMap;
        @Nullable
        private AbsTagEditorActivity.ArtworkInfo artworkInfo;

        public LoadingInfo(Collection<String> filePaths,
                           @Nullable Map<FieldKey, String> fieldKeyValueMap,
                           @Nullable AbsTagEditorActivity.ArtworkInfo artworkInfo) {
            this.filePaths = filePaths;
            this.fieldKeyValueMap = fieldKeyValueMap;
            this.artworkInfo = artworkInfo;
        }
    }
}