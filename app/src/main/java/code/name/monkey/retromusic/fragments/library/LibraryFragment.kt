package code.name.monkey.retromusic.fragments.library

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import code.name.monkey.appthemehelper.common.ATHToolbarActivity.getToolbarBackgroundColor
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.findNavController
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.fragments.folder.FoldersFragment
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.appbar.AppBarLayout
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
        mainActivity.getBottomNavigationView().setOnNavigationItemSelectedListener {
            var handled = false
            if (navController.graph.findNode(it.itemId) != null) {
                navController.navigate(it.itemId, null, navOptions)
                handled = true
            }
            when (it.itemId) {
                R.id.action_folder -> navController.navigate(
                    R.id.action_folder,
                    Bundle().apply {
                        putSerializable(
                            FoldersFragment.PATH,
                            PreferenceUtil.startDirectory
                        )
                    },
                    navOptions
                )
            }
            return@setOnNavigationItemSelectedListener handled
        }
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