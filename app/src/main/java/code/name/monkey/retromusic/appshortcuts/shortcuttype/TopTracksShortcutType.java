package code.name.monkey.retromusic.appshortcuts.shortcuttype;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.os.Build;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.appshortcuts.AppShortcutIconGenerator;
import code.name.monkey.retromusic.appshortcuts.AppShortcutLauncherActivity;



/**
 * @author Adrian Campos
 */
@TargetApi(Build.VERSION_CODES.N_MR1)
public final class TopTracksShortcutType extends BaseShortcutType {
    public TopTracksShortcutType(Context context) {
        super(context);
    }

    public static String getId() {
        return ID_PREFIX + "top_tracks";
    }

    public ShortcutInfo getShortcutInfo() {
        return new ShortcutInfo.Builder(context, getId())
                .setShortLabel(context.getString(R.string.app_shortcut_top_tracks_short))
                .setLongLabel(context.getString(R.string.app_shortcut_top_tracks_long))
                .setIcon(AppShortcutIconGenerator.generateThemedIcon(context, R.drawable.ic_app_shortcut_top_tracks))
                .setIntent(getPlaySongsIntent(AppShortcutLauncherActivity.SHORTCUT_TYPE_TOP_TRACKS))
                .build();
    }
}
