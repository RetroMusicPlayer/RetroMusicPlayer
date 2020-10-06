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

package code.name.monkey.retromusic.util;

import android.content.Context;
import android.graphics.Bitmap;
import java.io.File;
import java.io.IOException;

/**
 * Created on : June 18, 2016 Author : zetbaitsu Name : Zetra GitHub : https://github.com/zetbaitsu
 */
public class Compressor {
  // max width and height values of the compressed image is taken as 612x816
  private int maxWidth = 612;
  private int maxHeight = 816;
  private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
  private int quality = 80;
  private String destinationDirectoryPath;

  public Compressor(Context context) {
    destinationDirectoryPath = context.getCacheDir().getPath() + File.separator + "images";
  }

  public Compressor setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
    return this;
  }

  public Compressor setMaxHeight(int maxHeight) {
    this.maxHeight = maxHeight;
    return this;
  }

  public Compressor setCompressFormat(Bitmap.CompressFormat compressFormat) {
    this.compressFormat = compressFormat;
    return this;
  }

  public Compressor setQuality(int quality) {
    this.quality = quality;
    return this;
  }

  public Compressor setDestinationDirectoryPath(String destinationDirectoryPath) {
    this.destinationDirectoryPath = destinationDirectoryPath;
    return this;
  }

  public File compressToFile(File imageFile) throws IOException {
    return compressToFile(imageFile, imageFile.getName());
  }

  public File compressToFile(File imageFile, String compressedFileName) throws IOException {
    return ImageUtil.compressImage(
        imageFile,
        maxWidth,
        maxHeight,
        compressFormat,
        quality,
        destinationDirectoryPath + File.separator + compressedFileName);
  }

  public Bitmap compressToBitmap(File imageFile) throws IOException {
    return ImageUtil.decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight);
  }
}
