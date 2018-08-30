package code.name.monkey.retromusic.model;

import androidx.annotation.StringRes;

import java.util.ArrayList;


/**
 * Created by BlackFootSanji on 5/22/2017.
 */

public class Home {

    private int sectionTitle;
    private ArrayList list;

    public Home(@StringRes int sectionTitle, ArrayList list) {
        this.sectionTitle = sectionTitle;
        this.list = list;
    }

    public int getSectionTitle() {
        return sectionTitle;
    }

    public ArrayList getList() {
        return list;
    }
}
