package code.name.monkey.retromusic.util;

import android.content.Context;

import com.github.javiersantos.piracychecker.utils.LibraryUtilsKt;

public class PiracyCheckerUtils {

    public static String getAPKSignature(Context context) {
        return LibraryUtilsKt.getCurrentSignature(context);
    }

}