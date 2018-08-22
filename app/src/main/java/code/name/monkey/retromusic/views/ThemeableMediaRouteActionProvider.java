package code.name.monkey.retromusic.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.view.ContextThemeWrapper;

import code.name.monkey.appthemehelper.ThemeStore;

public class ThemeableMediaRouteActionProvider extends MediaRouteActionProvider {
    public ThemeableMediaRouteActionProvider(Context context) {
        super(context);
    }

    @Override
    public MediaRouteButton onCreateMediaRouteButton() {
        MediaRouteButton button = super.onCreateMediaRouteButton();
        colorWorkaroundForCastIcon(button);
        return button;
    }

    @Nullable
    @Override
    public MediaRouteButton getMediaRouteButton() {
        MediaRouteButton button = super.getMediaRouteButton();
        colorWorkaroundForCastIcon(button);
        return button;
    }

    private void colorWorkaroundForCastIcon(MediaRouteButton button) {
        if (button == null) return;
        Context castContext = new ContextThemeWrapper(getContext(), android.support.v7.mediarouter.R.style.Theme_MediaRouter);

        TypedArray a = castContext.obtainStyledAttributes(null,
                android.support.v7.mediarouter.R.styleable.MediaRouteButton, android.support.v7.mediarouter.R.attr.mediaRouteButtonStyle, 0);
        Drawable drawable = a.getDrawable(
                android.support.v7.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable);
        a.recycle();
        if (drawable != null) {
            DrawableCompat.setTint(drawable, ThemeStore.textColorPrimary(getContext()));
            drawable.setState(button.getDrawableState());
            button.setRemoteIndicatorDrawable(drawable);
        }
    }
}