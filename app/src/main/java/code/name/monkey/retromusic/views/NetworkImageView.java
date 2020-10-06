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

package code.name.monkey.retromusic.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import code.name.monkey.retromusic.R;
import com.bumptech.glide.Glide;

/** @author Hemanth S (h4h13). */
public class NetworkImageView extends CircularImageView {

  public NetworkImageView(@NonNull Context context) {
    super(context);
    init(context, null);
  }

  public NetworkImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public NetworkImageView(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  public void setImageUrl(@NonNull String imageUrl) {
    setImageUrl(getContext(), imageUrl);
  }

  public void setImageUrl(@NonNull Context context, @NonNull String imageUrl) {
    Glide.with(context)
        .load(imageUrl)
        .error(R.drawable.ic_account)
        .placeholder(R.drawable.ic_account)
        .into(this);
  }

  private void init(Context context, AttributeSet attributeSet) {
    TypedArray attributes =
        context.obtainStyledAttributes(attributeSet, R.styleable.NetworkImageView, 0, 0);
    String url = attributes.getString(R.styleable.NetworkImageView_url_link);
    setImageUrl(context, url);
    attributes.recycle();
  }
}
