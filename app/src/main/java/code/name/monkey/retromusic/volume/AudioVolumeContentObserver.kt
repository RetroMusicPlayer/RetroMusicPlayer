package code.name.monkey.retromusic.volume

import android.database.ContentObserver
import android.media.AudioManager
import android.net.Uri
import android.os.Handler

class AudioVolumeContentObserver internal constructor(
    handler: Handler,
    audioManager: AudioManager,
    audioStreamType: Int,
    listener: OnAudioVolumeChangedListener
) : ContentObserver(handler) {
    private val mListener: OnAudioVolumeChangedListener?
    private val mAudioManager: AudioManager?
    private val mAudioStreamType: Int
    private var mLastVolume: Float

    /** Depending on the handler this method may be executed on the UI thread  */
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        if (mAudioManager != null && mListener != null) {
            val maxVolume = mAudioManager.getStreamMaxVolume(mAudioStreamType)
            val currentVolume = mAudioManager.getStreamVolume(mAudioStreamType)
            if (currentVolume.toFloat() != mLastVolume) {
                mLastVolume = currentVolume.toFloat()
                mListener.onAudioVolumeChanged(currentVolume, maxVolume)
            }
        }
    }

    init {
        mAudioManager = audioManager
        mAudioStreamType = audioStreamType
        mListener = listener
        mLastVolume = audioManager.getStreamVolume(mAudioStreamType).toFloat()
    }
}