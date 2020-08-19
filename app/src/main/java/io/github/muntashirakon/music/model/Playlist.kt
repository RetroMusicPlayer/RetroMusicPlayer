package io.github.muntashirakon.music.model

import android.content.Context
import android.os.Parcelable
import io.github.muntashirakon.music.repository.RealPlaylistRepository
import io.github.muntashirakon.music.util.MusicUtil
import kotlinx.android.parcel.Parcelize
import org.koin.core.KoinComponent
import org.koin.core.get

@Parcelize
open class Playlist(
    val id: Int = -1,
    val name: String = ""
) : Parcelable, KoinComponent {

    // this default implementation covers static playlists
    fun getSongs(): List<Song> {
        return RealPlaylistRepository(get()).playlistSongs(id)
    }

    open fun getInfoString(context: Context): String {
        val songCount = getSongs().size
        val songCountString = MusicUtil.getSongCountString(context, songCount)
        return MusicUtil.buildInfoString(
            songCountString,
            ""
        )
    }
}