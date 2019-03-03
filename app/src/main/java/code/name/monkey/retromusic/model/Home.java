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

package code.name.monkey.retromusic.model;

import java.util.ArrayList;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import code.name.monkey.retromusic.ui.adapter.HomeAdapter.Companion.HomeSection;

public class Home {
    @StringRes
    int title;
    @StringRes
    int subtitle;
    @HomeSection
    int homeSection;
    @DrawableRes
    int icon;

    ArrayList arrayList;

    public Home(int title, int subtitle, ArrayList arrayList, @HomeSection int homeSection, @DrawableRes int icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.arrayList = arrayList;
        this.homeSection = homeSection;
        this.icon = icon;
    }

    @HomeSection
    public int getHomeSection() {
        return homeSection;
    }

    @StringRes
    public int getTitle() {
        return title;
    }

    @StringRes
    public int getSubtitle() {
        return subtitle;
    }

    public ArrayList getArrayList() {
        return arrayList;
    }

    @DrawableRes
    public int getIcon() {
        return icon;
    }
}
