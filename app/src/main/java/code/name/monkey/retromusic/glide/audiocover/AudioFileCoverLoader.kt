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

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public class AudioFileCoverLoader implements ModelLoader<AudioFileCover, InputStream> {

  @Override
  public LoadData<InputStream> buildLoadData(@NonNull @NotNull AudioFileCover audioFileCover, int width, int height, @NonNull @NotNull Options options) {
    return new LoadData<>(new ObjectKey(audioFileCover.filePath), new AudioFileCoverFetcher(audioFileCover));
  }

  @Override
  public boolean handles(@NonNull @NotNull AudioFileCover audioFileCover) {
    return audioFileCover.filePath != null;
  }

  public static class Factory implements ModelLoaderFactory<AudioFileCover, InputStream> {

    @NotNull
    @Override
    public ModelLoader<AudioFileCover, InputStream> build(@NonNull @NotNull MultiModelLoaderFactory multiFactory) {
      return new AudioFileCoverLoader();
    }

    @Override
    public void teardown() {
    }
  }
}
