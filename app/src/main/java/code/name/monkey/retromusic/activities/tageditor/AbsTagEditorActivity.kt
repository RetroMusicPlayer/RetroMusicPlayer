package code.name.monkey.retromusic.activities.tageditor

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.R.drawable
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.activities.saf.SAFGuideActivity
import code.name.monkey.retromusic.util.RetroUtil
import code.name.monkey.retromusic.util.SAFUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_album_tag_editor.*
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import java.util.*

abstract class AbsTagEditorActivity : AbsBaseActivity() {

    protected var id: Int = 0
        private set
    private var paletteColorPrimary: Int = 0
    private var isInNoImageMode: Boolean = false
    private var songPaths: List<String>? = null
    lateinit var saveFab: MaterialButton

    private var savedSongPaths: List<String>? = null
    private val currentSongPath: String? = null
    private var savedTags: Map<FieldKey, String>? = null
    private var savedArtworkInfo: ArtworkInfo? = null

    protected val show: MaterialDialog
        get() = MaterialDialog(this).show {
            title(R.string.update_image)
            listItems(items = items) { _, position, _ ->
                when (position) {
                    0 -> startImagePicker()
                    1 -> searchImageOnWeb()
                    2 -> deleteImage()
                }
            }
        }
    protected abstract val contentViewLayout: Int

