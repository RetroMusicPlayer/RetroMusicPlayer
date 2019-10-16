package code.name.monkey.retromusic.activities

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.adapter.song.ShuffleButtonSongAdapter
import code.name.monkey.retromusic.extensions.applyToolbar
import code.name.monkey.retromusic.helper.menu.GenreMenuHelper
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.presenter.GenreDetailsPresenter
import code.name.monkey.retromusic.mvp.presenter.GenreDetailsView
import code.name.monkey.retromusic.util.RetroColorUtil
import code.name.monkey.retromusic.util.ViewUtil
import com.afollestad.materialcab.MaterialCab
import kotlinx.android.synthetic.main.activity_playlist_detail.*
import java.util.*
import javax.inject.Inject

/**
 * @author Hemanth S (h4h13).
 */

class GenreDetailsActivity : AbsSlidingMusicPanelActivity(), CabHolder, GenreDetailsView {

    @Inject
    lateinit var genreDetailsPresenter: GenreDetailsPresenter

    private lateinit var genre: Genre
    private lateinit var songAdapter: ShuffleButtonSongAdapter
    private var cab: MaterialCab? = null

    private fun checkIsEmpty() {
        empty?.visibility = if (songAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)

        setStatusbarColor(Color.TRANSPARENT)
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)
        setLightStatusbar(ColorUtil.isColorLight(ATHUtil.resolveColor(this, R.attr.colorPrimary)))
        toggleBottomNavigationView(true)

        if (intent.extras != null) {
            genre = intent?.extras?.getParcelable(EXTRA_GENRE_ID)!!
        } else {
            finish()
        }

        setUpToolBar()
        setupRecyclerView()

        App.musicComponent.inject(this)
        genreDetailsPresenter.attachView(this)

    }

    private fun setUpToolBar() {
        val primaryColor = ATHUtil.resolveColor(this, R.attr.colorPrimary)
        appBarLayout.setBackgroundColor(primaryColor)
        applyToolbar(toolbar)
        title = genre.name
    }

    override fun onResume() {
        super.onResume()
        genreDetailsPresenter.loadGenreSongs(genre.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        genreDetailsPresenter.detachView()
    }

    override fun createContentView(): View {
        return wrapSlidingMusicPanel(R.layout.activity_playlist_detail)
    }

    override fun showEmptyView() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_genre_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return GenreMenuHelper.handleMenuClick(this, genre, item)
    }

    private fun setupRecyclerView() {
        ViewUtil.setUpFastScrollRecyclerViewColor(this, recyclerView, ThemeStore.accentColor(this))
        songAdapter = ShuffleButtonSongAdapter(this, ArrayList(), R.layout.item_list, false, this)
        recyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this@GenreDetailsActivity)
            adapter = songAdapter
        }
        songAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
            }
        })
    }

    override fun songs(songs: ArrayList<Song>) {
        songAdapter.swapDataSet(songs)
    }

    override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
        if (cab != null && cab!!.isActive) cab!!.finish()
        cab = MaterialCab(this, R.id.cab_stub)
                .setMenu(menuRes)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(RetroColorUtil.shiftBackgroundColorForLightText(ATHUtil.resolveColor(this, R.attr.colorPrimary)))
                .start(callback)
        return cab!!
    }

    override fun onBackPressed() {
        if (cab != null && cab!!.isActive)
            cab!!.finish()
        else {
            recyclerView!!.stopScroll()
            super.onBackPressed()
        }
    }

    override fun onMediaStoreChanged() {
        super.onMediaStoreChanged()
        genreDetailsPresenter.loadGenreSongs(genre.id)
    }

    companion object {
        const val EXTRA_GENRE_ID = "extra_genre_id"
    }
}
