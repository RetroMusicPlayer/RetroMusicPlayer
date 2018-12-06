package code.name.monkey.retromusic.ui.activities

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.model.lyrics.Lyrics
import code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.ui.activities.tageditor.WriteTagsAsyncTask
import code.name.monkey.retromusic.util.LyricUtil
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lyrics.*
import org.jaudiotagger.tag.FieldKey
import java.io.File
import java.util.*

class LyricsActivity : AbsMusicServiceActivity(), MusicProgressViewUpdateHelper.Callback, View.OnClickListener {

    private var updateHelper: MusicProgressViewUpdateHelper? = null
    private var updateLyricsAsyncTask: AsyncTask<*, *, *>? = null
    private var disposable: CompositeDisposable? = null
    private var song: Song? = null
    private var lyrics: Lyrics? = null

    private val googleSearchLrcUrl: String
        get() {
            var baseUrl = "http://www.google.com/search?"
            var query = song!!.title + "+" + song!!.artistName
            query = "q=" + query.replace(" ", "+") + " .lrc"
            baseUrl += query
            return baseUrl
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        //setDrawUnderNavigationBar();
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lyrics)


        setTaskDescriptionColorAuto()
        setNavigationbarColorAuto()

        container.isFit = !PreferenceUtil.getInstance().fullScreenMode

        setSupportActionBar(bottomAppBar)
        Objects.requireNonNull<Drawable>(bottomAppBar!!.navigationIcon)
                .setColorFilter(ThemeStore.textColorPrimary(this), PorterDuff.Mode.SRC_IN)
        bottomAppBar!!.backgroundTint = ColorStateList.valueOf(ThemeStore.primaryColor(this))

        TintHelper.setTintAuto(fab, ThemeStore.accentColor(this), true)

        updateHelper = MusicProgressViewUpdateHelper(this, 500, 1000)

        setupLyricsView()
        setupWakelock()
        loadLrcFile()

        actions.setOnCheckedChangeListener { _, checkedId -> selectLyricsTye(checkedId) }
        actions.check(PreferenceUtil.getInstance().lastLyricsType)


