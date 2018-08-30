package code.name.monkey.retromusic.volume;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import androidx.annotation.NonNull;

public class AudioVolumeObserver {

    private final Context mContext;
    private final AudioManager mAudioManager;
    private AudioVolumeContentObserver mAudioVolumeContentObserver;

    public AudioVolumeObserver(@NonNull Context context) {
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void register(int audioStreamType, @NonNull OnAudioVolumeChangedListener listener) {

        Handler handler = new Handler();
        // with this handler AudioVolumeContentObserver#onChange() 
        //   will be executed in the main thread
        // To execute in another thread you can use a Looper
        // +info: https://stackoverflow.com/a/35261443/904907

        mAudioVolumeContentObserver = new AudioVolumeContentObserver(
                handler,
                mAudioManager,
                audioStreamType,
                listener);

        mContext.getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI,
                true,
                mAudioVolumeContentObserver);
    }

    public void unregister() {
        if (mAudioVolumeContentObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mAudioVolumeContentObserver);
            mAudioVolumeContentObserver = null;
        }
    }
}