package code.name.monkey.retromusic.appshortcuts.shortcuttype;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.os.Build;
import android.os.Bundle;

import code.name.monkey.retromusic.appshortcuts.AppShortcutLauncherActivity;


/**
 * @author Adrian Campos
 */
@TargetApi(Build.VERSION_CODES.N_MR1)
public abstract class BaseShortcutType {

    static final String ID_PREFIX = "code.name.monkey.retromusic.appshortcuts.id.";

    Context context;

    public BaseShortcutType(Context context) {
        this.context = context;
    }

    static public String getId() {
        return ID_PREFIX + "invalid";
    }

    abstract ShortcutInfo getShortcutInfo();

    /**
     * Creates an Intent that will launch MainActivtiy and immediately play {@param songs} in either shuffle or normal mode
     *
     * @param shortcutType Describes the type of shortcut to create (ShuffleAll, TopTracks, custom playlist, etc.)
     * @return
     */
    Intent getPlaySongsIntent(int shortcutType) {
        Intent intent = new Intent(context, AppShortcutLauncherActivity.class);
        intent.setAction(Intent.ACTION_VIEW);

        Bundle b = new Bundle();
        b.putInt(AppShortcutLauncherActivity.KEY_SHORTCUT_TYPE, shortcutType);

        intent.putExtras(b);

        return intent;
    }
}
