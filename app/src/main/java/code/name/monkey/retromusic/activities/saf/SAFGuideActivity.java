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

package code.name.monkey.retromusic.activities.saf;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import code.name.monkey.retromusic.R;

/** Created by hemanths on 2019-07-31. */
public class SAFGuideActivity extends IntroActivity {

  public static final int REQUEST_CODE_SAF_GUIDE = 98;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setButtonCtaVisible(false);
    setButtonNextVisible(false);
    setButtonBackVisible(false);

    setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

    String title =
        String.format(getString(R.string.saf_guide_slide1_title), getString(R.string.app_name));

    addSlide(
        new SimpleSlide.Builder()
            .title(title)
            .description(
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1
                    ? R.string.saf_guide_slide1_description_before_o
                    : R.string.saf_guide_slide1_description)
            .image(R.drawable.saf_guide_1)
            .background(code.name.monkey.appthemehelper.R.color.md_deep_purple_300)
            .backgroundDark(code.name.monkey.appthemehelper.R.color.md_deep_purple_400)
            .layout(R.layout.fragment_simple_slide_large_image)
            .build());
    addSlide(
        new SimpleSlide.Builder()
            .title(R.string.saf_guide_slide2_title)
            .description(R.string.saf_guide_slide2_description)
            .image(R.drawable.saf_guide_2)
            .background(code.name.monkey.appthemehelper.R.color.md_deep_purple_500)
            .backgroundDark(code.name.monkey.appthemehelper.R.color.md_deep_purple_600)
            .layout(R.layout.fragment_simple_slide_large_image)
            .build());
    addSlide(
        new SimpleSlide.Builder()
            .title(R.string.saf_guide_slide3_title)
            .description(R.string.saf_guide_slide3_description)
            .image(R.drawable.saf_guide_3)
            .background(code.name.monkey.appthemehelper.R.color.md_deep_purple_700)
            .backgroundDark(code.name.monkey.appthemehelper.R.color.md_deep_purple_800)
            .layout(R.layout.fragment_simple_slide_large_image)
            .build());
  }
}
