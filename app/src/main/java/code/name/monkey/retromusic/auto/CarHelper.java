package code.name.monkey.retromusic.auto;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;

public class CarHelper {

    private static final String TAG = "CarHelper";

    public static boolean isCarUiMode(Context c) {
        UiModeManager uiModeManager = (UiModeManager) c.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_CAR;
    }
}