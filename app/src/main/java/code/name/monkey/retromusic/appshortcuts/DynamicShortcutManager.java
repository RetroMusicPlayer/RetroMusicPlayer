package code.name.monkey.retromusic.appshortcuts;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;

import java.util.Arrays;
import java.util.List;

import code.name.monkey.retromusic.appshortcuts.shortcuttype.LastAddedShortcutType;
import code.name.monkey.retromusic.appshortcuts.shortcuttype.ShuffleAllShortcutType;
import code.name.monkey.retromusic.appshortcuts.shortcuttype.TopTracksShortcutType;

/**
 * @author Adrian Campos
 */

@TargetApi(Build.VERSION_CODES.N_MR1)
public class DynamicShortcutManager {

    private Context context;
    private ShortcutManager shortcutManager;

    public DynamicShortcutManager(Context context) {
        this.context = context;
        shortcutManager = this.context.getSystemService(ShortcutManager.class);
    }

    public static ShortcutInfo createShortcut(Context context, String id, String shortLabel, String longLabel, Icon icon, Intent intent) {
        return new ShortcutInfo.Builder(context, id)
                .setShortLabel(shortLabel)
                .setLongLabel(longLabel)
                .setIcon(icon)
                .setIntent(intent)
                .build();
    }

    public static void reportShortcutUsed(Context context, String shortcutId) {
        context.getSystemService(ShortcutManager.class).reportShortcutUsed(shortcutId);
    }

    public void initDynamicShortcuts() {
        if (shortcutManager.getDynamicShortcuts().size() == 0) {
            shortcutManager.setDynamicShortcuts(getDefaultShortcuts());
        }
    }

    public void updateDynamicShortcuts() {
        shortcutManager.updateShortcuts(getDefaultShortcuts());
    }

    public List<ShortcutInfo> getDefaultShortcuts() {
        return (Arrays.asList(
                new ShuffleAllShortcutType(context).getShortcutInfo(),
                new TopTracksShortcutType(context).getShortcutInfo(),
                new LastAddedShortcutType(context).getShortcutInfo()
        ));
    }
}
