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

package code.name.monkey.appthemehelper.common.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.appbar.MaterialToolbar;

/**
 * Created by hemanths on 2019-09-02.
 */
public class ATEToolbar extends MaterialToolbar {
    public ATEToolbar(Context context) {
        super(context);
    }

    public ATEToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ATEToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