    internal val albumArtist: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.ALBUM_ARTIST)
            } catch (ignored: Exception) {
                null
            }
        }

    protected val songTitle: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.TITLE)
            } catch (ignored: Exception) {
                null
            }
        }
    protected val composer: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.COMPOSER)
            } catch (ignored: Exception) {
                null
            }
        }

    protected val albumTitle: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.ALBUM)
            } catch (ignored: Exception) {
                null
            }
        }

    protected val artistName: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.ARTIST)
            } catch (ignored: Exception) {
                null
            }
        }

    protected val albumArtistName: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.ALBUM_ARTIST)
            } catch (ignored: Exception) {
                null
            }
        }

    protected val genreName: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.GENRE)
            } catch (ignored: Exception) {
                null
            }
        }

    protected val songYear: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.YEAR)
            } catch (ignored: Exception) {
                null
            }
        }

    protected val trackNumber: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.TRACK)
            } catch (ignored: Exception) {
                null
            }
        }

    protected val lyrics: String?
        get() {
            return try {
                getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.getFirst(FieldKey.LYRICS)
            } catch (ignored: Exception) {
                null
            }
        }

    protected val albumArt: Bitmap?
        get() {
            try {
                val artworkTag = getAudioFile(songPaths!![0]).tagOrCreateAndSetDefault.firstArtwork
                if (artworkTag != null) {
                    val artworkBinaryData = artworkTag.binaryData
                    return BitmapFactory.decodeByteArray(
                        artworkBinaryData,
                        0,
                        artworkBinaryData.size
                    )
                }
                return null
            } catch (ignored: Exception) {
                return null
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewLayout)

        saveFab = findViewById(R.id.saveTags)
        getIntentExtras()

        songPaths = getSongPaths()
        if (songPaths!!.isEmpty()) {
            finish()
            return
        }

        setUpViews()

        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
    }

    private fun setUpViews() {
        setUpScrollView()
        setUpFab()
        setUpImageView()
    }

    private fun setUpScrollView() {
        //observableScrollView.setScrollViewCallbacks(observableScrollViewCallbacks);
    }

    private lateinit var items: List<String>

    private fun setUpImageView() {
        loadCurrentImage()
        items = listOf(
            getString(code.name.monkey.retromusic.R.string.pick_from_local_storage),
            getString(code.name.monkey.retromusic.R.string.web_search),
            getString(code.name.monkey.retromusic.R.string.remove_cover)
        )
        editorImage?.setOnClickListener { show }
    }

    private fun startImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(code.name.monkey.retromusic.R.string.pick_from_local_storage)
            ), REQUEST_CODE_SELECT_IMAGE
        )
    }

    protected abstract fun loadCurrentImage()

    protected abstract fun searchImageOnWeb()

    protected abstract fun deleteImage()

    private fun setUpFab() {
        saveFab.backgroundTintList = ColorStateList.valueOf(ThemeStore.accentColor(this))
        ColorStateList.valueOf(
            MaterialValueHelper.getPrimaryTextColor(
                this,
                ColorUtil.isColorLight(
                    ThemeStore.accentColor(
                        this
                    )
                )
            )
        ).apply {
            saveFab.setTextColor(this)
            saveFab.iconTint = this
        }
        saveFab.apply {
            scaleX = 0f
            scaleY = 0f
            isEnabled = false
            setOnClickListener { save() }
            TintHelper.setTintAuto(this, ThemeStore.accentColor(this@AbsTagEditorActivity), true)
        }
    }

    protected abstract fun save()

    private fun getIntentExtras() {
        val intentExtras = intent.extras
        if (intentExtras != null) {
            id = intentExtras.getInt(EXTRA_ID)
        }
    }

    protected abstract fun getSongPaths(): List<String>

    protected fun searchWebFor(vararg keys: String) {
        val stringBuilder = StringBuilder()
        for (key in keys) {
            stringBuilder.append(key)
            stringBuilder.append(" ")
        }
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra(SearchManager.QUERY, stringBuilder.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun setNoImageMode() {
        isInNoImageMode = true
        imageContainer?.visibility = View.GONE
        editorImage?.visibility = View.GONE
        editorImage?.isEnabled = false

        setColors(
            intent.getIntExtra(
                EXTRA_PALETTE,
                ATHUtil.resolveColor(this, R.attr.colorPrimary)
            )
        )
    }

    protected fun dataChanged() {
        showFab()
    }

    private fun showFab() {
        saveFab.animate().setDuration(500).setInterpolator(OvershootInterpolator()).scaleX(1f)
            .scaleY(1f).start()
        saveFab.isEnabled = true
    }

    private fun hideFab() {
        saveFab.animate().setDuration(500).setInterpolator(OvershootInterpolator()).scaleX(0.0f)
            .scaleY(0.0f).start()
        saveFab.isEnabled = false
    }

    protected fun setImageBitmap(bitmap: Bitmap?, bgColor: Int) {
        if (bitmap == null) {
            editorImage.setImageResource(drawable.default_audio_art)
        } else {
            editorImage.setImageBitmap(bitmap)
        }
        setColors(bgColor)
    }

    protected open fun setColors(color: Int) {
        paletteColorPrimary = color
    }

    protected fun writeValuesToFiles(
        fieldKeyValueMap: Map<FieldKey, String>, artworkInfo: ArtworkInfo?
    ) {
        RetroUtil.hideSoftKeyboard(this)

        hideFab()

        savedSongPaths = getSongPaths()
        savedTags = fieldKeyValueMap
        savedArtworkInfo = artworkInfo

        if (!SAFUtil.isSAFRequired(savedSongPaths)) {
            writeTags(savedSongPaths)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (SAFUtil.isSDCardAccessGranted(this)) {
                    writeTags(savedSongPaths)
                } else {
                    startActivityForResult(
                        Intent(this, SAFGuideActivity::class.java),
                        SAFGuideActivity.REQUEST_CODE_SAF_GUIDE
                    )
                }
            }
        }
    }

    private fun writeTags(paths: List<String>?) {
        WriteTagsAsyncTask(this).execute(
            WriteTagsAsyncTask.LoadingInfo(
                paths,
                savedTags,
                savedArtworkInfo
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            REQUEST_CODE_SELECT_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                intent?.data?.let {
                    loadImageFromFile(it)
                }
            }
            SAFGuideActivity.REQUEST_CODE_SAF_GUIDE -> {
                SAFUtil.openTreePicker(this)
            }
            SAFUtil.REQUEST_SAF_PICK_TREE -> {
                if (resultCode == Activity.RESULT_OK) {
                    SAFUtil.saveTreeUri(this, intent)
                    writeTags(savedSongPaths)
                }
            }
            SAFUtil.REQUEST_SAF_PICK_FILE -> {
                if (resultCode == Activity.RESULT_OK) {
                    writeTags(Collections.singletonList(currentSongPath + SAFUtil.SEPARATOR + intent!!.dataString))
                }
            }
        }
    }

    protected abstract fun loadImageFromFile(selectedFile: Uri?)

    private fun getAudioFile(path: String): AudioFile {
        return try {
            AudioFileIO.read(File(path))
        } catch (e: Exception) {
            Log.e(TAG, "Could not read audio file $path", e)
            AudioFile()
        }
    }

    class ArtworkInfo constructor(val albumId: Int, val artwork: Bitmap?)

    companion object {

        const val EXTRA_ID = "extra_id"
        const val EXTRA_PALETTE = "extra_palette"
        private val TAG = AbsTagEditorActivity::class.java.simpleName
        private const val REQUEST_CODE_SELECT_IMAGE = 1000
    }

}
