package code.name.monkey.retromusic.model;

import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist;

import java.util.ArrayList;


/**
 * Created by BlackFootSanji on 5/22/2017.
 */

public class Home {
    public String sectionTitle;
    public ArrayList<Object> list;
    public AbsSmartPlaylist playlist;

    public Home(String sectionTitle, AbsSmartPlaylist playlist) {
        this.sectionTitle = sectionTitle;
        this.playlist = playlist;
    }

    public Home(String sectionTitle, ArrayList<Object> list) {
        this.sectionTitle = sectionTitle;
        this.list = list;
    }

    public Home(AbsSmartPlaylist playlist) {
        this.playlist = playlist;
    }

    public AbsSmartPlaylist getPlaylist() {
        return playlist;
    }

    @Override
    public String toString() {
        return "Home{" +
                "sectionTitle='" + sectionTitle + '\'' +
                ", songs=" + list +
                '}';
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public ArrayList<Object> getList() {
        return list;
    }
}
