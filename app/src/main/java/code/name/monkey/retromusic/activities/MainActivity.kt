package code.name.monkey.retromusic.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.*
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager
import code.name.monkey.retromusic.extensions.findNavController
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.helper.MusicPlayerRemote.openAndShuffleQueue
import code.name.monkey.retromusic.helper.MusicPlayerRemote.openQueue
import code.name.monkey.retromusic.helper.MusicPlayerRemote.playFromUri
import code.name.monkey.retromusic.helper.MusicPlayerRemote.shuffleMode
import code.name.monkey.retromusic.helper.SearchQueryHelper.getSongs
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.loaders.AlbumLoader.getAlbum
import code.name.monkey.retromusic.loaders.ArtistLoader.getArtist
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader.getPlaylistSongList
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.AppRater.appLaunched
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import com.afollestad.materialcab.MaterialCab
import com.afollestad.materialdialogs.color.ColorChooserDialog
import org.koin.android.ext.android.inject
import java.util.*

class MainActivity : AbsSlidingMusicPanelActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener, CabHolder,
    ColorChooserDialog.ColorCallback {
    companion object {
        const val TAG = "MainActivity"
        const val EXPAND_PANEL = "expand_panel"
        const val APP_UPDATE_REQUEST_CODE = 9002
    }


    private val libraryViewModel: LibraryViewModel by inject()
    private var cab: MaterialCab? = null
    private var blockRequestPermissions = false

    override fun createContentView(): View {
        return wrapSlidingMusicPanel(R.layout.activity_main_content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setLightNavigationBar(true)
        setTaskDescriptionColorAuto()
        hideStatusBar()
        setBottomBarVisibility(View.VISIBLE)
        appLaunched(this)
        addMusicServiceEventListener(libraryViewModel)
        updateTabs()
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.fragment_container).navigateUp()

    override fun onResume() {
        super.onResume()
        PreferenceUtil.registerOnSharedPreferenceChangedListener(this)
        if (intent.hasExtra(EXPAND_PANEL) &&
            intent.getBooleanExtra(EXPAND_PANEL, false) &&
            PreferenceUtil.isExpandPanel
        ) {
            expandPanel()
            intent.removeExtra(EXPAND_PANEL)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceUtil.unregisterOnSharedPreferenceChangedListener(this)
    }

    override fun requestPermissions() {
        if (!blockRequestPermissions) {
            super.requestPermissions()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!hasPermissions()) {
            requestPermissions()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == GENERAL_THEME || key == BLACK_THEME || key == ADAPTIVE_COLOR_APP || key == USER_NAME || key == TOGGLE_FULL_SCREEN || key == TOGGLE_VOLUME || key == ROUND_CORNERS || key == CAROUSEL_EFFECT || key == NOW_PLAYING_SCREEN_ID || key == TOGGLE_GENRE || key == BANNER_IMAGE_PATH || key == PROFILE_IMAGE_PATH || key == CIRCULAR_ALBUM_ART || key == KEEP_SCREEN_ON || key == TOGGLE_SEPARATE_LINE || key == TOGGLE_HOME_BANNER || key == TOGGLE_ADD_CONTROLS || key == ALBUM_COVER_STYLE || key == HOME_ARTIST_GRID_STYLE || key == ALBUM_COVER_TRANSFORM || key == DESATURATED_COLOR || key == EXTRA_SONG_INFO || key == TAB_TEXT_MODE || key == LANGUAGE_NAME || key == LIBRARY_CATEGORIES
        ) {
            postRecreate()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        handlePlaybackIntent(intent)
    }

    private fun handlePlaybackIntent(intent: Intent?) {
        if (intent == null) {
            return
        }
        val uri = intent.data
        val mimeType = intent.type
        var handled = false
        if (intent.action != null && (intent.action == MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)
        ) {
            val songs: List<Song> =
                getSongs(this, intent.extras!!)
            if (shuffleMode == MusicService.SHUFFLE_MODE_SHUFFLE) {
                openAndShuffleQueue(songs, true)
            } else {
                openQueue(songs, 0, true)
            }
            handled = true
        }
        if (uri != null && uri.toString().isNotEmpty()) {
            playFromUri(uri)
            handled = true
        } else if (MediaStore.Audio.Playlists.CONTENT_TYPE == mimeType) {
            val id = parseIdFromIntent(intent, "playlistId", "playlist").toInt()
            if (id >= 0) {
                val position = intent.getIntExtra("position", 0)
                val songs: List<Song> =
                    ArrayList(getPlaylistSongList(this, id))
                openQueue(songs, position, true)
                handled = true
            }
        } else if (MediaStore.Audio.Albums.CONTENT_TYPE == mimeType) {
            val id = parseIdFromIntent(intent, "albumId", "album").toInt()
            if (id >= 0) {
                val position = intent.getIntExtra("position", 0)
                openQueue(getAlbum(this, id).songs!!, position, true)
                handled = true
            }
        } else if (MediaStore.Audio.Artists.CONTENT_TYPE == mimeType) {
            val id = parseIdFromIntent(intent, "artistId", "artist").toInt()
            if (id >= 0) {
                val position = intent.getIntExtra("position", 0)
                openQueue(getArtist(this, id).songs, position, true)
                handled = true
            }
        }
        if (handled) {
            setIntent(Intent())
        }
    }

    private fun parseIdFromIntent(
        intent: Intent, longKey: String,
        stringKey: String
    ): Long {
        var id = intent.getLongExtra(longKey, -1)
        if (id < 0) {
            val idString = intent.getStringExtra(stringKey)
            if (idString != null) {
                try {
                    id = idString.toLong()
                } catch (e: NumberFormatException) {
                    Log.e(TAG, e.message)
                }
            }
        }
        return id
    }

    override fun handleBackPress(): Boolean {
        if (cab != null && cab!!.isActive) {
            cab?.finish()
            return true
        }
        return super.handleBackPress()
    }

    override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
        cab?.let {
            if (it.isActive) it.finish()
        }
        cab = MaterialCab(this, R.id.cab_stub)
            .setMenu(menuRes)
            .setCloseDrawableRes(R.drawable.ic_close)
            .setBackgroundColor(
                RetroColorUtil.shiftBackgroundColorForLightText(
                    ATHUtil.resolveColor(
                        this,
                        R.attr.colorSurface
                    )
                )
            )
            .start(callback)
        return cab as MaterialCab
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        when (dialog.title) {
            R.string.accent_color -> {
                ThemeStore.editTheme(this).accentColor(selectedColor).commit()
                if (VersionUtils.hasNougatMR())
                    DynamicShortcutManager(this).updateDynamicShortcuts()
            }
        }
        recreate()
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {

    }
}