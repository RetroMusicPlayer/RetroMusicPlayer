package code.name.monkey.retromusic.appshortcuts.shortcuttype

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.ShortcutInfo
import android.os.Build

import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.appshortcuts.AppShortcutIconGenerator
import code.name.monkey.retromusic.appshortcuts.AppShortcutLauncherActivity
import java.security.AccessController.getContext

@TargetApi(Build.VERSION_CODES.N_MR1)
class ShuffleAllShortcutType(context: Context) : BaseShortcutType(context) {

    override val shortcutInfo: ShortcutInfo
        get() = ShortcutInfo.Builder(context, id)
                .setShortLabel(context.getString(R.string.app_shortcut_shuffle_all_short))
                .setLongLabel(context.getString(R.string.app_shortcut_shuffle_all_long))
                .setIcon(AppShortcutIconGenerator.generateThemedIcon(context, R.drawable.ic_app_shortcut_shuffle_all))
                .setIntent(getPlaySongsIntent(AppShortcutLauncherActivity.SHORTCUT_TYPE_SHUFFLE_ALL))
                .build()

    companion object {

        val id: String
            get() = BaseShortcutType.ID_PREFIX + "shuffle_all"
    }
}
