package code.name.monkey.retromusic.adapter.base

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.interfaces.ICabHolder
import com.afollestad.materialcab.MaterialCab
import java.util.*

abstract class AbsMultiSelectAdapter<V : RecyclerView.ViewHolder?, I>(
    context: Context, private val ICabHolder: ICabHolder?, @MenuRes menuRes: Int
) : RecyclerView.Adapter<V>(), MaterialCab.Callback {
    private val context: Context
    private var cab: MaterialCab? = null
    private val checked: MutableList<I>
    private var menuRes: Int
    override fun onCabCreated(materialCab: MaterialCab, menu: Menu): Boolean {
        return true
    }

    override fun onCabFinished(materialCab: MaterialCab): Boolean {
        clearChecked()
        return true
    }

    override fun onCabItemClicked(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.action_multi_select_adapter_check_all) {
            checkAll()
        } else {
            onMultipleItemAction(menuItem, ArrayList(checked))
            cab!!.finish()
            clearChecked()
        }
        return true
    }

    private fun checkAll() {
        if (ICabHolder != null) {
            checked.clear()
            for (i in 0 until itemCount) {
                val identifier = getIdentifier(i)
                if (identifier != null) {
                    checked.add(identifier)
                }
            }
            notifyDataSetChanged()
            updateCab()
        }
    }

    protected abstract fun getIdentifier(position: Int): I?
    protected open fun getName(`object`: I): String? {
        return `object`.toString()
    }

    protected fun isChecked(identifier: I): Boolean {
        return checked.contains(identifier)
    }

    protected val isInQuickSelectMode: Boolean
        get() = cab != null && cab!!.isActive

    protected abstract fun onMultipleItemAction(menuItem: MenuItem?, selection: List<I>?)
    protected fun setMultiSelectMenuRes(@MenuRes menuRes: Int) {
        this.menuRes = menuRes
    }

    protected fun toggleChecked(position: Int): Boolean {
        if (ICabHolder != null) {
            val identifier = getIdentifier(position) ?: return false
            if (!checked.remove(identifier)) {
                checked.add(identifier)
            }
            notifyItemChanged(position)
            updateCab()
            return true
        }
        return false
    }

    private fun clearChecked() {
        checked.clear()
        notifyDataSetChanged()
    }

    private fun updateCab() {
        if (ICabHolder != null) {
            if (cab == null || !cab!!.isActive) {
                cab = ICabHolder.openCab(menuRes, this)
            }
            val size = checked.size
            when {
                size <= 0 -> {
                    cab!!.finish()
                }
                size == 1 -> {
                    cab!!.setTitle(getName(checked[0]))
                }
                else -> {
                    cab!!.setTitle(context.getString(R.string.x_selected, size))
                }
            }
        }
    }

    init {
        checked = ArrayList()
        this.menuRes = menuRes
        this.context = context
    }
}