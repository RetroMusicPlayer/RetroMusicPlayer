package code.name.monkey.retromusic.activities

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.*
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.*
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.activities.tageditor.WriteTagsAsyncTask
import code.name.monkey.retromusic.fragments.base.AbsMusicServiceFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.lyrics.LrcHelper
import code.name.monkey.retromusic.lyrics.LrcView
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.model.lyrics.Lyrics
import code.name.monkey.retromusic.util.LyricUtil
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.input.getInputLayout
import com.afollestad.materialdialogs.input.input
import kotlinx.android.synthetic.main.activity_lyrics.*
import kotlinx.android.synthetic.main.fragment_lyrics.*
import kotlinx.android.synthetic.main.fragment_synced.*
import org.jaudiotagger.tag.FieldKey
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class LyricsActivity : AbsMusicServiceActivity(), View.OnClickListener, ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            ViewPager.SCROLL_STATE_IDLE ->
                fab.show()
            ViewPager.SCROLL_STATE_DRAGGING,
            ViewPager.SCROLL_STATE_SETTLING ->
                fab.hide()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        PreferenceUtil.getInstance(this).lyricsOptions = position
        if (position == 0) fab.text = getString(R.string.synced_lyrics)
        else if (position == 1) fab.text = getString(R.string.lyrics)
    }

    override fun onClick(v: View?) {
        when (viewPager.currentItem) {
            0 -> showSyncedLyrics()
            1 -> showLyricsSaveDialog()
        }
    }

    private lateinit var song: Song
    private var lyricsString: String? = null

    private val googleSearchLrcUrl: String
        get() {
            var baseUrl = "http://www.google.com/search?"
            var query = song.title + "+" + song.artistName
            query = "q=" + query.replace(" ", "+") + " .lrc"
            baseUrl += query
            return baseUrl
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)

        setStatusbarColorAuto()
        setTaskDescriptionColorAuto()
        setNavigationBarColorPrimary()

        val primaryColor = ATHUtil.resolveColor(this, R.attr.colorPrimary)
        appBarLayout.setBackgroundColor(primaryColor)
        toolbar.apply {
            setBackgroundColor(primaryColor)
            navigationIcon = TintHelper.createTintedDrawable(ContextCompat.getDrawable(this@LyricsActivity, R.drawable.ic_keyboard_backspace_black_24dp), ThemeStore.textColorSecondary(this@LyricsActivity))
            setSupportActionBar(toolbar)
        }


        fab.backgroundTintList = ColorStateList.valueOf(ThemeStore.accentColor(this))
        ColorStateList.valueOf(MaterialValueHelper.getPrimaryTextColor(this, ColorUtil.isColorLight(ThemeStore.accentColor(this)))).apply {
            fab.setTextColor(this)
            fab.iconTint = this
        }
        setupWakelock()

        viewPager.apply {
            adapter = PagerAdapter(supportFragmentManager)
            currentItem = PreferenceUtil.getInstance(this@LyricsActivity).lyricsOptions
            addOnPageChangeListener(this@LyricsActivity)
        }

        tabs.apply {
            setupWithViewPager(viewPager)
            setSelectedTabIndicator(TintHelper.createTintedDrawable(ContextCompat.getDrawable(this@LyricsActivity, R.drawable.tab_indicator), ThemeStore.accentColor(this@LyricsActivity)))
            setTabTextColors(ThemeStore.textColorSecondary(this@LyricsActivity), ThemeStore.accentColor(this@LyricsActivity))
            setSelectedTabIndicatorColor(ThemeStore.accentColor(context))
        }
        fab.setOnClickListener(this)
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        song = MusicPlayerRemote.currentSong
        toolbar.title = song.title
        toolbar.subtitle = song.artistName
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        song = MusicPlayerRemote.currentSong
        toolbar.title = song.title
        toolbar.subtitle = song.artistName
    }

    private fun setupWakelock() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSyncedLyrics() {
        var content = ""
        try {
            content = LyricUtil.getStringFromFile(song.title, song.artistName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val materialDialog = MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.add_time_framed_lryics)
            negativeButton(R.string.action_search) { RetroUtil.openUrl(this@LyricsActivity, googleSearchLrcUrl) }
            input(hint = getString(R.string.paste_lyrics_here),
                    prefill = content,
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE) { _, input ->
                LyricUtil.writeLrcToLoc(song.title, song.artistName, input.toString())
            }
            positiveButton(android.R.string.ok) {
                updateSong()
            }
        }
        MaterialUtil.setTint(materialDialog.getInputLayout(), false)
    }

    private fun updateSong() {
        val page = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.currentItem)
        if (viewPager.currentItem == 0 && page != null) {
            (page as BaseLyricsFragment).upDateSong()
        }
    }

    private fun showLyricsSaveDialog() {
        val content: String = if (lyricsString == null) {
            ""
        } else {
            lyricsString!!
        }

        val materialDialog = MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.add_lyrics)
            negativeButton(R.string.action_search) { RetroUtil.openUrl(this@LyricsActivity, getGoogleSearchUrl()) }
            input(hint = getString(R.string.paste_lyrics_here),
                    prefill = content,
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE) { _, input ->
                val fieldKeyValueMap = EnumMap<FieldKey, String>(FieldKey::class.java)
                fieldKeyValueMap[FieldKey.LYRICS] = input.toString()
                WriteTagsAsyncTask(this@LyricsActivity).execute(WriteTagsAsyncTask.LoadingInfo(getSongPaths(song), fieldKeyValueMap, null))
            }
            positiveButton(android.R.string.ok) {
                updateSong()
            }
        }
        MaterialUtil.setTint(materialDialog.getInputLayout(), false)
    }

    private fun getSongPaths(song: Song): ArrayList<String> {
        val paths = ArrayList<String>(1)
        paths.add(song.data)
        return paths
    }

    private fun getGoogleSearchUrl(): String {
        var baseUrl = "http://www.google.com/search?"
        var query = song.title + "+" + song.artistName
        query = "q=" + query.replace(" ", "+") + " lyrics"
        baseUrl += query
        return baseUrl
    }

    class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        class Tabs(@StringRes val title: Int,
                   val fragment: Fragment)

        private var tabs = ArrayList<Tabs>()

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                tabs.add(Tabs(R.string.synced_lyrics, SyncedLyricsFragment()))
            }
            tabs.add(Tabs(R.string.normal_lyrics, OfflineLyricsFragment()))
        }

        override fun getItem(position: Int): Fragment {
            return tabs[position].fragment
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return App.getContext().getString(tabs[position].title)
        }

        override fun getCount(): Int {
            return tabs.size
        }

    }

    abstract class BaseLyricsFragment : AbsMusicServiceFragment() {
        abstract fun upDateSong()

        override fun onPlayingMetaChanged() {
            super.onPlayingMetaChanged()
            upDateSong()
        }

        override fun onServiceConnected() {
            super.onServiceConnected()
            upDateSong()
        }

    }

    class OfflineLyricsFragment : BaseLyricsFragment() {
        override fun upDateSong() {
            loadSongLyrics()
        }

        private var updateLyricsAsyncTask: AsyncTask<*, *, *>? = null
        private var lyrics: Lyrics? = null

        @SuppressLint("StaticFieldLeak")
        private fun loadSongLyrics() {
            if (updateLyricsAsyncTask != null) {
                updateLyricsAsyncTask!!.cancel(false)
            }
            val song = MusicPlayerRemote.currentSong
            updateLyricsAsyncTask = object : AsyncTask<Void?, Void?, Lyrics?>() {
                override fun doInBackground(vararg params: Void?): Lyrics? {
                    val data = MusicUtil.getLyrics(song)
                    return if (TextUtils.isEmpty(data)) {
                        null
                    } else Lyrics.parse(song, data!!)
                }

                override fun onPreExecute() {
                    super.onPreExecute()
                    lyrics = null
                }

                override fun onPostExecute(l: Lyrics?) {
                    lyrics = l
                    offlineLyrics?.visibility = View.VISIBLE
                    if (l == null) {
                        offlineLyrics?.setText(R.string.no_lyrics_found)
                        return
                    }
                    (activity as LyricsActivity).lyricsString = l.text
                    offlineLyrics?.text = l.text
                }

                override fun onCancelled(s: Lyrics?) {
                    onPostExecute(null)
                }
            }.execute()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            if (updateLyricsAsyncTask != null && !updateLyricsAsyncTask!!.isCancelled) {
                updateLyricsAsyncTask!!.cancel(true)
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_lyrics, container, false)
        }
    }

    class SyncedLyricsFragment : BaseLyricsFragment(), MusicProgressViewUpdateHelper.Callback {
        override fun upDateSong() {
            loadLRCLyrics()
        }

        private lateinit var updateHelper: MusicProgressViewUpdateHelper
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_synced, container, false)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            updateHelper = MusicProgressViewUpdateHelper(this, 500, 1000)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setupLyricsView()
        }

        private fun setupLyricsView() {
            lyricsView.apply {
                val context = activity!!
                setCurrentPlayLineColor(ThemeStore.accentColor(context))
                setIndicatorTextColor(ThemeStore.accentColor(context))
                setCurrentIndicateLineTextColor(ThemeStore.textColorPrimary(context))
                setNoLrcTextColor(ThemeStore.textColorPrimary(context))
                setOnPlayIndicatorLineListener(object : LrcView.OnPlayIndicatorLineListener {
                    override fun onPlay(time: Long, content: String) {
                        MusicPlayerRemote.seekTo(time.toInt())
                    }
                })
            }
        }

        override fun onResume() {
            super.onResume()
            updateHelper.start()
        }

        override fun onPause() {
            super.onPause()
            updateHelper.stop()
        }

        override fun onUpdateProgressViews(progress: Int, total: Int) {
            lyricsView.updateTime(progress.toLong())
        }

        private fun loadLRCLyrics() {
            val song = MusicPlayerRemote.currentSong
            if (LyricUtil.isLrcFileExist(song.title, song.artistName)) {
                showLyricsLocal(LyricUtil.getLocalLyricFile(song.title, song.artistName))
            }
        }

        private fun showLyricsLocal(file: File?) {
            if (file != null) {
                lyricsView.setLrcData(LrcHelper.parseLrcFromFile(file))
            }
        }
    }
}