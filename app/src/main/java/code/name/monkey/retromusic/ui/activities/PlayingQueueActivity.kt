package code.name.monkey.retromusic.ui.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter
import code.name.monkey.retromusic.util.MusicUtil
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils
import kotlinx.android.synthetic.main.activity_playing_queue.*


class PlayingQueueActivity : AbsMusicServiceActivity() {

    private var wrappedAdapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewDragDropManager: RecyclerViewDragDropManager? = null
    private var playingQueueAdapter: PlayingQueueAdapter? = null
    private lateinit var layoutManager: LinearLayoutManager

    private val upNextAndQueueTime: String
        get() = resources.getString(R.string.up_next) + "  •  " + MusicUtil.getReadableDurationString(MusicPlayerRemote.getQueueDurationMillis(MusicPlayerRemote.position))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing_queue)

        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        setupToolbar()
        setUpRecyclerView()

        clearQueue.setOnClickListener {
            MusicPlayerRemote.clearQueue()
        }
    }

    private fun setUpRecyclerView() {
        recyclerViewDragDropManager = RecyclerViewDragDropManager()
        val animator = RefactoredDefaultItemAnimator()

        playingQueueAdapter = PlayingQueueAdapter(
                this,
                MusicPlayerRemote.playingQueue,
                MusicPlayerRemote.position,
                R.layout.item_queue)
        wrappedAdapter = recyclerViewDragDropManager!!.createWrappedAdapter(playingQueueAdapter!!)

        layoutManager = LinearLayoutManager(this)

        recyclerView.apply {
            layoutManager = layoutManager
            adapter = wrappedAdapter
            itemAnimator = animator
            recyclerViewDragDropManager!!.attachRecyclerView(this)
        }

        layoutManager.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    clearQueue.setShowTitle(false)
                } else if (dy < 0) {
                    clearQueue.setShowTitle(true)
                }
            }
        })
    }

    override fun onQueueChanged() {
        if (MusicPlayerRemote.playingQueue.isEmpty()) {
            finish()
            return
        }
        updateQueue()
        updateCurrentSong()
    }

    override fun onMediaStoreChanged() {
        updateQueue()
        updateCurrentSong()
    }

    private fun updateCurrentSong() {}

    override fun onPlayingMetaChanged() {
        updateQueuePosition()
    }

    private fun updateQueuePosition() {
        playingQueueAdapter!!.setCurrent(MusicPlayerRemote.position)
        resetToCurrentPosition()
    }

    private fun updateQueue() {
        playingQueueAdapter!!.swapDataSet(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
        resetToCurrentPosition()
    }

    private fun resetToCurrentPosition() {
        recyclerView.stopScroll()
        layoutManager.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }

    override fun onPause() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager!!.cancelDrag()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager!!.release()
            recyclerViewDragDropManager = null
        }



        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter)
            wrappedAdapter = null
        }
        playingQueueAdapter = null
        super.onDestroy()
    }

    private fun setupToolbar() {
        bannerTitle.setTextColor(ThemeStore.textColorPrimary(this))
        playerQueueSubHeader.text = upNextAndQueueTime
        playerQueueSubHeader.setTextColor(ThemeStore.accentColor(this))

        appBarLayout.setBackgroundColor(ThemeStore.primaryColor(this))
        toolbar.setBackgroundColor(ThemeStore.primaryColor(this))
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        setSupportActionBar(toolbar)
        title = null
        toolbar.setNavigationOnClickListener { onBackPressed() }
        ToolbarContentTintHelper.colorBackButton(toolbar, ThemeStore.accentColor(this))
        clearQueue.setColor(ThemeStore.accentColor(this))
    }
}
