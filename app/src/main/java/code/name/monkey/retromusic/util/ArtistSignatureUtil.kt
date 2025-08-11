package code.name.monkey.retromusic.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.bumptech.glide.signature.ObjectKey

/** @author Karim Abou Zeid (kabouzeid)
 */
class ArtistSignatureUtil private constructor(context: Context) {
    private val mPreferences: SharedPreferences =
        context.getSharedPreferences(ARTIST_SIGNATURE_PREFS, Context.MODE_PRIVATE)

    fun updateArtistSignature(artistName: String?) {
        mPreferences.edit { putLong(artistName, System.currentTimeMillis()) }
    }

    private fun getArtistSignatureRaw(artistName: String?): Long {
        return mPreferences.getLong(artistName, 0)
    }

    fun getArtistSignature(artistName: String?): ObjectKey {
        return ObjectKey(getArtistSignatureRaw(artistName).toString())
    }

    companion object {
        private const val ARTIST_SIGNATURE_PREFS = "artist_signatures"
        private var INSTANCE: ArtistSignatureUtil? = null
        fun getInstance(context: Context): ArtistSignatureUtil {
            if (INSTANCE == null) {
                INSTANCE = ArtistSignatureUtil(context.applicationContext)
            }
            return INSTANCE!!
        }
    }

}