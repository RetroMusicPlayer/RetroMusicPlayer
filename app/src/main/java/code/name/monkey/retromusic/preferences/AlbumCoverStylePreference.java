package code.name.monkey.retromusic.preferences;

import android.content.Context;
import android.util.AttributeSet;

import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEDialogPreference;


public class AlbumCoverStylePreference extends ATEDialogPreference {
    public AlbumCoverStylePreference(Context context) {
        super(context);
    }

    public AlbumCoverStylePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumCoverStylePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AlbumCoverStylePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}