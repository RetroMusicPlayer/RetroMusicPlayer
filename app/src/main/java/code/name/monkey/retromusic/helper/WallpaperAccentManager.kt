package code.name.monkey.retromusic.helper

import android.app.WallpaperColors
import android.app.WallpaperManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.util.PreferenceUtil

class WallpaperAccentManager(val context: Context) {

    private val onColorsChangedListener by lazy {
        WallpaperManager.OnColorsChangedListener { colors, which ->
            updateColors(colors, which)
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

    private fun updateColors(colors: WallpaperColors?, which: Int) {
        if (VersionUtils.hasOreoMR1()) {
            if (which == WallpaperManager.FLAG_SYSTEM && colors != null) {
                ThemeStore.editTheme(context).wallpaperColor(colors.primaryColor.toArgb())
                    .commit()
            }
        }
    }
}