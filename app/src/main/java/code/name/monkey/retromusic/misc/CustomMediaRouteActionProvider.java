package code.name.monkey.retromusic.misc;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouteSelector;
import android.util.Log;
import android.view.ViewGroup;

import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.views.CustomMediaRouteButton;

public class CustomMediaRouteActionProvider extends MediaRouteActionProvider {
    private static final String TAG = "MediaRteActProvider";

    private Activity activity;

    private MediaRouteSelector selector = MediaRouteSelector.EMPTY;
    private CustomMediaRouteButton customMediaRouteButton;

    public CustomMediaRouteActionProvider(Context context) {
        super(context);
    }

    public void setActivity(@NonNull Activity activity) {
        this.activity = activity;
        if (customMediaRouteButton != null) {
            customMediaRouteButton.setActivity(activity);
        }
    }

    @Nullable
    @Override
    public MediaRouteButton getMediaRouteButton() {
        return customMediaRouteButton;
    }

    /**
     * Called when the media route button is being created.
     */
    @SuppressWarnings("deprecation")
    @Override
    public CustomMediaRouteButton onCreateMediaRouteButton() {
        if (customMediaRouteButton != null) {
            Log.e(TAG, "onCreateMediaRouteButton: This ActionProvider is already associated "
                    + "with a menu item. Don't reuse MediaRouteActionProvider instances!  "
                    + "Abandoning the old button...");
        }

        customMediaRouteButton = new CustomMediaRouteButton(getContext());
        customMediaRouteButton.setActivity(activity);
        customMediaRouteButton.setAlpha(RetroApplication.isProVersion() ? 1.0f : 0.5f);
        customMediaRouteButton.setRouteSelector(selector);
        customMediaRouteButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        return customMediaRouteButton;
    }
} 