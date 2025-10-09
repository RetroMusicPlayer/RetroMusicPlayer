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
    "cover.jpg", "album.jpg", "folder.jpg",
    "cover.png", "album.png", "folder.png", 
    "cover.webp", "album.webp", "folder.webp"
  };
  
  private static final long MIN_COVER_SIZE_BYTES = 1024;
  private static final long MAX_COVER_SIZE_BYTES = 50 * 1024 * 1024;


  public static InputStream fallback(String path) throws FileNotFoundException {
    if (path == null || path.trim().isEmpty()) {
      return null;
    }

    InputStream taggedArtwork = extractArtworkFromTags(path);
    if (taggedArtwork != null) {
      return taggedArtwork;
    }

    return findExternalCoverArt(path);
  }

  private static InputStream extractArtworkFromTags(String path) {
    try {
      MP3File mp3File = new MP3File(path);
      
      if (mp3File.hasID3v2Tag()) {
        Artwork artwork = mp3File.getTag().getFirstArtwork();
        if (isValidArtwork(artwork)) {
          return new ByteArrayInputStream(artwork.getBinaryData());
        }
      }
      
      if (mp3File.hasID3v1Tag()) {
        Artwork artwork = mp3File.getID3v1Tag().getFirstArtwork();
        if (isValidArtwork(artwork)) {
          return new ByteArrayInputStream(artwork.getBinaryData());
        }
      }
      
    } catch (Exception e) {
    }
    return null;
  }

  private static InputStream findExternalCoverArt(String path) throws FileNotFoundException {
    final File audioFile = new File(path);
    final File parentDirectory = audioFile.getParentFile();
    
    if (!isValidDirectory(parentDirectory)) {
      return null;
    }
    
    for (String coverFileName : FALLBACKS) {
      File coverFile = new File(parentDirectory, coverFileName);
      
      if (isValidCoverFile(coverFile)) {
        try {
          return new FileInputStream(coverFile);
        } catch (FileNotFoundException | SecurityException e) {
          continue;
        }
      }
    }
    
    return null;
  }

  private static boolean isValidArtwork(Artwork artwork) {
    if (artwork == null) {
      return false;
    }
    
    byte[] imageData = artwork.getBinaryData();
    return imageData != null && imageData.length > 0;
  }

  private static boolean isValidDirectory(File directory) {
    return directory != null && 
           directory.exists() && 
           directory.isDirectory() && 
           directory.canRead();
  }

  private static boolean isValidCoverFile(File coverFile) {
    if (!coverFile.exists() || !coverFile.isFile() || !coverFile.canRead()) {
      return false;
    }
    
    long fileSize = coverFile.length();
    return fileSize >= MIN_COVER_SIZE_BYTES && fileSize <= MAX_COVER_SIZE_BYTES;
  }
}
