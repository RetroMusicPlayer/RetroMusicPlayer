package code.name.monkey.retromusic.fragments.base

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.util.DensityUtil
import code.name.monkey.retromusic.util.ViewUtil
import com.google.android.material.appbar.AppBarLayout
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import kotlinx.android.synthetic.main.fragment_main_activity_recycler_view.*

abstract class AbsLibraryPagerRecyclerViewFragment<A : RecyclerView.Adapter<*>, LM : RecyclerView.LayoutManager> : AbsLibraryPagerFragment(), AppBarLayout.OnOffsetChangedListener {

    protected var adapter: A? = null
    protected var layoutManager: LM? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_activity_recycler_view, container, false);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryFragment.addOnAppBarOffsetChangedListener(this)
        initLayoutManager()
        initAdapter()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        if (recyclerView is FastScrollRecyclerView) {
            ViewUtil.setUpFastScrollRecyclerViewColor(activity!!, recyclerView as FastScrollRecyclerView, ThemeStore.accentColor(activity!!))
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return -1
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                ViewCompat.animate(viewHolder.itemView)
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .start()
            }

            override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView())
    }

    private fun initAdapter() {
        adapter = createAdapter()
        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
                checkForPadding()
            }
        })
    }

    protected open val emptyMessage: Int
        @StringRes
        get() = R.string.empty

    private fun checkIsEmpty() {
        emptyText.setText(emptyMessage)
        empty.visibility = if (adapter!!.itemCount == 0) View.VISIBLE else View.GONE
    }

    private fun checkForPadding() {
        val height = if (MusicPlayerRemote.playingQueue.isEmpty())
            DensityUtil.dip2px(context!!, 52f)
        else
            0
        recyclerView.setPadding(0, 0, 0, (height * 2.3).toInt())
    }

    private fun initLayoutManager() {
        layoutManager = createLayoutManager()
    }

    protected abstract fun createLayoutManager(): LM

    @NonNull
    protected abstract fun createAdapter(): A

    override fun onOffsetChanged(p0: AppBarLayout?, i: Int) {
        container.setPadding(container.paddingLeft, container.paddingTop,
                container.paddingRight, libraryFragment.totalAppBarScrollingRange + i)
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        checkForPadding()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        checkForPadding()
    }

    protected fun invalidateLayoutManager() {
        initLayoutManager()
        recyclerView.layoutManager = layoutManager
    }

    protected fun invalidateAdapter() {
        initAdapter()
        checkIsEmpty()
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        libraryFragment.removeOnAppBarOffsetChangedListener(this)
    }

    fun recyclerView(): RecyclerView {
        return recyclerView
    }
}