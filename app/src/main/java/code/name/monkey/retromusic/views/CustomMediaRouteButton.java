package code.name.monkey.retromusic.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.MediaRouteButton;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;

import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.util.NavigationUtil;

public class CustomMediaRouteButton extends MediaRouteButton {

    @Nullable
    private Activity activity;

    public CustomMediaRouteButton(Context context) {
        this(context, null);
    }

    public CustomMediaRouteButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.mediaRouteButtonStyle);
    }

    public CustomMediaRouteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(getThemedContext(context), attrs, defStyleAttr);
    }

    private static Context getThemedContext(Context context) {
        return ATHUtil.isWindowBackgroundDark(context) ?
                new ContextThemeWrapper(new ContextThemeWrapper(context, R.style.Theme_AppCompat), R.style.Theme_MediaRouter) :
                new ContextThemeWrapper(new ContextThemeWrapper(context, R.style.Theme_AppCompat_Light), R.style.Theme_MediaRouter);
    }

    public void setActivity(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean showDialog() {

        if (!RetroApplication.isProVersion()) {
            if (activity != null) {
                NavigationUtil.goToProVersion((Activity) getContext());
            }
            return false;
        }

        return super.showDialog();
    }
} 