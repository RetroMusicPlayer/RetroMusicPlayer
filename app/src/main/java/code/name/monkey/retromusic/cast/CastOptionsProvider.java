package code.name.monkey.retromusic.cast;

import android.content.Context;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.MediaIntentReceiver;
import com.google.android.gms.cast.framework.media.NotificationOptions;

import java.util.ArrayList;
import java.util.List;

import code.name.monkey.retromusic.R;

public class CastOptionsProvider implements OptionsProvider {
    @Override
    public CastOptions getCastOptions(Context context) {
        List<String> buttonActions = new ArrayList<>();
        buttonActions.add(MediaIntentReceiver.ACTION_TOGGLE_PLAYBACK);
        buttonActions.add(MediaIntentReceiver.ACTION_STOP_CASTING);
        int[] compatButtonActionsIndicies = new int[]{ 0, 1 };

        NotificationOptions notificationOptions = new NotificationOptions.Builder()
                .setActions(buttonActions, compatButtonActionsIndicies)
                .setTargetActivityClassName(ExpandedControlsActivity.class.getName())
                .build();

        CastMediaOptions mediaOptions = new CastMediaOptions.Builder()
                .setNotificationOptions(notificationOptions)
                .setExpandedControllerActivityClassName(ExpandedControlsActivity.class.getName())
                .build();

        return new CastOptions.Builder()
                .setReceiverApplicationId(context.getString(R.string.cast_app_id))
                .setCastMediaOptions(mediaOptions)
                .build();
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}
