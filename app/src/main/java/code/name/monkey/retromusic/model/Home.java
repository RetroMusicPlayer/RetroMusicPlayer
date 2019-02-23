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
