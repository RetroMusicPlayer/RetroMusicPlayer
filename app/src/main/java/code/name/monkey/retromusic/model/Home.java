package code.name.monkey.retromusic.model;

import androidx.annotation.StringRes;

import java.util.ArrayList;

import code.name.monkey.retromusic.ui.adapter.HomeAdapter.Companion.HomeSection;

public class Home {
    @StringRes
    int title;
    @StringRes
    int subtitle;
    @HomeSection
    int homeSection;

    ArrayList arrayList;

    public Home(int title, int subtitle, ArrayList arrayList, @HomeSection int homeSection) {
        this.title = title;
        this.subtitle = subtitle;
        this.arrayList = arrayList;
        this.homeSection = homeSection;
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
}
