/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.model.lyrics;

import code.name.monkey.retromusic.model.Song;
import java.util.ArrayList;

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
