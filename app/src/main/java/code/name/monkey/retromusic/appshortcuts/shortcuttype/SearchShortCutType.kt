package code.name.monkey.retromusic.appshortcuts.shortcuttype

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.ShortcutInfo
import android.os.Build
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.appshortcuts.AppShortcutIconGenerator
import code.name.monkey.retromusic.appshortcuts.AppShortcutLauncherActivity

@TargetApi(Build.VERSION_CODES.N_MR1)
class SearchShortCutType(context: Context) : BaseShortcutType(context) {
    companion object {

        val id: String
            get() = BaseShortcutType.ID_PREFIX + "search"
    }

    override val shortcutInfo: ShortcutInfo
        get() = ShortcutInfo.Builder(context, id)
                .setShortLabel(context.getString(R.string.action_search))
                .setLongLabel(context.getString(R.string.search_hint))
                .setIcon(AppShortcutIconGenerator.generateThemedIcon(context, R.drawable.ic_app_shortcut_search))
                .setIntent(getPlaySongsIntent(AppShortcutLauncherActivity.SHORTCUT_TYPE_SEARCH))
                .build()
}