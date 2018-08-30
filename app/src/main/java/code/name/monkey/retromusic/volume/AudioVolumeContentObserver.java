package code.name.monkey.retromusic.volume;

import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;

public class AudioVolumeContentObserver extends ContentObserver {

    private final OnAudioVolumeChangedListener mListener;
    private final AudioManager mAudioManager;
    private final int mAudioStreamType;
    private int mLastVolume;

    AudioVolumeContentObserver(@NonNull Handler handler, @NonNull AudioManager audioManager,
                               int audioStreamType,
                               @NonNull OnAudioVolumeChangedListener listener) {

        super(handler);
        mAudioManager = audioManager;
        mAudioStreamType = audioStreamType;
        mListener = listener;
        mLastVolume = audioManager.getStreamVolume(mAudioStreamType);
    }

    /**
     * Depending on the handler this method may be executed on the UI thread
     */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        if (mAudioManager != null && mListener != null) {
            int maxVolume = mAudioManager.getStreamMaxVolume(mAudioStreamType);
            int currentVolume = mAudioManager.getStreamVolume(mAudioStreamType);
            if (currentVolume != mLastVolume) {
                mLastVolume = currentVolume;
                mListener.onAudioVolumeChanged(currentVolume, maxVolume);
            }
        }
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }
}