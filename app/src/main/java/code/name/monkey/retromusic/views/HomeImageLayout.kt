package code.name.monkey.retromusic.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import code.name.monkey.retromusic.databinding.BannerImageLayoutBinding
import code.name.monkey.retromusic.databinding.UserImageLayoutBinding
import code.name.monkey.retromusic.util.PreferenceUtil

class HomeImageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var userImageBinding: UserImageLayoutBinding? = null
    private var bannerImageBinding: BannerImageLayoutBinding? = null

    init {
        if (isInEditMode || PreferenceUtil.isHomeBanner) {
            bannerImageBinding = BannerImageLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        } else {
            userImageBinding = UserImageLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        }
    }

    val userImage: ImageView
        get() = if (PreferenceUtil.isHomeBanner) {
            bannerImageBinding!!.userImage
        } else {
            userImageBinding!!.userImage
        }

    val bannerImage: ImageView?
        get() = if (PreferenceUtil.isHomeBanner) {
            bannerImageBinding!!.bannerImage
        } else {
            null
        }

    val titleWelcome : TextView
        get() = if (PreferenceUtil.isHomeBanner) {
            bannerImageBinding!!.titleWelcome
        } else {
            userImageBinding!!.titleWelcome
        }
}