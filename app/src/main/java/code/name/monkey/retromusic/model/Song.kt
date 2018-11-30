package code.name.monkey.retromusic.model

import android.os.Parcel
import android.os.Parcelable


open class Song : Parcelable {
    val id: Int
    val title: String?
    val trackNumber: Int
    val year: Int
    val duration: Long
    val data: String?
    val dateModified: Long
    val albumId: Int
    val albumName: String?
    val artistId: Int
    val artistName: String?

    constructor(id: Int, title: String, trackNumber: Int, year: Int, duration: Long, data: String, dateModified: Long, albumId: Int, albumName: String, artistId: Int, artistName: String) {
        this.id = id
        this.title = title
        this.trackNumber = trackNumber
        this.year = year
        this.duration = duration
        this.data = data
        this.dateModified = dateModified
        this.albumId = albumId
        this.albumName = albumName
        this.artistId = artistId
        this.artistName = artistName
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readInt()
        this.title = `in`.readString()
        this.trackNumber = `in`.readInt()
        this.year = `in`.readInt()
        this.duration = `in`.readLong()
        this.data = `in`.readString()
        this.dateModified = `in`.readLong()
        this.albumId = `in`.readInt()
        this.albumName = `in`.readString()
        this.artistId = `in`.readInt()
        this.artistName = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.id)
        dest.writeString(this.title)
        dest.writeInt(this.trackNumber)
        dest.writeInt(this.year)
        dest.writeLong(this.duration)
        dest.writeString(this.data)
        dest.writeLong(this.dateModified)
        dest.writeInt(this.albumId)
        dest.writeString(this.albumName)
        dest.writeInt(this.artistId)
        dest.writeString(this.artistName)
    }

    companion object {
        val EMPTY_SONG = Song(-1, "", -1, -1, -1, "", -1, -1, "", -1, "")
        @JvmField
        val CREATOR: Parcelable.Creator<Song> = object : Parcelable.Creator<Song> {
            override fun createFromParcel(source: Parcel): Song {
                return Song(source)
            }

            override fun newArray(size: Int): Array<Song> {
                return emptyArray()
            }
        }
    }
}
