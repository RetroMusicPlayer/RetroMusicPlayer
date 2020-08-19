package io.github.muntashirakon.music.fragments.library

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import code.name.monkey.appthemehelper.common.ATHToolbarActivity.getToolbarBackgroundColor
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import com.google.android.material.appbar.AppBarLayout
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.extensions.findNavController
import io.github.muntashirakon.music.fragments.base.AbsMainActivityFragment
import kotlinx.android.synthetic.main.fragment_library.*

class LibraryFragment : AbsMainActivityFragment(R.layout.fragment_library) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity.hideBottomBarVisibility(true)
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar?.title = null
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(
                R.id.searchFragment,
                null,
                navOptions
            )
        }
        setupNavigationController()
    }

    private fun setupNavigationController() {
        val navController = findNavController(R.id.fragment_container)
        NavigationUI.setupWithNavController(mainActivity.getBottomNavigationView(), navController)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(),
            toolbar,
            menu,
            getToolbarBackgroundColor(toolbar)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> findNavController().navigate(
                R.id.settingsActivity,
                null,
                navOptions
            )
        }
        return super.onOptionsItemSelected(item)
    }

    fun addOnAppBarOffsetChangedListener(changedListener: AppBarLayout.OnOffsetChangedListener) {
        appBarLayout.addOnOffsetChangedListener(changedListener)
    }

    fun removeOnAppBarOffsetChangedListener(changedListener: AppBarLayout.OnOffsetChangedListener) {
        appBarLayout.removeOnOffsetChangedListener(changedListener)
    }

    fun getTotalAppBarScrollingRange(): Int {
        return 0
    }
}