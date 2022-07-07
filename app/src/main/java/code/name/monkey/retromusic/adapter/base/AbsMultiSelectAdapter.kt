package code.name.monkey.retromusic.adapter.base

import android.graphics.Color
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.annotation.MenuRes
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.NumberRollViewBinding
import code.name.monkey.retromusic.views.NumberRollView

abstract class AbsMultiSelectAdapter<V : RecyclerView.ViewHolder?, I>(
    open val activity: FragmentActivity, @MenuRes menuRes: Int,
) : RecyclerView.Adapter<V>(), ActionMode.Callback {
    var actionMode: ActionMode? = null
    private val checked: MutableList<I>
    private var menuRes: Int

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater = mode?.menuInflater
        inflater?.inflate(menuRes, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_multi_select_adapter_check_all) {
            checkAll()
        } else {
            onMultipleItemAction(item!!, ArrayList(checked))
            actionMode?.finish()
            clearChecked()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        clearChecked()
        activity.window.statusBarColor = when {
            VersionUtils.hasMarshmallow() -> Color.TRANSPARENT
            else -> Color.BLACK
        }
        actionMode = null
        onBackPressedCallback.remove()
    }

    private fun checkAll() {
        if (actionMode != null) {
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

    protected abstract fun getName(model: I): String?

    protected fun isChecked(identifier: I): Boolean {
        return checked.contains(identifier)
    }

    protected val isInQuickSelectMode: Boolean
        get() = actionMode != null

    protected abstract fun onMultipleItemAction(menuItem: MenuItem, selection: List<I>)
    protected fun setMultiSelectMenuRes(@MenuRes menuRes: Int) {
        this.menuRes = menuRes
    }

    protected fun toggleChecked(position: Int): Boolean {
        val identifier = getIdentifier(position) ?: return false
        if (!checked.remove(identifier)) {
            checked.add(identifier)
        }
        notifyItemChanged(position)
        updateCab()
        return true
    }

    private fun clearChecked() {
        checked.clear()
        notifyDataSetChanged()
    }

    private fun updateCab() {
        if (actionMode == null) {
            actionMode = activity.startActionMode(this)?.apply {
                customView = NumberRollViewBinding.inflate(activity.layoutInflater).root
            }
            activity.onBackPressedDispatcher.addCallback(onBackPressedCallback)
        }
        val size = checked.size
        when {
            size <= 0 -> {
                actionMode?.finish()
            }
            else -> {
                actionMode?.customView?.findViewById<NumberRollView>(R.id.selection_mode_number)
                    ?.setNumber(size, true)
            }
        }
    }

    init {
        checked = ArrayList()
        this.menuRes = menuRes
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (actionMode != null) {
                actionMode?.finish()
                remove()
            }
        }
    }
}