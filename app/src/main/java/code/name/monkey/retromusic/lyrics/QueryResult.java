package code.name.monkey.retromusic.lyrics;

public class QueryResult {
    public static final String ITEM_LRC = "lrc";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_ARTIST = "artist";
    public static final String ATTRIBUTE_TITLE = "title";

    public final int mId;
    public final String mArtist;
    public final String mTitle;

    QueryResult(int id, String artist, String title) {
        mId = id;
        mArtist = artist;
        mTitle = title;
    }
    
    @Override 
    public String toString() {
        return mTitle + " - " + mArtist;
    }
}