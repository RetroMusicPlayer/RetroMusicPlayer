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

package code.name.monkey.retromusic.glide.audiocover;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AudioFileCoverUtils {

  public static final String[] FALLBACKS = {
    "cover.jpg", "album.jpg", "folder.jpg", "cover.png", "album.png", "folder.png"
  };

  public static InputStream fallback(String path) throws FileNotFoundException {
    // Method 1: use embedded high resolution album art if there is any
    try {
      MP3File mp3File = new MP3File(path);
      if (mp3File.hasID3v2Tag()) {
        Artwork art = mp3File.getTag().getFirstArtwork();
        if (art != null) {
          byte[] imageData = art.getBinaryData();
          return new ByteArrayInputStream(imageData);
        }
      }
      // If there are any exceptions, we ignore them and continue to the other fallback method
    } catch (ReadOnlyFileException | InvalidAudioFrameException | TagException | IOException | CannotReadException ignored) {
    }

    // Method 2: look for album art in external files
    final File parent = new File(path).getParentFile();
    for (String fallback : FALLBACKS) {
      File cover = new File(parent, fallback);
      if (cover.exists()) {
        return new FileInputStream(cover);
      }
    }
    return null;
  }
}
