package code.name.monkey.retromusic.deezer

import com.google.gson.annotations.SerializedName

data class Data(
        val id: String,
        val link: String,
        val name: String,
        @SerializedName("nb_album")
        val nbAlbum: Int,
        @SerializedName("nb_fan")
        val nbFan: Int,
        val picture: String,
        @SerializedName("picture_big")
        val pictureBig: String,
        @SerializedName("picture_medium")
        val pictureMedium: String,
        @SerializedName("picture_small")
        val pictureSmall: String,
        @SerializedName("picture_xl")
        val pictureXl: String,
        val radio: Boolean,
        val tracklist: String,
        val type: String
)

data class DeezerResponse(
        val data: List<Data>,
        val next: String,
        val total: Int
)