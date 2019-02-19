package code.name.monkey.retromusic.preferences

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.ui.fragments.AlbumCoverStyle
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide


class AlbumCoverStylePreference : DialogPreference {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val mLayoutRes = R.layout.preference_dialog_now_playing_screen

    override fun getDialogLayoutResource(): Int {
        return mLayoutRes;
    }
}

class AlbumCoverStylePreferenceDialog : PreferenceDialogFragmentCompat(), ViewPager.OnPageChangeListener {
    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val nowPlayingScreen = AlbumCoverStyle.values()[viewPagerPosition]
            PreferenceUtil.getInstance().albumCoverStyle = nowPlayingScreen
        }
    }

    private var viewPagerPosition: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        @SuppressLint("InflateParams") val view = LayoutInflater.from(activity).inflate(R.layout.preference_dialog_now_playing_screen, null)
        val viewPager = view.findViewById<ViewPager>(R.id.now_playing_screen_view_pager)
        viewPager.adapter = AlbumCoverStyleAdapter(activity!!)
        viewPager.addOnPageChangeListener(this)
        viewPager.pageMargin = ViewUtil.convertDpToPixel(32f, resources).toInt()
        viewPager.currentItem = PreferenceUtil.getInstance().albumCoverStyle.ordinal

        return MaterialDialog(activity!!).show {
            title(R.string.pref_title_album_cover_style)
            positiveButton(android.R.string.ok)
            negativeButton(android.R.string.cancel)
            customView(view = view, scrollable = false, noVerticalPadding = false)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        this.viewPagerPosition = position
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    private class AlbumCoverStyleAdapter internal constructor(private val context: Context) : PagerAdapter() {

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val albumCoverStyle = AlbumCoverStyle.values()[position]

            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.preference_now_playing_screen_item, collection, false) as ViewGroup
            collection.addView(layout)

            val image = layout.findViewById<ImageView>(R.id.image)
            val title = layout.findViewById<TextView>(R.id.title)
            Glide.with(context).load(albumCoverStyle.drawableResId).into(image)
            title.setText(albumCoverStyle.titleRes)

            return layout
        }

        override fun destroyItem(collection: ViewGroup,
                                 position: Int,
                                 view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return AlbumCoverStyle.values().size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return context.getString(AlbumCoverStyle.values()[position].titleRes)
        }
    }

    companion object {
        val TAG: String = AlbumCoverStylePreferenceDialog::class.java.simpleName

        private const val EXTRA_KEY = "key"

        fun newInstance(key: String): AlbumCoverStylePreferenceDialog {
            val args = Bundle()
            args.putString(EXTRA_KEY, key)
            val fragment = AlbumCoverStylePreferenceDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
