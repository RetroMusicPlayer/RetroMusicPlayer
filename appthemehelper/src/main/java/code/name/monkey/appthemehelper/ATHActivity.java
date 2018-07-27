package code.name.monkey.appthemehelper;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Aidan Follestad (afollestad), Karim Abou Zeid (kabouzeid)
 */
public class ATHActivity extends AppCompatActivity {

    private long updateTime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getThemeRes());
        super.onCreate(savedInstanceState);
        updateTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ATH.didThemeValuesChange(this, updateTime)) {
            onThemeChanged();
        }
    }

    public void onThemeChanged() {
        postRecreate();
    }

    public void postRecreate() {
        // hack to prevent java.lang.RuntimeException: Performing pause of activity that is not resumed
        // makes sure recreate() is called right after and not in onResume()
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                recreate();
            }
        });
    }

    @StyleRes
    protected int getThemeRes() {
        return ThemeStore.activityTheme(this);
    }
}