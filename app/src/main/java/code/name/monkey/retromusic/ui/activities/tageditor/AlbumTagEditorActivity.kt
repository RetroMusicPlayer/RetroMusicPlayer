package code.name.monkey.retromusic.ui.activities.tageditor

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroSimpleTarget
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.loaders.AlbumLoader
import code.name.monkey.retromusic.rest.LastFMRestClient
import code.name.monkey.retromusic.rest.model.LastFmAlbum
import code.name.monkey.retromusic.util.ImageUtil
import code.name.monkey.retromusic.util.LastFMUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import code.name.monkey.retromusic.util.RetroColorUtil.generatePalette
import code.name.monkey.retromusic.util.RetroColorUtil.getColor
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.Transition
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_album_tag_editor.*
import org.jaudiotagger.tag.FieldKey
import java.util.*


class AlbumTagEditorActivity : AbsTagEditorActivity(), TextWatcher {
    override val contentViewLayout: Int
        get() = R.layout.activity_album_tag_editor

    override fun loadImageFromFile(selectedFile: Uri?) {
        GlideApp.with(this).`as`(BitmapPaletteWrapper::class.java)
                .load(selectedFile)
                .transition(GenericTransitionOptions<BitmapPaletteWrapper>().transition(android.R.anim.fade_in))
                .apply(RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(object : RetroSimpleTarget<BitmapPaletteWrapper>() {
                    override fun onResourceReady(resource: BitmapPaletteWrapper, transition: Transition<in BitmapPaletteWrapper>?) {
                        RetroColorUtil.getColor(resource.palette, Color.TRANSPARENT)
                        albumArtBitmap = ImageUtil.resizeBitmap(resource.bitmap, 2048)
                        setImageBitmap(albumArtBitmap, RetroColorUtil.getColor(resource.palette, ATHUtil.resolveColor(this@AlbumTagEditorActivity, R.attr.defaultFooterColor)))
                        deleteAlbumArt = false
                        dataChanged()
                        setResult(Activity.RESULT_OK)
                    }
                })
    }

    private var albumArtBitmap: Bitmap? = null
    private var deleteAlbumArt: Boolean = false
    private var lastFMRestClient: LastFMRestClient? = null
    private val disposable = CompositeDisposable()

    private fun setupToolbar() {
        bannerTitle.setTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        ToolbarContentTintHelper.setToolbarContentColorBasedOnToolbarColor(this, toolbar, Color.TRANSPARENT)
        title = null
        setSupportActionBar(toolbar)
        TintHelper.setTintAuto(content, ThemeStore.primaryColor(this), true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        lastFMRestClient = LastFMRestClient(this)

        setUpViews()
        setupToolbar()
    }


    private fun setUpViews() {
        fillViewsWithFileTags()

        MaterialUtil.setTint(yearContainer, false)
        MaterialUtil.setTint(genreContainer, false)
        MaterialUtil.setTint(albumTitleContainer, false)
        MaterialUtil.setTint(albumArtistContainer, false)

        albumText.addTextChangedListener(this)
        albumArtistText.addTextChangedListener(this)
        genreTitle.addTextChangedListener(this)
        yearTitle.addTextChangedListener(this)
    }

    private fun fillViewsWithFileTags() {
        albumText.setText(albumTitle)
        albumArtistText.setText(albumArtistName)
        genreTitle.setText(genreName)
        yearTitle.setText(songYear)
    }

    override fun loadCurrentImage() {
        val bitmap = albumArt
        setImageBitmap(bitmap, getColor(generatePalette(bitmap), ATHUtil.resolveColor(this, R.attr.defaultFooterColor)))
        deleteAlbumArt = false
    }

    override fun getImageFromLastFM() {
        val albumTitleStr = albumText.text.toString()
        val albumArtistNameStr = albumArtistText.text.toString()
        if (albumArtistNameStr.trim { it <= ' ' } == "" || albumTitleStr.trim { it <= ' ' } == "") {
            Toast.makeText(this, resources.getString(R.string.album_or_artist_empty), Toast.LENGTH_SHORT).show()
            return
        }

        disposable.add(lastFMRestClient!!.apiService
                .getAlbumInfo(albumTitleStr, albumArtistNameStr, null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe { this.extractDetails(it) })
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    private fun extractDetails(lastFmAlbum: LastFmAlbum) {
        if (lastFmAlbum.album != null) {

            val url = LastFMUtil.getLargestAlbumImageUrl(lastFmAlbum.album.image)

            if (!TextUtils.isEmpty(url) && url.trim { it <= ' ' }.isNotEmpty()) {
                GlideApp.with(this)
                        .`as`(BitmapPaletteWrapper::class.java)
                        .load(url)
                        .apply(RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .error(R.drawable.default_album_art))
                        .into(object : RetroSimpleTarget<BitmapPaletteWrapper>() {
                            override fun onResourceReady(resource: BitmapPaletteWrapper, transition: Transition<in BitmapPaletteWrapper>?) {
                                albumArtBitmap = ImageUtil.resizeBitmap(resource.bitmap, 2048)
                                setImageBitmap(albumArtBitmap, getColor(resource.palette,
                                        ContextCompat.getColor(this@AlbumTagEditorActivity, R.color.md_grey_500)))
                                deleteAlbumArt = false
                                dataChanged()
                                setResult(Activity.RESULT_OK)
                            }
                        })
                return
            }
            if (lastFmAlbum.album.tags.tag.size > 0) {
                genreTitle.setText(lastFmAlbum.album.tags.tag[0].name)
            }

        }
        toastLoadingFailed()
    }

    private fun toastLoadingFailed() {
        Toast.makeText(this@AlbumTagEditorActivity, R.string.could_not_download_album_cover, Toast.LENGTH_SHORT).show()
    }

    override fun searchImageOnWeb() {
        searchWebFor(albumText.text.toString(), albumArtistText.text.toString())
    }

    override fun deleteImage() {
        setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.default_album_art), ATHUtil.resolveColor(this, R.attr.defaultFooterColor))
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

        writeValuesToFiles(fieldKeyValueMap, if (deleteAlbumArt) AbsTagEditorActivity.ArtworkInfo(id, null)
        else if (albumArtBitmap == null) null else AbsTagEditorActivity.ArtworkInfo(id, albumArtBitmap!!))
    }

    override fun getSongPaths(): List<String> {
        val songs = AlbumLoader.getAlbum(this, id).blockingFirst().songs
        val paths = ArrayList<String>(songs!!.size)
        for (song in songs) {
            paths.add(song.data!!)
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
