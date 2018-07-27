package code.name.monkey.retromusic.lyrics;

import android.os.Handler;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.rest.KogouClient;
import code.name.monkey.retromusic.rest.model.KuGouSearchLyricResult;
import code.name.monkey.retromusic.util.LyricUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;

/**
 * @author Hemanth S (h4h13).
 */

public class KogouLyricsFetcher {

  private KogouClient mKogouClient;
  private Song mSong;
  private KogouLyricsCallback mCallback;

  public KogouLyricsFetcher(KogouLyricsCallback callback) {
    mCallback = callback;
    mKogouClient = new KogouClient();
  }

  public void loadLyrics(Song song, String duration) {
    mSong = song;
    mKogouClient.getApiService()
        .searchLyric(mSong.title, duration)
        .subscribe(this::parseKugouResult,
            throwable -> mCallback.onNoLyrics());
  }

  private void parseKugouResult(KuGouSearchLyricResult kuGouSearchLyricResult) {
    if (kuGouSearchLyricResult != null && kuGouSearchLyricResult.status == 200 &
        kuGouSearchLyricResult.candidates != null &&
        kuGouSearchLyricResult.candidates.size() != 0) {
      KuGouSearchLyricResult.Candidates candidates = kuGouSearchLyricResult.candidates.get(0);
      loadLyricsFile(candidates);
    } else {
      mCallback.onNoLyrics();
    }
  }

  private void loadLyricsFile(KuGouSearchLyricResult.Candidates candidates) {
    mKogouClient.getApiService().getRawLyric(candidates.id, candidates.accesskey)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(kuGouRawLyric -> {
          if (kuGouRawLyric == null) {
            mCallback.onNoLyrics();
            return;
          }
          String rawLyric = LyricUtil.decryptBASE64(kuGouRawLyric.content);
          LyricUtil.writeLrcToLoc(mSong.title, mSong.artistName, rawLyric);
          new Handler().postDelayed(
              () -> mCallback.onLyrics(LyricUtil.getLocalLyricFile(mSong.title, mSong.artistName)),
              1);
        });
  }

  public interface KogouLyricsCallback {

    void onNoLyrics();

    void onLyrics(File file);
  }
}