        fab.setOnClickListener(this)
    }

    private fun selectLyricsTye(group: Int) {
        PreferenceUtil.getInstance().lastLyricsType = group
        val radioButton = actions.findViewById<RadioButton>(group)
        if (radioButton != null) {
            radioButton.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            //radioButton.setTextColor(ThemeStore.textColorPrimary(this));
        }

        offlineLyrics!!.visibility = View.GONE
        lyricsView.visibility = View.GONE

        when (group) {
            R.id.syncedLyrics -> {
                loadLRCLyrics()
                lyricsView!!.visibility = View.VISIBLE
            }
            R.id.normalLyrics -> {
                loadSongLyrics()
                offlineLyrics!!.visibility = View.VISIBLE
            }
            else -> {
                loadSongLyrics()
                offlineLyrics!!.visibility = View.VISIBLE
            }
        }
    }

    private fun loadLRCLyrics() {
        if (LyricUtil.isLrcFileExist(song!!.title, song!!.artistName)) {
            showLyricsLocal(LyricUtil.getLocalLyricFile(song!!.title, song!!.artistName))
        }
    }

    private fun setupWakelock() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setupLyricsView() {
        disposable = CompositeDisposable()

        lyricsView!!.apply {
            setOnPlayerClickListener { progress, _ -> MusicPlayerRemote.seekTo(progress.toInt()) }
            //lyricView.setHighLightTextColor(ThemeStore.accentColor(this));
            setDefaultColor(ContextCompat.getColor(this@LyricsActivity, R.color.md_grey_400))
            //lyricView.setTouchable(false);
            setHintColor(Color.WHITE)
        }
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        loadLrcFile()
    }

    override fun onResume() {
        super.onResume()
        updateHelper!!.start()
    }

    override fun onPause() {
        super.onPause()
        updateHelper!!.stop()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        loadLrcFile()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable!!.clear()

        if (updateLyricsAsyncTask != null && !updateLyricsAsyncTask!!.isCancelled) {
            updateLyricsAsyncTask!!.cancel(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        lyricsView!!.setCurrentTimeMillis(progress.toLong())
    }

    private fun loadLrcFile() {
        song = MusicPlayerRemote.currentSong
        bottomAppBar.title = song!!.title
        bottomAppBar.subtitle = song!!.artistName
        SongGlideRequest.Builder.from(Glide.with(this), song!!)
                .checkIgnoreMediaStore(this)
                .generatePalette(this)
                .build()
                .into(object : RetroMusicColoredTarget(findViewById(R.id.image)) {
                    override fun onColorReady(color: Int) {
                        if (PreferenceUtil.getInstance().adaptiveColor) {
                            //background.setBackgroundColor(color);
                        }
                    }
                })
    }

    private fun showLyricsLocal(file: File?) {
        if (file == null) {
            lyricsView!!.reset()
        } else {
            lyricsView!!.setLyricFile(file, "UTF-8")
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            android.R.id.home -> onBackPressed()
            R.id.fab -> when (actions.checkedRadioButtonId) {
                R.id.syncedLyrics -> showSyncedLyrics()
                R.id.normalLyrics -> showLyricsSaveDialog()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun loadSongLyrics() {
        if (updateLyricsAsyncTask != null) {
            updateLyricsAsyncTask!!.cancel(false)
        }
        val song = MusicPlayerRemote.currentSong
        updateLyricsAsyncTask = object : AsyncTask<Void, Void, Lyrics>() {
            override fun doInBackground(vararg params: Void): Lyrics? {
                val data = MusicUtil.getLyrics(song)
                return if (TextUtils.isEmpty(data)) {
                    null
                } else Lyrics.parse(song, data)
            }

            override fun onPreExecute() {
                super.onPreExecute()
                lyrics = null
            }

            override fun onPostExecute(l: Lyrics?) {
                lyrics = l
                offlineLyrics!!.visibility = View.VISIBLE
                if (l == null) {
                    offlineLyrics!!.setText(R.string.no_lyrics_found)
                    return
                }
                offlineLyrics!!.text = l.data
            }

            override fun onCancelled(s: Lyrics) {
                onPostExecute(null)
            }
        }.execute()
    }

    private fun showSyncedLyrics() {
        var content = ""
        try {
            content = LyricUtil.getStringFromFile(song!!.title, song!!.artistName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        MaterialDialog.Builder(this)
                .title("Add lyrics")
                .neutralText("Search")
                .content("Add time frame lyrics")
                .negativeText("Delete")
                .onNegative { _, _ ->
                    LyricUtil.deleteLrcFile(song!!.title, song!!.artistName)
                    loadLrcFile()
                }
                .onNeutral { _, _ -> RetroUtil.openUrl(this@LyricsActivity, googleSearchLrcUrl) }
                .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .input("Paste lyrics here", content) { _, input ->
                    LyricUtil.writeLrcToLoc(song!!.title, song!!.artistName, input.toString())
                    loadLrcFile()
                }.show()
    }

    private fun showLyricsSaveDialog() {
        val content: String = if (lyrics == null) {
            ""
        } else {
            lyrics!!.data
        }
        MaterialDialog.Builder(this)
                .title("Add lyrics")
                .neutralText("Search")
                .onNeutral { _, _ -> RetroUtil.openUrl(this@LyricsActivity, getGoogleSearchUrl(song!!.title, song!!.artistName)) }
                .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .input("Paste lyrics here", content) { _, input ->
                    val fieldKeyValueMap = EnumMap<FieldKey, String>(FieldKey::class.java)
                    fieldKeyValueMap[FieldKey.LYRICS] = input.toString()
                    WriteTagsAsyncTask(this@LyricsActivity).execute(WriteTagsAsyncTask.LoadingInfo(getSongPaths(song!!), fieldKeyValueMap, null))
                    loadLrcFile()
                }
                .show()
    }

    private fun getSongPaths(song: Song): ArrayList<String> {
        val paths = ArrayList<String>(1)
        paths.add(song.data!!)
        return paths
    }

    private fun getGoogleSearchUrl(title: String?, text: String?): String {
        var baseUrl = "http://www.google.com/search?"
        var query = "$title+$text"
        query = "q=" + query.replace(" ", "+") + " lyrics"
        baseUrl += query
        return baseUrl
    }
}
