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

/**
 * Utility class for extracting cover art from audio files using fallback methods.
 * 
 * This class provides comprehensive cover art extraction capabilities when
 * MediaMetadataRetriever fails or doesn't find embedded artwork. It supports
 * multiple audio formats and external cover art files.
 * 
 * Thread-safe and optimized for performance with proper resource management.
 */
public class AudioFileCoverUtils {

  /**
   * Common cover art file names in order of preference.
   * Covers multiple formats (JPEG, PNG, WebP) and naming conventions.
   */
  public static final String[] FALLBACKS = {
    "cover.jpg", "album.jpg", "folder.jpg",
    "cover.png", "album.png", "folder.png", 
    "cover.webp", "album.webp", "folder.webp"
  };
  
  /**
   * Minimum valid cover art file size (1KB) to filter out placeholder files.
   */
  private static final long MIN_COVER_SIZE_BYTES = 1024;
  
  /**
   * Maximum valid cover art file size (50MB) to prevent memory issues.
   */
  private static final long MAX_COVER_SIZE_BYTES = 50 * 1024 * 1024;

  /**
   * Attempts to extract cover art using fallback methods when MediaMetadataRetriever fails.
   * 
   * This method implements a comprehensive fallback strategy:
   * 1. JAudioTagger with ID3v2 tag support for enhanced metadata extraction
   * 2. JAudioTagger with ID3v1 tag support for legacy files
   * 3. External cover art files in the same directory
   * 
   * @param path The file path of the audio file
   * @return InputStream of the cover art image, or null if no artwork is found
   * @throws FileNotFoundException if external cover files exist but cannot be read
   */
  public static InputStream fallback(String path) throws FileNotFoundException {
    if (path == null || path.trim().isEmpty()) {
      return null;
    }

    // Method 1: Use JAudioTagger for enhanced metadata extraction
    InputStream taggedArtwork = extractArtworkFromTags(path);
    if (taggedArtwork != null) {
      return taggedArtwork;
    }

    // Method 2: Look for external cover art files in the same directory
    return findExternalCoverArt(path);
  }

  /**
   * Extracts artwork from audio file tags using JAudioTagger.
   * Supports both ID3v2 and ID3v1 tags with comprehensive error handling.
   */
  private static InputStream extractArtworkFromTags(String path) {
    try {
      MP3File mp3File = new MP3File(path);
      
      // Try ID3v2 tag first (preferred for artwork)
      if (mp3File.hasID3v2Tag()) {
        Artwork artwork = mp3File.getTag().getFirstArtwork();
        if (isValidArtwork(artwork)) {
          return new ByteArrayInputStream(artwork.getBinaryData());
        }
      }
      
      // Fallback to ID3v1 tag if available
      if (mp3File.hasID3v1Tag()) {
        try {
          Artwork artwork = mp3File.getID3v1Tag().getFirstArtwork();
          if (isValidArtwork(artwork)) {
            return new ByteArrayInputStream(artwork.getBinaryData());
          }
        } catch (Exception e) {
          // ID3v1 artwork extraction failed, continue to external files
        }
      }
      
    } catch (ReadOnlyFileException | InvalidAudioFrameException | 
             TagException | IOException | CannotReadException e) {
      // Tag extraction failed, will try external files
    } catch (Exception e) {
      // Unexpected exception during tag processing
    }
    
    return null;
  }

  /**
   * Searches for external cover art files in the same directory as the audio file.
   * Checks multiple common cover art file names and formats.
   */
  private static InputStream findExternalCoverArt(String path) throws FileNotFoundException {
    final File audioFile = new File(path);
    final File parentDirectory = audioFile.getParentFile();
    
    if (!isValidDirectory(parentDirectory)) {
      return null;
    }
    
    // Search for cover art files in order of preference
    for (String coverFileName : FALLBACKS) {
      File coverFile = new File(parentDirectory, coverFileName);
      
      if (isValidCoverFile(coverFile)) {
        try {
          return new FileInputStream(coverFile);
        } catch (FileNotFoundException | SecurityException e) {
          // This specific cover file failed, try the next one
          continue;
        }
      }
    }
    
    return null;
  }

  /**
   * Validates that artwork data is present and non-empty.
   */
  private static boolean isValidArtwork(Artwork artwork) {
    if (artwork == null) {
      return false;
    }
    
    byte[] imageData = artwork.getBinaryData();
    return imageData != null && imageData.length > 0;
  }

  /**
   * Validates that the directory exists and is accessible.
   */
  private static boolean isValidDirectory(File directory) {
    return directory != null && 
           directory.exists() && 
           directory.isDirectory() && 
           directory.canRead();
  }

  /**
   * Validates that the cover file exists, is readable, and has a reasonable size.
   */
  private static boolean isValidCoverFile(File coverFile) {
    if (!coverFile.exists() || !coverFile.isFile() || !coverFile.canRead()) {
      return false;
    }
    
    // Size validation using defined constants
    long fileSize = coverFile.length();
    return fileSize >= MIN_COVER_SIZE_BYTES && fileSize <= MAX_COVER_SIZE_BYTES;
  }
}
