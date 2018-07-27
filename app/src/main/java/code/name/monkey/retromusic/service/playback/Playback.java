package code.name.monkey.retromusic.service.playback;

import android.support.annotation.Nullable;


public interface Playback {

    boolean setDataSource(String path);

    void setNextDataSource(@Nullable String path);

    void setCallbacks(PlaybackCallbacks callbacks);

    boolean isInitialized();

    boolean start();

    void stop();

    void release();

    boolean pause();

    boolean isPlaying();

    int duration();

    int position();

    int seek(int whereto);

    boolean setVolume(float vol);

    boolean setAudioSessionId(int sessionId);

    int getAudioSessionId();

    interface PlaybackCallbacks {
        void onTrackWentToNext();

        void onTrackEnded();
    }
}
