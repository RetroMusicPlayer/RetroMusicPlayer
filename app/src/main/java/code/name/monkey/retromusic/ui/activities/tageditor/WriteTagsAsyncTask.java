package code.name.monkey.retromusic.ui.activities.tageditor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.misc.DialogAsyncTask;
import code.name.monkey.retromusic.misc.UpdateToastMediaScannerCompletionListener;
import code.name.monkey.retromusic.util.MusicUtil;
import com.afollestad.materialdialogs.MaterialDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

public class WriteTagsAsyncTask extends
    DialogAsyncTask<WriteTagsAsyncTask.LoadingInfo, Integer, String[]> {

  private Context applicationContext;

  public WriteTagsAsyncTask(Context context) {
    super(context);
    applicationContext = context;
  }

  @Override
  protected String[] doInBackground(LoadingInfo... params) {
    try {
      LoadingInfo info = params[0];

      Artwork artwork = null;
      File albumArtFile = null;
      if (info.artworkInfo != null && info.artworkInfo.artwork != null) {
        try {
          albumArtFile = MusicUtil.createAlbumArtFile().getCanonicalFile();
          info.artworkInfo.artwork
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
            if (info.artworkInfo.artwork == null) {
              tag.deleteArtworkField();
              deletedArtwork = true;
            } else if (artwork != null) {
              tag.deleteArtworkField();
              tag.setField(artwork);
              wroteArtwork = true;
            }
          }

          audioFile.commit();
        } catch (@NonNull CannotReadException | IOException | CannotWriteException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
          e.printStackTrace();
        }
      }

      Context context = getContext();
      if (context != null) {
        if (wroteArtwork) {
          MusicUtil.insertAlbumArt(context, info.artworkInfo.albumId, albumArtFile.getPath());
        } else if (deletedArtwork) {
          MusicUtil.deleteAlbumArt(context, info.artworkInfo.albumId);
        }
      }

      return info.filePaths.toArray(new String[info.filePaths.size()]);
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
    Context context = getContext();
    MediaScannerConnection.scanFile(applicationContext, toBeScanned, null,
        context instanceof Activity ? new UpdateToastMediaScannerCompletionListener(
            (Activity) context, toBeScanned) : null);
  }

  @Override
  protected Dialog createDialog(@NonNull Context context) {
    return new MaterialDialog.Builder(context)
        .title(R.string.saving_changes)
        .cancelable(false)
        .progress(false, 0)
        .build();
  }

  @Override
  protected void onProgressUpdate(@NonNull Dialog dialog, Integer... values) {
    super.onProgressUpdate(dialog, values);
    ((MaterialDialog) dialog).setMaxProgress(values[1]);
    ((MaterialDialog) dialog).setProgress(values[0]);
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