package code.name.monkey.retromusic.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.mediarouter.app.MediaRouteActionProvider;
import androidx.mediarouter.app.MediaRouteButton;
import androidx.appcompat.view.ContextThemeWrapper;

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
        Context castContext = new ContextThemeWrapper(getContext(), androidx.mediarouter.R.style.Theme_MediaRouter);

        TypedArray a = castContext.obtainStyledAttributes(null,
                androidx.mediarouter.R.styleable.MediaRouteButton, androidx.mediarouter.R.attr.mediaRouteButtonStyle, 0);
        Drawable drawable = a.getDrawable(
                androidx.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable);
        a.recycle();
        if (drawable != null) {
            DrawableCompat.setTint(drawable, ThemeStore.textColorPrimary(getContext()));
            drawable.setState(button.getDrawableState());
            button.setRemoteIndicatorDrawable(drawable);
        }
    }
}