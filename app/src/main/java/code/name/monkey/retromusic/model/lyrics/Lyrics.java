package code.name.monkey.retromusic.model.lyrics;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Song;

public class Lyrics {

  private static final ArrayList<Class<? extends Lyrics>> FORMATS = new ArrayList<>();

  static {
    Lyrics.FORMATS.add(SynchronizedLyricsLRC.class);
  }

  public String data;
  public Song song;
  protected boolean parsed = false;
  protected boolean valid = false;

  public static boolean isSynchronized(String data) {
    for (Class<? extends Lyrics> format : Lyrics.FORMATS) {
      try {
        Lyrics lyrics = format.newInstance().setData(null, data);
        if (lyrics.isValid()) {
          return true;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static Lyrics parse(Song song, String data) {
    for (Class<? extends Lyrics> format : Lyrics.FORMATS) {
      try {
        Lyrics lyrics = format.newInstance().setData(song, data);
        if (lyrics.isValid()) {
          return lyrics.parse(false);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return new Lyrics().setData(song, data).parse(false);
  }

  public String getText() {
    return this.data.trim().replaceAll("(\r?\n){3,}", "\r\n\r\n");
  }

  public boolean isSynchronized() {
    return false;
  }

  public boolean isValid() {
    this.parse(true);
    return this.valid;
  }

  public Lyrics parse(boolean check) {
    this.valid = true;
    this.parsed = true;
    return this;
  }

  public Lyrics setData(Song song, String data) {
    this.song = song;
    this.data = data;
    return this;
  }
}
