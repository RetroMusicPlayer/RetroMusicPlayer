package code.name.monkey.retromusic.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.ThemedFastScroller.create
import kotlinx.android.synthetic.main.fragment_main_activity_recycler_view.*
import me.zhanghai.android.fastscroll.FastScroller
import me.zhanghai.android.fastscroll.FastScrollerBuilder

abstract class AbsLibraryPagerRecyclerViewFragment<A : RecyclerView.Adapter<*>, LM : RecyclerView.LayoutManager> :
    AbsLibraryPagerFragment() {

    protected var adapter: A? = null
    protected var layoutManager: LM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_activity_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutManager()
        initAdapter()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val fastScroller = create(recyclerView)
    }

    protected open fun createFastScroller(recyclerView: RecyclerView): FastScroller {
        return FastScrollerBuilder(recyclerView).useMd2Style().build()
    }

    private fun initAdapter() {
        adapter = createAdapter()
        adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
            }
        })
    }

    protected open val emptyMessage: Int
        @StringRes get() = R.string.empty

    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    private fun checkIsEmpty() {
        emptyEmoji.text = getEmojiByUnicode(0x1F631)
        emptyText.setText(emptyMessage)
        empty.visibility = if (adapter!!.itemCount == 0) View.VISIBLE else View.GONE
    }

    private fun initLayoutManager() {
        layoutManager = createLayoutManager()
    }

    protected abstract fun createLayoutManager(): LM

    @NonNull
    protected abstract fun createAdapter(): A

    protected fun invalidateLayoutManager() {
        initLayoutManager()
        recyclerView.layoutManager = layoutManager
    }

    protected fun invalidateAdapter() {
        initAdapter()
        checkIsEmpty()
        recyclerView.adapter = adapter
    }

    fun recyclerView(): RecyclerView {
        return recyclerView
    }
}