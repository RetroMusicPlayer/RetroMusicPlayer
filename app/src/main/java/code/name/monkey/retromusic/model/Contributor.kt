package code.name.monkey.retromusic.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Contributor(
    @SerializedName("name") val name: String = "",
    @SerializedName("summary") val summary: String = "",
    @SerializedName("link") val link: String = "",
    @SerializedName("image") val image: String = ""
) : Parcelable