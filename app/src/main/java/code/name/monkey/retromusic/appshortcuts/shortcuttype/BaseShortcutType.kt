package code.name.monkey.retromusic.appshortcuts.shortcuttype

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.os.Build
import android.os.Bundle

import code.name.monkey.retromusic.appshortcuts.AppShortcutLauncherActivity


/**
 * @author Adrian Campos
 */
@TargetApi(Build.VERSION_CODES.N_MR1)
abstract class BaseShortcutType(internal var context: Context) {

    internal abstract val shortcutInfo: ShortcutInfo

    /**
     * Creates an Intent that will launch MainActivtiy and immediately play {@param songs} in either shuffle or normal mode
     *
     * @param shortcutType Describes the type of shortcut to create (ShuffleAll, TopTracks, custom playlist, etc.)
     * @return
     */
    internal fun getPlaySongsIntent(shortcutType: Int): Intent {
        val intent = Intent(context, AppShortcutLauncherActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        val b = Bundle()
        b.putInt(AppShortcutLauncherActivity.KEY_SHORTCUT_TYPE, shortcutType)
        intent.putExtras(b)
        return intent
    }

    companion object {
        internal const val ID_PREFIX = "code.name.monkey.retromusic.appshortcuts.id."
        val id: String
            get() = ID_PREFIX + "invalid"
    }
}
