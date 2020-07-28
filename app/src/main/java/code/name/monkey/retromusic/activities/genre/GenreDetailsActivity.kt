package code.name.monkey.retromusic.activities.genre

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.adapter.song.ShuffleButtonSongAdapter
import code.name.monkey.retromusic.extensions.applyToolbar
import code.name.monkey.retromusic.extensions.extraNotNull
import code.name.monkey.retromusic.helper.menu.GenreMenuHelper
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.DensityUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import com.afollestad.materialcab.MaterialCab
import kotlinx.android.synthetic.main.activity_playlist_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

/**
 * @author Hemanth S (h4h13).
 */

class GenreDetailsActivity : AbsSlidingMusicPanelActivity(), CabHolder {


    private val detailsViewModel: GenreDetailsViewModel by viewModel {
        parametersOf(extraNotNull<Genre>(EXTRA_GENRE_ID).value)
    }

    private lateinit var genre: Genre
    private lateinit var songAdapter: ShuffleButtonSongAdapter
    private var cab: MaterialCab? = null

    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    private fun checkIsEmpty() {
        checkForPadding()
        emptyEmoji.text = getEmojiByUnicode(0x1F631)
        empty?.visibility = if (songAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    private fun checkForPadding() {
        val height = DensityUtil.dip2px(this, 52f)
        recyclerView.setPadding(0, 0, 0, (height))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)
        setBottomBarVisibility(View.GONE)
        applyToolbar(toolbar)
        setupRecyclerView()

        detailsViewModel.getSongs().observe(this, androidx.lifecycle.Observer {
            songs(it)
        })

        detailsViewModel.getGenre().observe(this, androidx.lifecycle.Observer {
            genre = it
            supportActionBar?.title = it.name
        })

        addMusicServiceEventListener(detailsViewModel)
    }

    override fun createContentView(): View {
        return wrapSlidingMusicPanel(R.layout.activity_playlist_detail)
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
        songAdapter = ShuffleButtonSongAdapter(this, ArrayList(), R.layout.item_list, this)
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

    fun songs(songs: List<Song>) {
        songAdapter.swapDataSet(songs)
    }

    override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
        if (cab != null && cab!!.isActive) cab?.finish()
        cab = MaterialCab(this, R.id.cab_stub).setMenu(menuRes)
            .setCloseDrawableRes(R.drawable.ic_close)
            .setBackgroundColor(
                RetroColorUtil.shiftBackgroundColorForLightText(
                    ATHUtil.resolveColor(
                        this,
                        R.attr.colorSurface
                    )
                )
            ).start(callback)
        return cab!!
    }

    override fun onBackPressed() {
        if (cab != null && cab!!.isActive) cab!!.finish()
        else {
            recyclerView!!.stopScroll()
            super.onBackPressed()
        }
    }

    companion object {
        const val EXTRA_GENRE_ID = "extra_genre_id"
    }
}
