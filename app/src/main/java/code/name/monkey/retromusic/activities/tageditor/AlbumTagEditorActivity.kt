package code.name.monkey.retromusic.activities.tageditor

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.widget.Toast
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.appHandleColor
import code.name.monkey.retromusic.extensions.applyToolbar
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTranscoder
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.loaders.AlbumLoader
import code.name.monkey.retromusic.rest.LastFMRestClient
import code.name.monkey.retromusic.util.ImageUtil
import code.name.monkey.retromusic.util.RetroColorUtil.generatePalette
import code.name.monkey.retromusic.util.RetroColorUtil.getColor
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_album_tag_editor.albumArtistContainer
import kotlinx.android.synthetic.main.activity_album_tag_editor.albumArtistText
import kotlinx.android.synthetic.main.activity_album_tag_editor.albumText
import kotlinx.android.synthetic.main.activity_album_tag_editor.albumTitleContainer
import kotlinx.android.synthetic.main.activity_album_tag_editor.genreContainer
import kotlinx.android.synthetic.main.activity_album_tag_editor.genreTitle
import kotlinx.android.synthetic.main.activity_album_tag_editor.imageContainer
import kotlinx.android.synthetic.main.activity_album_tag_editor.toolbar
import kotlinx.android.synthetic.main.activity_album_tag_editor.yearContainer
import kotlinx.android.synthetic.main.activity_album_tag_editor.yearTitle
import org.jaudiotagger.tag.FieldKey
import java.util.ArrayList
import java.util.EnumMap

class AlbumTagEditorActivity : AbsTagEditorActivity(), TextWatcher {
    override val contentViewLayout: Int
        get() = R.layout.activity_album_tag_editor

    private fun windowEnterTransition() {
        val slide = Slide()
        slide.excludeTarget(R.id.appBarLayout, true)
        slide.excludeTarget(R.id.status_bar, true)
        slide.excludeTarget(android.R.id.statusBarBackground, true)
        slide.excludeTarget(android.R.id.navigationBarBackground, true)

        window.enterTransition = slide
    }

    override fun loadImageFromFile(selectedFileUri: Uri?) {

        Glide.with(this@AlbumTagEditorActivity).load(selectedFileUri).asBitmap()
            .transcode(BitmapPaletteTranscoder(this), BitmapPaletteWrapper::class.java)
            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            .into(object : SimpleTarget<BitmapPaletteWrapper>() {
                override fun onResourceReady(
                    resource: BitmapPaletteWrapper?,
                    glideAnimation: GlideAnimation<in BitmapPaletteWrapper>?
                ) {
                    getColor(resource?.palette, Color.TRANSPARENT)
                    albumArtBitmap = resource?.bitmap?.let { ImageUtil.resizeBitmap(it, 2048) }
                    setImageBitmap(
                        albumArtBitmap,
                        getColor(
                            resource?.palette,
                            ATHUtil.resolveColor(this@AlbumTagEditorActivity, R.attr.defaultFooterColor)
                        )
                    )
                    deleteAlbumArt = false
                    dataChanged()
                    setResult(Activity.RESULT_OK)
                }

                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                    super.onLoadFailed(e, errorDrawable)
                    Toast.makeText(this@AlbumTagEditorActivity, e.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private var albumArtBitmap: Bitmap? = null
    private var deleteAlbumArt: Boolean = false
    private var lastFMRestClient: LastFMRestClient? = null
    private val disposable = CompositeDisposable()

    private fun setupToolbar() {
        applyToolbar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        window.sharedElementsUseOverlay = true
        lastFMRestClient = LastFMRestClient(this)
        imageContainer?.transitionName = "${getString(R.string.transition_album_art)}_$id"
        windowEnterTransition()
        setUpViews()
        setupToolbar()
    }

    private fun setUpViews() {
        fillViewsWithFileTags()

        MaterialUtil.setTint(yearContainer, false)
        MaterialUtil.setTint(genreContainer, false)
        MaterialUtil.setTint(albumTitleContainer, false)
        MaterialUtil.setTint(albumArtistContainer, false)

        albumText.appHandleColor().addTextChangedListener(this)
        albumArtistText.appHandleColor().addTextChangedListener(this)
        genreTitle.appHandleColor().addTextChangedListener(this)
        yearTitle.appHandleColor().addTextChangedListener(this)
    }

    private fun fillViewsWithFileTags() {
        albumText.setText(albumTitle)
        albumArtistText.setText(albumArtistName)
        genreTitle.setText(genreName)
        yearTitle.setText(songYear)
    }

    override fun loadCurrentImage() {
        val bitmap = albumArt
        setImageBitmap(
            bitmap,
            getColor(
                generatePalette(bitmap),
                ATHUtil.resolveColor(this, R.attr.defaultFooterColor)
            )
        )
        deleteAlbumArt = false
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    private fun toastLoadingFailed() {
        Toast.makeText(
            this@AlbumTagEditorActivity,
            R.string.could_not_download_album_cover,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun searchImageOnWeb() {
        searchWebFor(albumText.text.toString(), albumArtistText.text.toString())
    }

    override fun deleteImage() {
        setImageBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.default_album_art),
            ATHUtil.resolveColor(this, R.attr.defaultFooterColor)
        )
        deleteAlbumArt = true
        dataChanged()
    }

    override fun save() {
        val fieldKeyValueMap = EnumMap<FieldKey, String>(FieldKey::class.java)
        fieldKeyValueMap[FieldKey.ALBUM] = albumText.text.toString()
        //android seems not to recognize album_artist field so we additionally write the normal artist field
        fieldKeyValueMap[FieldKey.ARTIST] = albumArtistText.text.toString()
        fieldKeyValueMap[FieldKey.ALBUM_ARTIST] = albumArtistText.text.toString()
        fieldKeyValueMap[FieldKey.GENRE] = genreTitle.text.toString()
        fieldKeyValueMap[FieldKey.YEAR] = yearTitle.text.toString()

        writeValuesToFiles(fieldKeyValueMap,
            if (deleteAlbumArt) ArtworkInfo(id, null)
            else if (albumArtBitmap == null) null else ArtworkInfo(id, albumArtBitmap!!)
        )
    }

    override fun getSongPaths(): List<String> {
        val songs = AlbumLoader.getAlbum(this, id).songs
        val paths = ArrayList<String>(songs!!.size)
        for (song in songs) {
            paths.add(song.data)
        }
        return paths
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable) {
        dataChanged()
    }

    override fun setColors(color: Int) {
        super.setColors(color)
        saveFab.backgroundTintList = ColorStateList.valueOf(color)
    }

    companion object {

        val TAG: String = AlbumTagEditorActivity::class.java.simpleName
    }
}
