package code.name.monkey.appthemehelper.util;

import android.os.Build;

/**
 * @author Hemanth S (h4h13).
 */

public final class VersionUtils {

    /**
     * @return true if device is running API >= 19
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * @return true if device is running API >= 20
     */
    public static boolean hasAndroidLPreview() {
        return Build.VERSION.SDK_INT >= 20;
    }

    /**
     * @return true if device is running API >= 21
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

    /**
     * @return true if device is running API >= 23
     */
    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * @return true if device is running API >= 24
     */
    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * @return true if device is running API >= 24
     */
    public static boolean hasNougatMR() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

    /**
     * @return true if device is running API >= 26
     */
    public static boolean hasOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
