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

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import java.io.InputStream;

public class AudioFileCoverLoader implements StreamModelLoader<AudioFileCover> {

  @Override
  public DataFetcher<InputStream> getResourceFetcher(AudioFileCover model, int width, int height) {
    return new AudioFileCoverFetcher(model);
  }

  public static class Factory implements ModelLoaderFactory<AudioFileCover, InputStream> {
    @Override
    public ModelLoader<AudioFileCover, InputStream> build(
        Context context, GenericLoaderFactory factories) {
      return new AudioFileCoverLoader();
    }

    @Override
    public void teardown() {}
  }
}
