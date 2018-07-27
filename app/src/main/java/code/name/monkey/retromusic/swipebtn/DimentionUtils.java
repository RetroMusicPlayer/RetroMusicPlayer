package code.name.monkey.retromusic.swipebtn;

import android.content.Context;

final class DimentionUtils {

    private DimentionUtils() {
    }

    static float convertPixelsToSp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }
}
