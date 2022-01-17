package code.name.monkey.retromusic.helper

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.util.PreferenceUtil

class WallpaperAccentManager(val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    private val onColorsChangedListener =
        WallpaperManager.OnColorsChangedListener { colors, which ->
            if (which == WallpaperManager.FLAG_SYSTEM && colors != null) {
                ThemeStore.editTheme(context).wallpaperColor(colors.primaryColor.toArgb()).commit()
            }
        }

    fun init() {
        if (VersionUtils.hasOreoMR1() && PreferenceUtil.wallpaperAccent) {
            WallpaperManager.getInstance(context).apply {
                addOnColorsChangedListener(onColorsChangedListener, Handler(Looper.getMainLooper()))
                ThemeStore.editTheme(context).wallpaperColor(
                    getWallpaperColors(WallpaperManager.FLAG_SYSTEM)?.primaryColor?.toArgb() ?: 0
                ).commit()
            }
        }
    }

    fun release() {
        if (VersionUtils.hasOreoMR1()) {
            WallpaperManager.getInstance(context)
                .removeOnColorsChangedListener(onColorsChangedListener)
        }
    }
}