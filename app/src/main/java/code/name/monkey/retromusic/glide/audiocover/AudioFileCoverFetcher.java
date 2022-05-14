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

import android.media.MediaMetadataRetriever;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioFileCoverFetcher implements DataFetcher<InputStream> {
  private final AudioFileCover model;

  private InputStream stream;

  public AudioFileCoverFetcher(AudioFileCover model) {

    this.model = model;
  }

  @Override
  public String getId() {
    // makes sure we never ever return null here
    return String.valueOf(model.filePath);
  }

  @Override
  public InputStream loadData(final Priority priority) throws Exception {

    final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    try {
      retriever.setDataSource(model.filePath);
      byte[] picture = retriever.getEmbeddedPicture();
      if (picture != null) {
        stream = new ByteArrayInputStream(picture);
      } else {
        stream = AudioFileCoverUtils.fallback(model.filePath);
      }
    } finally {
      retriever.release();
    }

    return stream;
  }

  @Override
  public void cleanup() {
    // already cleaned up in loadData and ByteArrayInputStream will be GC'd
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException ignore) {
        // can't do much about it
      }
    }
  }

  @Override
  public void cancel() {
    // cannot cancel
  }
}
