package code.name.monkey.retromusic.preferences

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.ui.fragments.NowPlayingScreen
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide


class NowPlayingScreenPreferenceDialog : DialogFragment(), ViewPager.OnPageChangeListener, MaterialDialog.SingleButtonCallback {

    private var whichButtonClicked: DialogAction? = null
    private var viewPagerPosition: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        @SuppressLint("InflateParams") val view = LayoutInflater.from(activity)
                .inflate(R.layout.preference_dialog_now_playing_screen, null)
        val viewPager = view.findViewById<ViewPager>(R.id.now_playing_screen_view_pager)
        viewPager.adapter = NowPlayingScreenAdapter(activity!!)
        viewPager.addOnPageChangeListener(this)
        viewPager.pageMargin = ViewUtil.convertDpToPixel(32f, resources).toInt()
        viewPager.currentItem = PreferenceUtil.getInstance().nowPlayingScreen.ordinal

        return MaterialDialog.Builder(activity!!)
                .title(R.string.pref_title_now_playing_screen_appearance)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onAny(this)
                .customView(view, false)
                .build()
    }

    override fun onClick(dialog: MaterialDialog,
                         which: DialogAction) {
        whichButtonClicked = which
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (whichButtonClicked == DialogAction.POSITIVE) {
            val nowPlayingScreen = NowPlayingScreen.values()[viewPagerPosition]
            if (isNowPlayingThemes(nowPlayingScreen)) {
                val result = getString(nowPlayingScreen.titleRes) + " theme is Pro version feature."
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                NavigationUtil.goToProVersion(activity!!)
            } else {
                PreferenceUtil.getInstance().nowPlayingScreen = nowPlayingScreen
            }
        }
    }

    private fun isNowPlayingThemes(nowPlayingScreen: NowPlayingScreen): Boolean {

        if (nowPlayingScreen == NowPlayingScreen.BLUR_CARD) {
            PreferenceUtil.getInstance().resetCarouselEffect()
            PreferenceUtil.getInstance().resetCircularAlbumArt()
        }

        return (nowPlayingScreen == NowPlayingScreen.FULL ||
                nowPlayingScreen == NowPlayingScreen.CARD ||
                nowPlayingScreen == NowPlayingScreen.PLAIN ||
                nowPlayingScreen == NowPlayingScreen.BLUR ||
                nowPlayingScreen == NowPlayingScreen.COLOR ||
                nowPlayingScreen == NowPlayingScreen.SIMPLE ||
                nowPlayingScreen == NowPlayingScreen.BLUR_CARD ||
                nowPlayingScreen == NowPlayingScreen.ADAPTIVE)
                && !App.isProVersion

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        this.viewPagerPosition = position
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    private class NowPlayingScreenAdapter internal constructor(private val context: Context) : PagerAdapter() {

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val nowPlayingScreen = NowPlayingScreen.values()[position]

            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.preference_now_playing_screen_item, collection, false) as ViewGroup
            collection.addView(layout)

            val image = layout.findViewById<ImageView>(R.id.image)
            val title = layout.findViewById<TextView>(R.id.title)
            Glide.with(context).load(nowPlayingScreen.drawableResId).into(image)
            title.setText(nowPlayingScreen.titleRes)

            return layout
        }

        override fun destroyItem(collection: ViewGroup,
                                 position: Int,
                                 view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return NowPlayingScreen.values().size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return context.getString(NowPlayingScreen.values()[position].titleRes)
        }
    }

    companion object {
        val TAG: String = NowPlayingScreenPreferenceDialog::class.java.simpleName

        fun newInstance(): NowPlayingScreenPreferenceDialog {
            return NowPlayingScreenPreferenceDialog()
        }
    }
}
